package com.arvadeveloper.breadstore.config

object ApiConfig {
    // Base URL untuk API
    // Untuk emulator Android Studio gunakan: "http://10.0.2.2:8000/api"
    // Untuk device fisik gunakan IP komputer: "http://192.168.1.100:8000/api"
    private const val BASE_URL = "http://10.0.2.2:8000/api"

    // Endpoints
    const val LOGIN_URL = "$BASE_URL/login"
    const val REGISTER_URL = "$BASE_URL/register"
    const val LOGOUT_URL = "$BASE_URL/logout"

    // Untuk development, Anda bisa menggunakan BuildConfig
    // const val BASE_URL = if (BuildConfig.DEBUG) {
    //     "http://192.168.1.100:8000/api"  // Development
    // } else {
    //     "https://your-production-url.com/api"  // Production
    // }

    // Headers default
    const val CONTENT_TYPE = "application/json"
    const val ACCEPT = "application/json"

    // Timeout settings
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}