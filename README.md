# Map & Weather Application

This Java-based Map & Weather Application allows users to search for a location, view it on Google Maps, and fetch real-time weather information using the OpenWeatherMap API.

## Features

- **Search Functionality:** Users can enter a location and view it on Google Maps within the app.
- **Weather Information:** Real-time weather data for the searched location, including temperature, weather description, pressure, and humidity.
- **Dynamic Styling:** Utilizes CSS for a user-friendly interface.

## Prerequisites

- Java Development Kit (JDK)
- IDE (Integrated Development Environment) such as IntelliJ IDEA or Eclipse

## Installation

1. Clone this repository to your local machine using `git clone`.
2. Open the project in your preferred IDE.
3. Configure the project SDK and dependencies.
4. Run the `MapWeatherApp.java` file to launch the application.

## Usage

1. Launch the application.
2. Enter a location in the provided text field.
3. Click the "Search" button.
4. View the location on Google Maps and receive real-time weather information in the displayed area.

## Structure

The project structure contains the following key components:

- `MapWeatherApp.java`: Main application class handling UI setup.
- `WeatherController.java`: Controller handling user interactions and API data fetching.
- `WeatherUpdateListener.java`:Interface that defines a callback method for receiving weather updates
- `weather_app.fxml`: FXML file defining the application's UI layout.
- `styles.css`: CSS file for styling the UI components.

## Credits

This application uses the OpenWeatherMap API for weather data and uses Google Maps API to display the location on a map.

---

Feel free to contribute, report issues, or suggest improvements!
