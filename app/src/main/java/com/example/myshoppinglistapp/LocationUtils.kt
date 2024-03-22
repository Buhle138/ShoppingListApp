package com.example.myshoppinglistapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper

import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale
    class LocationUtils (val context: Context){

        //variable below will allow us to be able to get the latitude and longitude of the user
        //Now we need to say what it should look like
        private val _fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


        @SuppressLint("MissingPermission")
        //below we are creating the function that will give us the location
        fun requestLocationUpdates(viewModel: LocationViewModel){
            val locationCallback = object : LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult.lastLocation?.let{
                        val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                        viewModel.updateLocation(location) //Updating the user location using the method we created on the LocationViewModel.
                    }
                }
            }

            //Code below updates the users location in one second each time they change their location.
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

            //The code below needs gives errors because it needs a permission to run so we'll use a suppressor above to make it run without a permission
            _fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        //This function will help us see if the user has granted us acesss to their locations or not.
        fun hasLocationPermission(context: Context) : Boolean{
            return ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    &&
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }


        /*When the location is updated using the method that requestLocationUpdates() above
        the LocationData object is updated so therefore we now use it to get the address of the user.
        */

        fun reverseGeocodeLocation(location: LocationData) : String{
            val geocoder = Geocoder(context, Locale.getDefault())
            val coordinate = LatLng(location.latitude, location.longitude)
            val addresses:MutableList<Address>? = geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
            return if(addresses?.isNotEmpty() == true){
                addresses[0].getAddressLine(0)
            }else{
                "Address not found"
            }
        }

    }
