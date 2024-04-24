package no.uio.ifi.in2000.team31.cache

data class CachePolicy (
    val type: Type? = Type.ALWAYS,
) {
    enum class Type {
        NEVER, // never cache data
        ALWAYS, // fetch data from cache if possible
        REFRESH // fetch remotely and cache the data
    }
}