package com.carshering.data.route

import com.carshering.R
import com.carshering.domain.entity.RoutesFromServer
import com.carshering.data.RetrofitApi
import com.carshering.domain.usecase.route.RouteDAO
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ACCESS_TOKEN =
    "pk.eyJ1IjoiYmVsa2FjYXIiLCJhIjoiY2w0NDhoZGN4MWRyeDNpcXRzeXVlYm8ybSJ9.tUK_nkBXhe7eXF8dld3BjA"

class RouteDaoImpl(
    private val retrofitClient: RetrofitApi
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

        retrofitClient.getRoute(routeUrl)
            .enqueue(object : Callback<RoutesFromServer> {
                override fun onResponse(
                    call: Call<RoutesFromServer>,
                    response: Response<RoutesFromServer>
                ) {
                    if (response.isSuccessful) {
                        val route =
                            convertRouteToPolyline(response.body()!!.routes[0], PolylineOptions())
                        onSuccess(route)
                    } else {
                        Exception().printStackTrace()
                        onError(R.string.error_cant_get_route_toast)
                    }
                }

                override fun onFailure(call: Call<RoutesFromServer>, t: Throwable) {
                    onError(R.string.error_no_internet_connection_toast)
                }
            })
    }
}