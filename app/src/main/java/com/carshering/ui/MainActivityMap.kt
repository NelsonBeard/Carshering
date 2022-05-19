package com.carshering.ui

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carshering.R
import com.carshering.databinding.ActivityMainMapBinding
import com.carshering.domain.entity.Car
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivityMap : AppCompatActivity(), OnMapReadyCallback, Contract.View,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMainMapBinding
    private lateinit var carInfo: BottomSheetBehavior<RelativeLayout>
    private val presenter = Presenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onAttach(this)
        initMap()
        initBottomSheet(findViewById(R.id.car_info))
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        presenter.requestStartPosition()
        presenter.requestCars()

        map.setOnMarkerClickListener(this)
    }

    override fun putMarkers(cars: List<Car>) {
        cars.forEach {
            val latLng = LatLng(it.lat, it.lng)
            val carModel = it.model

            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(carModel)
            )
        }
    }

    override fun showErrorToast() {
        Toast.makeText(this, R.string.error_toast, Toast.LENGTH_SHORT).show()
    }

    override fun moveCamera(startPosition: CameraPosition) {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(startPosition))
    }

    override fun initBottomSheet(bottomSheet: RelativeLayout) {
        carInfo = BottomSheetBehavior.from(bottomSheet)
        carInfo.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        carInfo.state = BottomSheetBehavior.STATE_HIDDEN
        presenter.onMarkerClicked(marker)
        return true
    }

    override fun updateBottomSheetBehavior(car: Car) {
        carInfo.state = BottomSheetBehavior.STATE_EXPANDED
        findViewById<TextView>(R.id.car_name_text_view).text = car.model
        findViewById<TextView>(R.id.seats_text_view).text = car.seats.toString()
        findViewById<TextView>(R.id.remain_range_text_view).text =
            car.remainRange.toString() + " км"

        setCarPicture(
            findViewById(R.id.car_picture_image_view),
            findViewById(R.id.shimmer_image_view),
            findViewById(R.id.shimmer_frame_layout),
            car.picture
        )

        setRegistrationNumber(
            findViewById(R.id.registration_number_text_view),
            car.registrationNumber
        )

        presenter.translateCarColor(car.color)
        presenter.translateCarTransmission(car.transmission)
    }

    override fun setCarColor(colorRussian: String) {
        findViewById<TextView>(R.id.color_text_view).text = colorRussian
    }

    override fun setCarTransmission(transmissionRussian: String) {
        findViewById<TextView>(R.id.transmission_text_view).text = transmissionRussian
    }
}