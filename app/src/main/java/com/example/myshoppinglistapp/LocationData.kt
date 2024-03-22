package com.example.myshoppinglistapp

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

data class GeocodingResponse(
    val results: List<GeocodingResponse>,
    val status: String
)

data class GeocodingResult(
    val formatted_address: String
)