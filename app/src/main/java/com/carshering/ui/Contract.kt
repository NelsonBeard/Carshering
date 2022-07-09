package com.carshering.ui

import com.carshering.domain.entity.CarCardViewModel
import com.carshering.domain.entity.Car
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions

class Contract {

    interface View {
        fun putMarkers(cars: List<Car>)
        fun showErrorToast(errorMessage: Int)
        fun moveCamera(startPosition: CameraPosition)
        fun updateBottomSheet(carCardViewModel: CarCardViewModel)
        fun showRoute(route: PolylineOptions, bounds: LatLngBounds)
    }

    interface Presenter {
        fun onAttach(view: View)
        suspend fun requestCars()
        fun requestStartPosition()
        suspend fun requestRoute(destinationLatLng: LatLng)
        suspend fun onMarkerClicked(clickedCarId: String)
        fun onDetach(view: View)
    }
}