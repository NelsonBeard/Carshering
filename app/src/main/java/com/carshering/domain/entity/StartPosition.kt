package com.carshering.domain.entity

import com.google.android.gms.maps.model.LatLng

data class StartPosition(
    val latLng: LatLng,
    val zoom: Float
)
