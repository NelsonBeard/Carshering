package com.carshering.domain.usecase.route

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions

interface RouteDAO {
    fun getRoute(
        originLatLng: LatLng?, // параметр должен быть обязательным
        destinationLatLng: LatLng?, // параметр должен быть обязательным
        onSuccess: (Pair<PolylineOptions, LatLngBounds>) -> Unit,
        onError: (Exception) -> Unit
    )
}