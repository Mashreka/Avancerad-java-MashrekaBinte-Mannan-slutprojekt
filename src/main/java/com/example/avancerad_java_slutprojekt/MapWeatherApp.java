package com.example.avancerad_java_slutprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MapWeatherApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("weather_app.fxml"));//Loads the UI components
        Parent root = loader.load();//root node of the scene graph

        com.example.avancerad_java_slutprojekt.WeatherController controller = loader.getController();//Retrieves the controller instance
        // Optionally pass any dependencies or configurations to the controller

        controller.addWeatherUpdateListener(weatherInfo -> {
            // Handle weather data
            System.out.println("Received updated weather info: " + weatherInfo);
        });

        Scene scene = new Scene(root, 800, 600);//Creates a new Scene object with the root node

        URL cssFile = getClass().getResource("/com/example/avancerad_java_slutprojekt/styles.css");
        if (cssFile != null) {
            System.out.println("CSS file loaded: " + cssFile.toExternalForm());
            scene.getStylesheets().add(cssFile.toExternalForm());//adds styles.css to the scene's stylesheets.
        } else {
            System.out.println("CSS file not found");
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle("Map & Weather App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); //starts the JavaFX application
    }
}
