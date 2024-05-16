package no.uio.ifi.in2000.team31.data.cachePolicy

/**
 * Custom data class that defines the way API request are made
 * The class is passed as a parameter to the methods that run the API requests.
 * There are three types of cache policies one can choose from, but more can be added if needed:
 * NEVER - never cache data
 * ALWAYS - fetch data from cache if possible
 * REFRESH - fetch remotely and cache the data
 * */

data class CachePolicy(
    val type: Type? = Type.ALWAYS,
) {
    enum class Type {
        NEVER, // never cache data
        ALWAYS, // fetch data from cache if possible
        REFRESH // fetch remotely and cache the data
    }
}