# team-31

Teamet:
Emil
Åsmund
Avrin
Sebastian
Alexander
Marius

# Instruks
Last ned zip fil og kan kjøres på Android enhet, eller i emulator i Android Studio.
Fungerer på API 26 og oppover, testet tilogmed API 34.

MoodCast er en app som både viser deg været, og kommer med forslag til aktiviteter som kan gjøres.
Været vises med lufttemperaturen ved siste API-kall, vind med vind-retning, nedbørsmengde og luftfuktighet.

Det vises en varseltrekant hvis det ligger farevarsel for dette området.

Brukeren kan selv velge å søke etter andre områder i verden, og se værmeldingen for disse områdene.
For å igjen se sin egen posisjon, trykkes det på søkefeltet og velger "Min posisjon".

Det er også en time-for-time varsling for det neste døgnet, hvor brukeren kan se lufttemperatur og et vær-ikon.
Det finnes også langtidsvarsel, hvor det står lavest og høyest temperatur for hver dag.

Brukeren kan selv legge til aktiviteter. Brukeren kan velge humør, og appen vil vise aktitiveter som passer både vær og humør.
Appen spør om tilgang til lokasjon fra brukerens enhet, samt tilgang til bilder hvis brukeren ønsker å laste opp bilder til egne aktiviteter.
Innstillinger er lagret i DataStore Preferences, mens aktiviteter og relevante humør og værforhold er lagret i Room, som brukes SQLite.

Appen fungerer på ulike skjermstørrelser, og både i vannrett og loddrett posisjon, men hovedsakelig desgnet rundt en loddrett mobilskjerm.
Brukeren kan selv manuelt velge mellom Dark Mode og Light Mode, og velge å bruke Fahrenheit fremfor Celsius.

Vi har brukt SpatialK for å tenge polygoner i geoJSON, for å finne relevant farevarsel.
Av API-er som er brukt er det det LocationForecast og WeatherAlerts fra MET, og GeoNames.



Alle bildene er hentet fra Unsplash
