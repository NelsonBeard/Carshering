package com.carshering.ui

import android.widget.RelativeLayout
import com.carshering.domain.entity.Car
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker

class Contract {

    interface View {
        fun putMarkers(cars: List<Car>)
        fun showErrorToast()
        fun moveCamera(startPosition: CameraPosition)
        fun initBottomSheet(bottomSheet: RelativeLayout)
        fun updateBottomSheetBehavior(car: Car)
        fun setCarColorField(colorRussianTitle: String, colorCode: Int)
        fun setCarTransmission(transmissionRussianTitle: String)
    }

    interface Presenter {
        fun onAttach(view: View)
        fun requestCars()
        fun requestStartPosition()
        fun onMarkerClicked(marker: Marker)
        fun fromEnumToColor(colorENUM: String)
        fun fromEnumToTransmission(transmissionENUM: String)
    }
}