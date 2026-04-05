<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Java Weather App</title>
    <link rel="stylesheet" href="style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="container">
        <div class="search-box">
            <h2>Weather Forecast</h2>
            <% if (request.getAttribute("error") != null) { %>
                <div style="background: rgba(255, 77, 77, 0.2); border: 1px solid #ff4d4d; border-radius: 10px; padding: 10px; margin-bottom: 20px; font-size: 0.9rem;">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="weather" method="GET" id="weatherForm">
                <input type="text" name="city" id="cityInput" placeholder="Enter city name..." required>
                <button type="submit" id="searchBtn">
                    <img src="https://cdn-icons-png.flaticon.com/512/54/54481.png" alt="search" width="20">
                </button>
            </form>
        </div>

        <div class="weather-card" id="weatherResult">
            <h1 class="city-name" id="displayCity">${not empty cityName ? cityName : 'City'}</h1>
            <div class="weather-details">
                <div class="temp-container">
                    <span class="temp" id="displayTemp">${not empty temperature ? temperature : '--'}</span><span class="unit">°C</span>
                </div>
                <p class="condition" id="displayCondition">${not empty condition ? condition : 'Waiting for input...'}</p>
                
                <div class="extra-info">
                    <div class="col">
                        <p class="label">Humidity</p>
                        <p class="value"><span id="displayHumidity">${not empty humidity ? humidity : '--'}</span>%</p>
                    </div>
                    <div class="col">
                        <p class="label">Wind Speed</p>
                        <p class="value"><span id="displayWind">${not empty windSpeed ? windSpeed : '--'}</span> km/h</p>
                    </div>
                </div>

                <div class="chart-container" style="margin-top: 20px; height: 120px; display: ${not empty forecastData ? 'block' : 'none'};">
                    <canvas id="forecastChart"></canvas>
                </div>

                <div class="daily-forecast" id="dailyForecast" style="display: ${not empty forecastData ? 'flex' : 'none'};">
                    </div>

            </div>
        </div>
    </div>

    <script>
        const forecastRawData = ${not empty forecastData ? forecastData : 'null'};
    </script>
    <script src="script.js"></script>
</body>
</html>