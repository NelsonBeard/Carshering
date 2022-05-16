package com.carshering.ui

import android.os.Handler
import android.os.Looper
import android.widget.RelativeLayout
import com.carshering.R
import com.carshering.data.CarDAOImpl
import com.carshering.data.HttpClient
import com.carshering.data.StartPositionManual
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
}
