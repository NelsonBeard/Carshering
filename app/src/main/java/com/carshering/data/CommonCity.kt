package com.carshering.data

import com.carshering.domain.entity.City
import com.google.android.gms.maps.model.LatLng

private val UFA = City(LatLng(54.74390, 56.04680), 11f)
private val MOSCOW= City(LatLng(55.751244, 37.618423), 10f)

class CommonCity {
    var city = UFA

    fun setCity(): City {
        val latLng = city.latLng
        val zoom = city.zoom
        return City(latLng, zoom)
    }

}