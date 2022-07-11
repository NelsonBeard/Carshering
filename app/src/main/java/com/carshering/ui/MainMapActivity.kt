package com.carshering.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.carshering.R
import com.carshering.data.route.OriginLatLng
import com.carshering.data.route.UserLocationQualifier
import com.carshering.databinding.ActivityMainMapBinding
import com.carshering.domain.entity.Car
import com.carshering.domain.entity.CarCardViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.android.ext.android.inject

const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MainMapActivity : AppCompatActivity(), OnMapReadyCallback, Contract.View,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMainMapBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var qualifier: UserLocationQualifier

    private var polyline: Polyline? = null
    private val presenter: Presenter by inject()

    private val mapPadding by lazy { resources.getDimension(R.dimen.map_bottom_padding).toInt() }
    private val boundsPadding by lazy { resources.getDimension(R.dimen.bounds_padding).toInt() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onAttach(this)
        initMap()
        initBottomSheet(binding.carCard)

        qualifier = UserLocationQualifier(
            LocationServices.getFusedLocationProviderClient(this),
            Handler(Looper.getMainLooper())
        )
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initBottomSheet(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        presenter.requestStartPosition()
        presenter.requestCars()
        requestPermission()

        map.setOnMarkerClickListener(this)
    }

    @SuppressLint("MissingPermission")
    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true

            qualifier.qualifyUserLocation {
                OriginLatLng.saveOriginLatLng(it)
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermission()
            } else {
                showErrorToast(R.string.error_missing_permission_toast)
            }
        }
    }

    override fun putMarkers(cars: List<Car>) {
        cars.forEach { car ->
            val latLng = LatLng(car.location.lat, car.location.lng)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
            marker?.tag = car.id
        }
    }

    override fun showErrorToast(errorMessage: Int) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun moveCamera(startPosition: CameraPosition) {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(startPosition))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val carId = marker.tag.toString()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        presenter.onMarkerClicked(carId)
        polyline?.remove()
        return true
    }

    override fun updateBottomSheet(carCardViewModel: CarCardViewModel) {
        val car = carCardViewModel.car

        val remainRangeList =
            listOf(car.remainRange.toString(), resources.getString(R.string.remain_range_measure))

        binding.apply {
            remainRangeTextView.text =
                remainRangeList.joinToString(separator = " ") { it }
            carNameTextView.text = car.model
            seatsTextView.text = car.seats.toString()
            colorTextView.text =
                resources.getString(carCardViewModel.colorRussianTitle)
            carColorContainerImageView.setColorFilter(
                ResourcesCompat.getColor(resources, carCardViewModel.colorCode, null)
            )
            transmissionTextView.text =
                resources.getString(carCardViewModel.transmissionRussianTitle)
        }

        setCarPicture(
            binding.carPictureImageView,
            binding.shimmerImageView,
            binding.shimmerFrameLayout,
            car.picture
        )

        setRegistrationNumber(
            binding.registrationNumberTextView,
            car.registrationNumber
        )

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun showRoute(route: PolylineOptions, bounds: LatLngBounds) {
        polyline = map.addPolyline(route)

        map.setPadding(0, 0, 0, mapPadding)
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, boundsPadding))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach(this)
    }
}
