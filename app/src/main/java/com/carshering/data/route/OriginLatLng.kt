package com.carshering.data.route

import com.google.android.gms.maps.model.LatLng

object OriginLatLng {

    private var originLatLng: LatLng? = null

    fun saveOriginLatLng(latLng: LatLng) {
        originLatLng = latLng
    }

    fun isExist(): Boolean {
        return originLatLng != null
    }

    fun getOriginLatLng(): LatLng {
        return originLatLng!!
    }
}