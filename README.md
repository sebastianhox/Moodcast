# Moodcast — Weather-Aware Activity Planner

An Android app that helps teenagers and young adults plan outdoor activities based on real-time weather conditions and forecasts. Built as a group project for the course **IN2000 — Software Engineering with Project Work** at the University of Oslo, spring 2024.

## What it does

Moodcast pulls live weather data from the Norwegian Meteorological Institute and surfaces activity suggestions tailored to current conditions and upcoming forecasts. Instead of just showing temperature and precipitation, the app interprets the weather and answers a more useful question: *what should I actually do outside today?*

Key features:
- Live weather data and forecasts from MET Norway's public API
- Activity recommendations driven by weather-based business logic (e.g. surfing when wind and waves align, hiking on dry days, indoor alternatives during severe weather alerts)
- Severe weather warnings integrated from MET's alert feed
- Clean, engaging UI built with Jetpack Compose, designed for a younger audience

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM with Kotlin Coroutines and Flow
- **Networking:** Retrofit + Moshi for API integration
- **APIs:** [MET Norway Locationforecast](https://api.met.no/) and [MetAlerts](https://api.met.no/weatherapi/metalerts/)
- **Tooling:** Android Studio, Git, GitHub

## My role

I worked as a developer in a team of six informatics students. My main contributions were:

- Building UI components in Jetpack Compose, including the activity recommendation views
- Integrating the MET Norway API for both forecast data and severe weather alerts
- Writing the rule-based logic that maps weather conditions to relevant activity suggestions
- Participating in user testing and iterating on the UX based on feedback
- Code reviews, pair programming, and architectural discussions with the team

## Project context

This was a semester-long agile project where the group acted as a small product team. We worked in iterations, held regular retrospectives, and delivered a working prototype as our final submission. The course emphasized real-world software engineering practice — requirements gathering, version control, code review, and user testing — alongside the actual development work.

## Screenshots

<img width="1586" height="574" alt="image" src="https://github.com/user-attachments/assets/25583aab-1634-4b61-a15c-267fe4028060" />

## Acknowledgments
Built together with five fellow informatics students at the University of Oslo. Weather data and alerts provided by the Norwegian Meteorological Institute under their open data license.
