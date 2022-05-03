package com.carshering.domain.usecase

import com.google.android.gms.maps.model.CameraPosition

interface StartPositionGetter {
    fun getStartPosition(): CameraPosition
}