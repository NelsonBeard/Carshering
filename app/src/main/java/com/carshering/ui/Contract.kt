package com.carshering.ui

import com.carshering.domain.entity.Car
import com.google.android.gms.maps.model.CameraPosition

class Contract {

    interface View {
        fun putMarkers(cars: List<Car>)
        fun showErrorToast()
        fun moveCamera(startPosition: CameraPosition)
    }

    interface Presenter {
        fun onAttach(view: View)
        fun requestCars()
        fun requestStartPosition()
    }
}