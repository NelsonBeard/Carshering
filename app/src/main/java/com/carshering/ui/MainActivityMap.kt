package com.carshering.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carshering.R
import com.carshering.data.StartPositionManual
import com.carshering.databinding.ActivityMainMapBinding
import com.carshering.domain.entity.Car
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivityMap : AppCompatActivity(), OnMapReadyCallback, Contract.View {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMainMapBinding
    private val presenter = Presenter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()
    }

    override fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val startPosition = StartPositionManual().getStartPosition()
        map = googleMap
        map.moveCamera(CameraUpdateFactory.newCameraPosition(startPosition))

        val cars = presenter.getCars()
        putMarks(cars)
    }

    override fun putMarks(cars: List<Car>) {
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
}