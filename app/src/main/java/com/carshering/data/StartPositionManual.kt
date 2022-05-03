package com.carshering.data

import com.carshering.domain.entity.StartPosition
import com.carshering.domain.usecase.StartPositionGetter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

private val LAT_LNG_UFA = LatLng(54.74390, 56.04680)
private const val ZOOM = 11f

class StartPositionManual : StartPositionGetter {
    private val startPosition = StartPosition(LAT_LNG_UFA, ZOOM)

    override fun getStartPosition(): CameraPosition {
        return CameraPosition.fromLatLngZoom(startPosition.latLng, startPosition.zoom)
    }
}