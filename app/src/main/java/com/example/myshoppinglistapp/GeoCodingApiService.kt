package com.example.myshoppinglistapp

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApiService {

    @GET("https://maps.googleapis.com/maps/api/geocode/json?latlng/")
    suspend fun getAddressFromCoordinates(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GeocodingResponse //return type of this method.
}