package com.carshering.domain.entity

import com.google.android.gms.maps.model.LatLng

data class City(
    var latLng: LatLng,
    var zoom: Float
)
