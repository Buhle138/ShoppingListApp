package com.example.myshoppinglistapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {

    //Whenever we change the state at the locationSelectionScreen mutable private state variable below  will be updated.
    private val _location= mutableStateOf<LocationData?>(null)
    val location : State<LocationData?> = _location

    fun updateLocation(newLocation: LocationData){
        _location.value = newLocation
    }
}