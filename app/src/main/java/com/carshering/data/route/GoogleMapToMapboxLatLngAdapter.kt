package com.carshering.data.route

import com.google.android.gms.maps.model.LatLng

class GoogleMapToMapboxLatLngAdapter {
    fun swapLatAndLng(latLngToSwap: LatLng?): String {
        return latLngToSwap?.longitude.toString() + "," + latLngToSwap?.latitude.toString()
    }
}