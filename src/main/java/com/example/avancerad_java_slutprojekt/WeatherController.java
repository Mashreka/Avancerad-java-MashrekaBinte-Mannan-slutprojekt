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

    @FXML
    private WebView webView;

    @FXML
    private TextArea weatherDisplay;

    @FXML
    private TextField locationField;

    private List<WeatherUpdateListener> listeners = new ArrayList<>();

    @FXML
    private void handleSearch() {
        String location = locationField.getText().trim();
        if (!location.isEmpty()) {
            webView.getEngine().load("https://www.google.com/maps/search/?api=1&query=" + location);
            fetchWeatherData(location);
        }
    }

    private void fetchWeatherData(String location) {
        new Thread(() -> {
            try {
                String apiKey =  "0c3cb85e26459a75d6bcf63fd7f1a7fa";
                URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                        location, apiKey));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                connection.disconnect();

                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                String weatherInfo = parseWeatherData(jsonObject);

                Platform.runLater(() -> updateWeatherDisplay(weatherInfo));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String parseWeatherData(JsonObject jsonObject) {
        String weatherDescription = jsonObject.getAsJsonArray("weather")
                .get(0).getAsJsonObject().get("description").getAsString();
        double temperature = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();

        return String.format("Weather: %s\nTemperature: %.1f°C", weatherDescription, temperature);
    }

    private void updateWeatherDisplay(String weatherInfo) {
        weatherDisplay.setText(weatherInfo);
        notifyWeatherUpdateListeners(weatherInfo);
    }

    public void addWeatherUpdateListener(WeatherUpdateListener listener) {
        listeners.add(listener);
    }

    private void notifyWeatherUpdateListeners(String weatherInfo) {
        for (WeatherUpdateListener listener : listeners) {
            listener.onWeatherUpdate(weatherInfo);
        }
    }
}
