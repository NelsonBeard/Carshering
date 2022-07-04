package com.carshering.data.route

import android.os.Handler
import com.carshering.data.HttpClient
import com.carshering.domain.usecase.route.RouteDAO
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import java.util.concurrent.Executor

private const val ACCESS_TOKEN =
    ""

class RouteDaoImpl(
    private val httpClient: HttpClient,
    private val executor: Executor,
    private val handler: Handler,
    private val latLngAdapter: GoogleMapToMapboxLatLngAdapter
) : RouteDAO {

    override fun getRoute(
        originLatLngGoogle: LatLng?, // обязательный аргумент
        destinationLatLngGoogle: LatLng?, // обязательный аргумент
        onSuccess: (Pair<PolylineOptions, LatLngBounds>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val originLatLngMapbox = latLngAdapter.swapLatAndLng(originLatLngGoogle)
        val destinationLatLngMapbox = latLngAdapter.swapLatAndLng(destinationLatLngGoogle)
        val routeUrl =
            "https://api.mapbox.com/directions/v5/mapbox/walking/$originLatLngMapbox;$destinationLatLngMapbox?access_token=$ACCESS_TOKEN"

        executor.execute {
            try {
                val serverResponseData = httpClient.get(routeUrl)
                // тут адаптер создается локально в методе, а latLngAdapter создается полем
                val route = JsonToPolylineOptionsAdapter(serverResponseData).fromJson()

                handler.post { onSuccess(route) }
            } catch (error: Exception) {
                error.printStackTrace()
                handler.post { onError(Exception()) }
            }
        }
    }
}