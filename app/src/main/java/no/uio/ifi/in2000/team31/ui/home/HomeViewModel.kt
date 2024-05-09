package no.uio.ifi.in2000.team31.ui.home

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MyApplication
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.model.GeonameData
import no.uio.ifi.in2000.team31.model.WeatherDataModel
import java.net.URLEncoder

data class WeatherDataUIState(
    val weatherData: WeatherDataModel? = null,
    val tempAndTimeData: List<Triple<String?, Double?, String?>> = listOf(),
    val longTermForecast: Map<String, Pair<Double, Double>>? = null,
    val alertIconData: List<Pair<String?,String?>> = listOf()
)

data class SearchUiState(
    val currentQuery: String = "",
    val places: List<GeonameData> = emptyList(),
    val selectedPlace: GeonameData? = null,
    val isSearching: Boolean = false
)

@OptIn(FlowPreview::class)
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val appContainer = (application as MyApplication).appContainer
    private val repository = appContainer.locWeatherRepository
    private val alertRepository = appContainer.alertRepository
    private val geonameRepository = appContainer.geonameRepository

    private val _weatherDataUIState = MutableStateFlow(WeatherDataUIState())
    val weatherDataUIState: StateFlow<WeatherDataUIState> = _weatherDataUIState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    private val _places = MutableStateFlow<List<GeonameData>>(emptyList())
    private val _selectedPlace = MutableStateFlow<GeonameData?>(null)
    private val _isSearching = MutableStateFlow(false)

    val searchUiState = combine(
        _searchText,
        _places,
        _selectedPlace,
        _isSearching
    ) { searchText, places, selectedPlace, isSearching ->

        if (searchText.isBlank())
            clearSearch()

        SearchUiState(
            searchText,
            places,
            selectedPlace,
            isSearching
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchUiState()
    )

    init {
        _searchText
            .debounce(300) // gets the latest; no need for delays!
            .filter { currentQuery -> (currentQuery.length > 1)} // make sure there's enough initial text to search for
            .distinctUntilChanged() // to avoid duplicate network calls
            .onEach { currentQuery -> // just gets the prefix: 'ph', 'pho', 'phoe'
                val encQuery = URLEncoder.encode(currentQuery,"UTF-8")
                getCityNames(encQuery)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            _searchText.value = ""
        }
    }
    fun onPlaceNameSearch(currentQuery: String) {
        _searchText.value = currentQuery
    }

    private fun getCityNames(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _places.value = geonameRepository.getPlaceRecommendations(query).geonames
        }
    }

    private fun clearSearch() {
        _places.value = emptyList()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val features = alertRepository.getDangerZonesOf(point(lon,lat), CachePolicy(CachePolicy.Type.REFRESH))
                val alertIcons = mutableListOf<Pair<String?,String?>>()
                features?.forEach {feature ->
                    val event = feature.getStringProperty("event")
                    val color = feature.getStringProperty("riskMatrixColor")
                    alertIcons.add(Pair(event,color))
                }
                _weatherDataUIState.update { currentState ->
                    currentState.copy(
                        weatherData = repository.fetchInfo(lat, lon, CachePolicy(CachePolicy.Type.REFRESH)),
                        tempAndTimeData = repository.get24HoursForecast(lat, lon),
                        longTermForecast = repository.getNext7Days(lat,lon),
                        alertIconData = alertIcons
                    )
                }

            } catch (e: Exception) {
                Log.e("testing", "Error fetching weather data", e)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onPlaceSelected(place: GeonameData) {
        onToogleSearch()
        viewModelScope.launch {
            _selectedPlace.value = place
        }
        fetchWeatherData(_selectedPlace.value?.lat!!,_selectedPlace.value?.lon!!)
    }
}