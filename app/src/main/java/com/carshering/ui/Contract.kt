package com.carshering.ui

import com.carshering.domain.entity.Car
import com.google.android.gms.maps.GoogleMap

class Contract {

    interface View {
        fun initMap()
        fun putMarks(cars: List<Car>)
        fun onMapReady(googleMap: GoogleMap)
    }

    interface Presenter {
        fun getCars(): List<Car>
    }
}