package com.carshering.ui

import com.carshering.R
import com.carshering.StoreGraph
import com.carshering.data.StartPositionManual
import com.carshering.data.cars.CarDAOImpl
import com.carshering.data.cars.colorsCodeMap
import com.carshering.data.cars.colorsRussianTitleMap
import com.carshering.data.cars.transmissionsMap
import com.carshering.data.route.OriginLatLng
import com.carshering.data.route.RouteDaoImpl
import com.carshering.domain.entity.CarCardViewModel
import com.carshering.domain.usecase.cars.CarDAO
import com.carshering.domain.usecase.route.RouteDAO
import com.google.android.gms.maps.model.LatLng

class Presenter : Contract.Presenter {
    private val store = StoreGraph
    private var view: Contract.View? = null

    private val carDAO: CarDAO = CarDAOImpl(store)
    private val routeDAO: RouteDAO = RouteDaoImpl(store)

    override fun onAttach(view: Contract.View) {
        this.view = view
    }

    override suspend fun requestCars() {
        carDAO.getAllCars(
            {
                view?.putMarkers(it)
            },
            {
                view?.showErrorToast(errorMessage = it)
            }
        )
    }

    override fun requestStartPosition() {
        val startPosition = StartPositionManual().getStartPosition()
        view?.moveCamera(startPosition)
    }

    override suspend fun onMarkerClicked(clickedCarId: String) {

        val clickedCar = carDAO.getSingleCar(clickedCarId)

        if (clickedCar != null) {
            val bottomSheetViewModel = CarCardViewModel(
                car = clickedCar,
                colorRussianTitle = colorsRussianTitleMap.getOrDefault(
                    clickedCar.color,
                    R.string.empty_color
                ),
                colorCode = colorsCodeMap.getOrDefault(clickedCar.color, R.color.empty_color),
                transmissionRussianTitle = transmissionsMap.getOrDefault(
                    clickedCar.transmission,
                    R.string.empty_transmission
                )
            )

            val destinationLatLng = LatLng(clickedCar.location.lat, clickedCar.location.lng)
            requestRoute(destinationLatLng)
            clickedCar.let { view?.updateBottomSheet(bottomSheetViewModel) }
        }
    }

    override suspend fun requestRoute(destinationLatLngGoogleMap: LatLng) {
        if (OriginLatLng.isExist()) {
            val originLatLngGoogleMap = OriginLatLng.getOriginLatLng()

            routeDAO.getRoute(
                originLatLngGoogleMap,
                destinationLatLngGoogleMap,
                {
                    view?.showRoute(it.first, it.second)
                },
                {
                    view?.showErrorToast(errorMessage = it)
                }
            )
        }
    }

    override fun onDetach(view: Contract.View) {
        this.view = null
    }
}
