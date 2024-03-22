package com.example.myshoppinglistapp

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApiService {

    @GET("maps/api/geocode/json")
    suspend fun getAddressFromCoordinates(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): GeocodingResponse //return type of this method.
}