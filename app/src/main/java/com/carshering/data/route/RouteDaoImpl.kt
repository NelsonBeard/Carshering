package com.carshering.data.route

import com.carshering.R
import com.carshering.StoreGraph
import com.carshering.domain.usecase.route.RouteDAO
import com.dropbox.android.external.store4.get
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val ACCESS_TOKEN =
    "pk.eyJ1IjoiYmVsa2FjYXIiLCJhIjoiY2w0NDhoZGN4MWRyeDNpcXRzeXVlYm8ybSJ9.tUK_nkBXhe7eXF8dld3BjA"

class RouteDaoImpl(
    private val store: StoreGraph,
    private val scope: CoroutineScope
) : RouteDAO {

    private val latLngAdapter: GoogleMapToMapboxLatLngAdapter = GoogleMapToMapboxLatLngAdapter()

    override fun getRoute(
        originLatLngGoogle: LatLng,
        destinationLatLngGoogle: LatLng,
        onSuccess: (Pair<PolylineOptions, LatLngBounds>) -> Unit,
        onError: (Int) -> Unit
    ) {
        val originLatLngMapbox = latLngAdapter.swapLatAndLng(originLatLngGoogle)
        val destinationLatLngMapbox = latLngAdapter.swapLatAndLng(destinationLatLngGoogle)
        val routeUrl =
            "https://api.mapbox.com/directions/v5/mapbox/walking/$originLatLngMapbox;$destinationLatLngMapbox?access_token=$ACCESS_TOKEN"

        scope.launch {
            try {
                val routesDataFromServer = store.provideRoutesStore().get(routeUrl)
                val route =
                    convertRouteToPolyline(routesDataFromServer.routes[0], PolylineOptions())
                onSuccess(route)
            } catch (error: Exception) {
                onError(R.string.error_cant_get_route_toast)
            }
        }
    }
}