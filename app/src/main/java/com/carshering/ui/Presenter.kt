package com.carshering.ui

import com.carshering.data.JsonConverterDTO
import com.carshering.data.ServerRequestDAO
import com.carshering.domain.entity.Car
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Presenter {

    fun initMarkers(googleMap: GoogleMap) {
        val cars = mutableListOf<Car>()
        ServerRequestDAO().getJsonArray {
            for (i in 0 until it.length()) {
                val car = JsonConverterDTO().fromJsonToCar(it.getJSONObject(i))
                cars.add(car)
            }
            addMarker(cars, googleMap)
        }
    }

    private fun addMarker(cars: List<Car>, googleMap: GoogleMap) {
        cars.forEach {
            val latLng = LatLng(it.lat, it.lng)
            val carModel = it.model

            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(carModel)
            )
        }
    }
}
