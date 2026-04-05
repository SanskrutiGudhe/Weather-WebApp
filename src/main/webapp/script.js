document.addEventListener('DOMContentLoaded', () => {
    // --- FORM SUBMISSION HANDLER ---
    const form = document.getElementById('weatherForm');
    const cityInput = document.getElementById('cityInput');
    const searchBtn = document.getElementById('searchBtn');

    form.addEventListener('submit', (e) => {
        if (cityInput.value.trim() === "") {
            e.preventDefault();
            alert("Please enter a city name");
            return;
        }
        searchBtn.innerHTML = "⌛";
        searchBtn.style.pointerEvents = "none";
    });

    // --- DRAW CHART & FORECAST IF DATA EXISTS ---
    if (forecastRawData && forecastRawData.list) {
        
        // 1. Build the 24-Hour Graph (Next 8 data points = 24 hours)
        const next24Hours = forecastRawData.list.slice(0, 8);
        
        // Extract times (e.g., "15:00") and temperatures
        const labels = next24Hours.map(item => {
            let date = new Date(item.dt * 1000);
            return date.getHours() + ":00";
        });
        const temps = next24Hours.map(item => Math.round(item.main.temp));

        const ctx = document.getElementById('forecastChart').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Temperature',
                    data: temps,
                    borderColor: '#ffeb3b', // Yellow line like Google
                    backgroundColor: 'rgba(255, 235, 59, 0.2)', // Light yellow fill
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4, // Makes the line curved!
                    pointRadius: 3,
                    pointBackgroundColor: '#ffffff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    x: {
                        grid: { display: false, drawBorder: false },
                        ticks: { color: 'rgba(255, 255, 255, 0.8)' }
                    },
                    y: {
                        display: false, // Hide Y axis to look like Google's clean graph
                        min: Math.min(...temps) - 2 // Add padding to bottom
                    }
                }
            }
        });

        // 2. Build the 5-Day Daily Forecast
        // OpenWeatherMap gives data every 3 hours. We will filter the list to only grab the forecast for 12:00 PM each day.
        const dailyData = forecastRawData.list.filter(item => item.dt_txt.includes("12:00:00"));
        
        const forecastContainer = document.getElementById('dailyForecast');
        const daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

        dailyData.forEach(day => {
            let date = new Date(day.dt * 1000);
            let dayName = daysOfWeek[date.getDay()];
            let temp = Math.round(day.main.temp);

            let box = document.createElement('div');
            box.className = 'daily-box';
            box.innerHTML = `
                <div class="daily-day">${dayName}</div>
                <div class="daily-temp">${temp}°</div>
            `;
            forecastContainer.appendChild(box);
        });
    }
});