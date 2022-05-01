package com.carshering.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carshering.R
import com.carshering.data.CommonCity
import com.carshering.databinding.ActivityMainMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MainActivityMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMainMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val (latLng, zoom) = CommonCity().setCity()

        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }
}