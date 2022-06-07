package com.carshering.data.route

import android.os.Handler
import com.carshering.data.HttpClient
import com.carshering.domain.usecase.route.RouteDAO
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import java.util.concurrent.Executor

private const val ACCESS_TOKEN =
    "pk.eyJ1IjoiYmVsa2FjYXIiLCJhIjoiY2w0NDhoZGN4MWRyeDNpcXRzeXVlYm8ybSJ9.tUK_nkBXhe7eXF8dld3BjA"

class RouteDaoImpl(
    private val httpClient: HttpClient,
    private val executor: Executor,
    private val handler: Handler,
    private val latLngAdapter: GoogleMapToMapboxLatLngAdapter
) : RouteDAO {

    private lateinit var routePoints: MutableList<LatLng>

    override fun getRoute(
        originLatLngGoogle: LatLng?,
        destinationLatLngGoogle: LatLng?,
        onSuccess: (PolylineOptions) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val originLatLngMapbox = latLngAdapter.swapLatAndLng(originLatLngGoogle)
        val destinationLatLngMapbox = latLngAdapter.swapLatAndLng(destinationLatLngGoogle)

        val routeUrl =
            "https://api.mapbox.com/directions/v5/mapbox/walking/$originLatLngMapbox;$destinationLatLngMapbox?access_token=$ACCESS_TOKEN"
        println(routeUrl)
        executor.execute {
            try {
                val serverResponseData = httpClient.get(routeUrl)
                println(serverResponseData)
                handler.post {}

            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }
}