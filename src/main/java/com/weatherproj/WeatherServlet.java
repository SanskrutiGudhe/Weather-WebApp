package com.weatherproj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Insert your OpenWeatherMap API key here
    private static final String API_KEY = "990c6d4dd98858c4393f0ededd9820c8"; 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String city = request.getParameter("city");
        
        if (city == null || city.trim().isEmpty()) {
            request.setAttribute("error", "Please enter a valid city name.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        try {
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            
            // --- CALL 1: CURRENT WEATHER ---
            String currentUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric";
            HttpURLConnection currentConn = (HttpURLConnection) URI.create(currentUrl).toURL().openConnection();
            currentConn.setRequestMethod("GET");

            int responseCode = currentConn.getResponseCode();

            switch (responseCode) {
                case HttpURLConnection.HTTP_OK -> {
                    // Parse Current Weather
                    StringBuilder currentJson = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(currentConn.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) currentJson.append(line);
                    }

                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(currentJson.toString(), JsonObject.class);

                    double temp = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
                    int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
                    String condition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                    String cityName = jsonObject.get("name").getAsString();
                    double windSpeedMs = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
                    double windSpeedKmh = Math.round(windSpeedMs * 3.6 * 10.0) / 10.0;

                    request.setAttribute("temperature", temp);
                    request.setAttribute("humidity", humidity);
                    request.setAttribute("condition", condition);
                    request.setAttribute("cityName", cityName);
                    request.setAttribute("windSpeed", windSpeedKmh);

                    // --- CALL 2: 5-DAY FORECAST DATA ---
                    String forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric";
                    HttpURLConnection forecastConn = (HttpURLConnection) URI.create(forecastUrl).toURL().openConnection();
                    forecastConn.setRequestMethod("GET");
                    
                    if(forecastConn.getResponseCode() == 200) {
                        StringBuilder forecastJson = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(forecastConn.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) forecastJson.append(line);
                        }
                        // Send the raw JSON string to the frontend so JavaScript can draw the chart!
                        request.setAttribute("forecastData", forecastJson.toString());
                    }
                    
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }

                case HttpURLConnection.HTTP_NOT_FOUND -> {
                    request.setAttribute("error", "City not found. Try again.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }

                default -> {
                    request.setAttribute("error", "API Error Code: " + responseCode);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
            }
            
            currentConn.disconnect();

        } catch (IOException | RuntimeException e) {
            getServletContext().log("Weather API Error: ", e);
            request.setAttribute("error", "Server error: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}