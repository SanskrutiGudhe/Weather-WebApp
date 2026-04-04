document.addEventListener('DOMContentLoaded', () => {
    
    // --- MOCK DATA SECTION (Frontend Testing) ---
    // Change this to 'false' to test the "waiting for input" state
    const isDataLoaded = true; 

    // Define mock weather data
    const mockData = {
        city: "Mumbai",
        temperature: "29",
        condition: "Partly Cloudy",
        humidity: "75",
        windSpeed: "15"
    };
    // ---------------------------------------------


    const displayElements = {
        city: document.getElementById('displayCity'),
        temp: document.getElementById('displayTemp'),
        condition: document.getElementById('displayCondition'),
        humidity: document.getElementById('displayHumidity'),
        wind: document.getElementById('displayWind')
    };

    // Update the UI based on whether data is available
    if (isDataLoaded) {
        displayElements.city.textContent = mockData.city;
        displayElements.temp.textContent = mockData.temperature;
        displayElements.condition.textContent = mockData.condition;
        displayElements.humidity.textContent = mockData.humidity;
        displayElements.wind.textContent = mockData.windSpeed;
    } else {
        // Keep default placeholders if data is not loaded
    }

    // --- FORM SUBMISSION HANDLER ---
    const form = document.getElementById('weatherForm');
    const cityInput = document.getElementById('cityInput');
    const searchBtn = document.getElementById('searchBtn');

    form.addEventListener('submit', (e) => {
        // Prevent submission if empty (just UX cleanup, not security)
        if (cityInput.value.trim() === "") {
            e.preventDefault();
            alert("Please enter a city name");
            return;
        }

        // Add a simple loading effect
        searchBtn.innerHTML = "⌛";
        searchBtn.style.pointerEvents = "none";
    });
});
