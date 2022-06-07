package com.carshering.domain.usecase.route

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

interface RouteDAO {
    fun getRoute(
        originLatLng: LatLng?,
        destinationLatLng: LatLng?,
        onSuccess: (PolylineOptions) -> Unit,
        onError: (Exception) -> Unit
    )
}