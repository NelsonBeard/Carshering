package com.carshering.ui

import android.os.Handler
import android.os.Looper
import com.carshering.R
import com.carshering.data.HttpClient
import com.carshering.data.StartPositionManual
import com.carshering.data.cars.*
import com.carshering.data.route.OriginLatLng
import com.carshering.data.route.RouteDaoImpl
import com.carshering.domain.entity.CarCardViewModel
import com.carshering.domain.usecase.cars.CarDAO
import com.carshering.domain.usecase.route.RouteDAO
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.Executors

class Presenter : Contract.Presenter {

    private var view: Contract.View? = null
    private val carDAO: CarDAO =
        CarDAOImpl(
            CarsLocalRepository,
            Executors.newSingleThreadExecutor(),
            Handler(Looper.getMainLooper()),
            HttpClient()
        )
    private val routeDAO: RouteDAO =
        RouteDaoImpl(
            HttpClient(),
            Executors.newSingleThreadExecutor(),
            Handler(Looper.getMainLooper())
        )

    override fun onAttach(view: Contract.View) {
        this.view = view
    }

    override fun requestCars() {
        carDAO.getAllCarsFromServer(
            {
                view?.putMarkers(it)
            },
            {
                view?.showErrorToast(errorMessage = R.string.error_cant_get_data_toast)
            }
        )
    }

    override fun requestStartPosition() {
        val startPosition = StartPositionManual().getStartPosition()
        view?.moveCamera(startPosition)
    }

    override fun onMarkerClicked(clickedCarId: String) {

        val clickedCar = carDAO.getSingleCarFromLocalRepo(clickedCarId)

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
            val destinationLatLng = LatLng(clickedCar.lat, clickedCar.lng)
            requestRoute(destinationLatLng)
            clickedCar.let { view?.updateBottomSheet(bottomSheetViewModel) }
        }
    }

    override fun requestRoute(destinationLatLngGoogleMap: LatLng) {
        if (OriginLatLng.isExist()) {
            val originLatLngGoogleMap = OriginLatLng.getOriginLatLng()
            routeDAO.getRoute(
                originLatLngGoogleMap,
                destinationLatLngGoogleMap,

                {
                    view?.showRoute(it.first, it.second)
                },
                {
                    view?.showErrorToast(errorMessage = R.string.error_cant_get_route_toast)
                }
            )
        }
    }

    override fun onDetach(view: Contract.View) {
        this.view = null
    }
}
