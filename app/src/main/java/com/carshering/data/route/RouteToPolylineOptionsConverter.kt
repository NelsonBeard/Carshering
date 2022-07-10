package com.carshering.data.route

import com.carshering.R
import com.carshering.domain.entity.Route
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil

fun convertRouteToPolyline(
    serverResponseData: Route,
    line: PolylineOptions
): Pair<PolylineOptions, LatLngBounds> {
    val routePoints = PolyUtil.decode(serverResponseData.geometry)
    val latLngBuilder = LatLngBounds.Builder()

    line.apply {
        color(R.color.route_color)
        routePoints.forEach {
            add(it)
            latLngBuilder.include(it)
        }
    }

    val latLngBounds = latLngBuilder.build()
    return Pair(line, latLngBounds)
}