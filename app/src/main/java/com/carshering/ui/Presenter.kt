package com.carshering.ui

import android.os.Handler
import android.os.Looper
import com.carshering.R
import com.carshering.data.*
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

    override fun onMarkerClicked(clickedCarId: String) {

        val clickedCar = carDAOImpl.cars.firstOrNull {
            clickedCarId == it.id
        }
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
