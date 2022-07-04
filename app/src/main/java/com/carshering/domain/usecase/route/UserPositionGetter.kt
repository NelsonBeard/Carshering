package com.carshering.domain.usecase.route

import com.google.android.gms.maps.model.LatLng

interface UserPositionGetter {
    fun qualifyUserLocation(
        onSuccess: (LatLng) -> Unit
    )
}