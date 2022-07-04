package com.carshering.data.route

import android.annotation.SuppressLint
import android.os.Handler
import com.carshering.domain.usecase.route.UserPositionGetter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng

class UserLocationQualifier(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val handler: Handler
) : UserPositionGetter {

    @SuppressLint("MissingPermission")
    override fun qualifyUserLocation(
        onSuccess: (LatLng) -> Unit
    ) {
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.result != null) {
                val originLatLng = LatLng(it.result.latitude, it.result.longitude)
                handler.post {
                    onSuccess(originLatLng)
                }
            }
        }
    }
}





