package com.carshering.ui

import com.carshering.domain.entity.Car
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

class Contract {

    interface View {
        fun putMarkers(cars: List<Car>)
        fun showErrorToast()
        fun moveCamera(startPosition: CameraPosition)
        fun updateBottomSheet(car: Car)
        fun showRoute(latLng: LatLng?)
        fun setCarColorField(colorRussianTitle: Int, colorCode: Int)
        fun setCarTransmission(transmissionRussianTitle: Int)
    }

    interface Presenter {
        fun onAttach(view: View)
        fun requestCars()
        fun requestStartPosition()
        fun onMarkerClicked(clickedCarId: String)
        fun fromEnumToColor(colorENUM: String)
        fun fromEnumToTransmission(transmissionENUM: String)
        fun onDetach(view: View)
    }
}