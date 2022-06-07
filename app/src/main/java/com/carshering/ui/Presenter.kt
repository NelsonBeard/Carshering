package com.carshering.ui

import android.os.Handler
import android.os.Looper
import com.carshering.R
import com.carshering.data.HttpClient
import com.carshering.data.StartPositionManual
import com.carshering.data.cars.*
import com.carshering.data.route.OriginLatLng
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.Executors

class Presenter : Contract.Presenter {

    private var view: Contract.View? = null
    private val carDAOImpl =
        CarDAOImpl(
            CarsLocalRepository,
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

    override fun onMarkerClicked(clickedCarId: String) {
        val savedCars = CarsLocalRepository.getCars()
        val clickedCar = savedCars?.firstOrNull {
            clickedCarId == it.id
        }

        val originLatLng = OriginLatLng.getOriginLatLng()
        val destinationLatLng = LatLng(clickedCar!!.lat, clickedCar.lng)

        view?.showRoute(destinationLatLng)
        clickedCar?.let { view?.updateBottomSheet(it) }
    }

    override fun fromEnumToColor(colorENUM: String) {
        val colorRussianTitle = colorsRussianTitleMap.getOrDefault(colorENUM, R.string.empty_color)
        val colorCode = colorsCodeMap.getOrDefault(colorENUM, R.color.empty_color)
        view?.setCarColorField(colorRussianTitle, colorCode)
    }

    override fun fromEnumToTransmission(transmissionENUM: String) {
        val transmissionRussianTitle =
            transmissionsMap.getOrDefault(transmissionENUM, R.string.empty_transmission)
        view?.setCarTransmission(transmissionRussianTitle)
    }

    override fun onDetach(view: Contract.View) {
        this.view = null
    }
}
