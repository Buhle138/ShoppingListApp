package com.example.myshoppinglistapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
//when a user clicks on a screen we want the lat and log of that place
//So we add the lat and log to the parameter of this function.
fun LocationSelectionScreen(

    location: LocationData, //getting the location
    onLocationSelected: (LocationData) -> Unit){ //what to do with that location when we have found it

    //user location might change which is why we need to manage their state.
    //this method has the current location of the user because mutables give us the current state
    val userLocation = remember{mutableStateOf(LatLng(location.latitude, location.longitude))}

    //variable below we want to know where exactly are we on the google maps selection screen.
    //We want to see where the user is at certain zoom state
    var cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(userLocation.value, 10f)
    }

    //Using  a column because we want to have the google map and underneath we want the button
    Column(modifier = Modifier.fillMaxSize()){
        GoogleMap(modifier = Modifier.weight(1f).padding(top=16.dp),
            cameraPositionState = cameraPositionState,
            //BIG NOTE: ONCE THE USER HAS CLICKED ON THE MAP THE STATE HAS CHANGED ALREADY BEFORE WE COULD EVEN CLICK ON THE GET LOCATION BUTTON.
            onMapClick = {userLocation.value = it} //whatever the user clicked on will be the new user location from the mutable variable above.
            ) {

            //it's the red dot showing the user location
            //Basically the code below is about where should our marker be. In this case it should be wherever the user has decided to point to.
            Marker(state = MarkerState(position = userLocation.value))
        }


        //Once we click on the get location below we want to keep track and show the new current location
        var newLocation: LocationData
        
        Button(onClick = { newLocation = LocationData(userLocation.value.latitude, userLocation.value.longitude)
                onLocationSelected(newLocation) //what to do with the new location.
        }) {
            Text("Set Location")
        }
    }

}

