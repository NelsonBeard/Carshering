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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class Presenter : Contract.Presenter {
    private val store = StoreGraph
    private var view: Contract.View? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val carDAO: CarDAO = CarDAOImpl(store)
    private val routeDAO: RouteDAO = RouteDaoImpl(store)

    override fun onAttach(view: Contract.View) {
        this.view = view
    }

    override fun requestCars() {
        scope.launch {
            carDAO.getAllCars(
                {
                    view?.putMarkers(it)
                },
                {
                    view?.showErrorToast(errorMessage = it)
                }
            )
        }
    }

    override fun requestStartPosition() {
        val startPosition = StartPositionManual().getStartPosition()
        view?.moveCamera(startPosition)
    }

    override fun onMarkerClicked(clickedCarId: String) {
        scope.launch {
            carDAO.getSingleCar(clickedCarId) {
                if (it != null) {
                    val bottomSheetViewModel =
                        CarCardViewModel(
                            car = it,
                            colorRussianTitle = colorsRussianTitleMap.getOrDefault(
                                it.color,
                                R.string.empty_color
                            ),
                            colorCode = colorsCodeMap.getOrDefault(it.color, R.color.empty_color),
                            transmissionRussianTitle = transmissionsMap.getOrDefault(
                                it.transmission,
                                R.string.empty_transmission
                            )
                        )

                    val destinationLatLng = LatLng(it.location.lat, it.location.lng)
                    requestRoute(destinationLatLng)
                    it.let { view?.updateBottomSheet(bottomSheetViewModel) }
                }
            }
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
                    view?.showErrorToast(errorMessage = it)
                }
            )
        }
    }

    override fun onDetach(view: Contract.View) {
        this.view = null
        scope.cancel()
    }
}
