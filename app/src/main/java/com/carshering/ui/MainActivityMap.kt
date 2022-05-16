package com.carshering.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
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
import com.google.android.material.chip.Chip

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
        presenter.onMarkerClicked(marker)
        return true
    }

    override fun updateBottomSheetBehavior(car: Car) {
        findViewById<TextView>(R.id.car_name).text = car.model
        findViewById<Chip>(R.id.seats_chip).text = car.seats.toString()
        findViewById<Chip>(R.id.remain_range_chip).text = car.remainRange.toString()
        findViewById<Chip>(R.id.color_chip).text = car.color
        findViewById<Chip>(R.id.transmission_chip).text = car.transmission
        findViewById<Chip>(R.id.registration_number_chip).text = car.registrationNumber
        findViewById<ImageView>(R.id.car_picture_image_view).load(car.picture)
        carInfo.state = BottomSheetBehavior.STATE_EXPANDED
    }
}