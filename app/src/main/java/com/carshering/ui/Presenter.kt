package com.carshering.ui

import android.os.Handler
import android.os.Looper
import com.carshering.data.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import java.util.concurrent.Executors

class Presenter : Contract.Presenter {

    private var view: Contract.View? = null
    private val carDAOImpl =
        CarDAOImpl(
            Executors.newSingleThreadExecutor(),
            Handler(Looper.getMainLooper()),
            HttpClient()
        )

    override fun onAttach(view: Contract.View) {
        this.view = view
    }

    override fun requestCars() {
        carDAOImpl.getAllCars(
            {
                view?.putMarkers(it)
            },
            {
                view?.showErrorToast()
            }
        )
    }

    override fun requestStartPosition() {
        val startPosition = StartPositionManual().getStartPosition()
        view?.moveCamera(startPosition)
    }

    override fun onMarkerClicked(marker: Marker) {
        val markerLatLng = marker.position
        carDAOImpl.getAllCars(
            {
                it.forEach { car ->
                    val carLatLng = LatLng(car.lat, car.lng)
                    if (markerLatLng == carLatLng) {
                        view?.updateBottomSheetBehavior(car)
                    }
                }
            },
            {
                //Nothing to do
            }
        )
    }

    override fun translateCarColor(colorENUM: String) {
        val colorRussian = colorsMap.getOrDefault(colorENUM, " ")
        view?.setCarColor(colorRussian)
    }

    override fun translateCarTransmission(transmissionENUM: String) {
        val transmissionRussian = transmissionsMap.getOrDefault(transmissionENUM, " ")
        view?.setCarTransmission(transmissionRussian)
    }
}
