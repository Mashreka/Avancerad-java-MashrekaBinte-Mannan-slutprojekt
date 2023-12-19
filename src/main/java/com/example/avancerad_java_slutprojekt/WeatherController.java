package com.example.avancerad_java_slutprojekt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherController {
    //instance variables annotated with @FXML,
    // JavaFX UI components defined in an associated FXML file
    @FXML
    private WebView webView; //displaying a map
    @FXML
    private TextArea weatherDisplay; //displaying weather information

    @FXML
    private TextField locationField; //entering a location

    private List<WeatherUpdateListener> listeners = new ArrayList<>();//a list of WeatherUpdateListener objects

    @FXML
    private void handleSearch() {
        String location = locationField.getText().trim();
        if (!location.isEmpty()) {
            webView.getEngine().load("https://www.google.com/maps/search/?api=1&query=" + location);//get Google map based on location
            fetchWeatherData(location);
        }
    }

    private void fetchWeatherData(String location) {   //fetching weather data from an API
        new Thread(() -> {  // the UI remains responsive while the weather data is fetched from the API
            try {
                // Use openWeatherMap API to retrieve weather data
                //construct a URL using the location and API key to make a request to the OpenWeatherMap API.
                String apiKey =  "0c3cb85e26459a75d6bcf63fd7f1a7fa";
                URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                        location, apiKey));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//to establish a connection with the API
                connection.setRequestMethod("GET");//and send a GET request to retrieve the weather data.
                //read the response from the API using a BufferedReader
                // and append each line to a StringBuilder
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);

                }
                reader.close(); //close the reader
                connection.disconnect();//disconnect the connection

                Gson gson = new Gson();  //for JSON parsing
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                System.out.println(jsonObject);
                String weatherInfo = parseWeatherData(jsonObject);

                Platform.runLater(() -> updateWeatherDisplay(weatherInfo)); //to update the weather display on the UI

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    //extracts the required information from the JSON object and formats it into a string
    private String parseWeatherData(JsonObject jsonObject) {
        String locationName = jsonObject.get("name").getAsString();
        String weatherDescription = jsonObject.getAsJsonArray("weather")
                .get(0).getAsJsonObject().get("description").getAsString();
        JsonObject main = jsonObject.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        double feelsLike = main.get("feels_like").getAsDouble();
        int pressure = main.get("pressure").getAsInt();
        int humidity = main.get("humidity").getAsInt();
        //to format the extracted information into a readable string
        return String.format("Location: %s\nWeather: %s\nTemperature: %.2f°C\nFeels Like: %.2f°C\nPressure: %d hPa\nHumidity: %d%%",
                locationName, weatherDescription, temperature, feelsLike, pressure, humidity);
    }

    private void updateWeatherDisplay(String weatherInfo) {
        weatherDisplay.setText(weatherInfo); //shows the current weather information in the textArea
        notifyWeatherUpdateListeners(weatherInfo);
    }
    //a listener to the listeners list,
    // which stores WeatherUpdateListener objects.
    public void addWeatherUpdateListener(WeatherUpdateListener listener) {
        listeners.add(listener);

    }
    //iterates over the list of listeners and calls the onWeatherUpdate method of each listener,
    // passing the weatherInfo string as an argument
    private void notifyWeatherUpdateListeners(String weatherInfo) {
        for (WeatherUpdateListener listener : listeners) {
            listener.onWeatherUpdate(weatherInfo);
        }
    }
}
