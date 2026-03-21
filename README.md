# Sky Oracle — Android Weather App

Sky Oracle is a modern Android weather application built with Jetpack Compose and Material 3.  
It allows users to search for cities and view real-time weather data with a clean, card-based UI.

---

## Features

- Search weather by city name
- View temperature, condition, humidity, and wind
- Loading state while fetching data
- Error handling with retry option
- Search history saved locally (Room or simple persistence)
- Clean Material 3 UI with card-based layout
- Floating Action Button for primary action

---

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- ViewModel + StateFlow
- Retrofit (API calls)
- Room (local database)

---
## API

**OpenWeatherMap API**
Used to fetch real-time weather data
Endpoints include current weather and forecast data
Integrated using Retrofit

## Architecture

This project follows a simple layered architecture:

UI (Compose)
   ↓
ViewModel
   ↓
Repository
   ↓
API / Room

**UI Layer**
Built with Jetpack Compose
Displays weather data, loading state, and error state
Observes UI state from the ViewModel

**ViewModel Layer**
Holds and manages UI state using StateFlow
Handles business logic for searching cities and fetching weather
Exposes data to the UI in a lifecycle-aware way

**Repository Layer**
Acts as the single source of truth for weather data
Fetches remote data from the weather API
Handles local data storage for search history

**Local Data Layer**
Uses Room database to save search history or cached data
Remote Data Layer
Uses Retrofit to call the weather API and parse responses

---

## Screenshots
<img width="516" height="1157" alt="image" src="https://github.com/user-attachments/assets/75d98e65-4cb3-49d6-8ef8-826dc10d7091" />
<img width="517" height="1156" alt="image" src="https://github.com/user-attachments/assets/a4496666-01bd-4a23-87fa-e602ade68ed6" />
<img width="512" height="1157" alt="image" src="https://github.com/user-attachments/assets/68d92395-cd0e-4036-915d-13435b58b619" />


---

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/your-username/android-weather-app-sky-oracle.git
