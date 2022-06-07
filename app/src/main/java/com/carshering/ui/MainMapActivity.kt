package com.carshering.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.carshering.R
import com.carshering.data.route.OriginLatLng
import com.carshering.data.route.UserLocationQualifier
import com.carshering.databinding.ActivityMainMapBinding
import com.carshering.domain.entity.Car
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MainMapActivity : AppCompatActivity(), OnMapReadyCallback, Contract.View,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMainMapBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var qualifier: UserLocationQualifier

    private val presenter = Presenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onAttach(this)
        initMap()
        initBottomSheet(findViewById(R.id.car_card))

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

            return
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
            }
        }
    }

    override fun putMarkers(cars: List<Car>) {
        cars.forEach { car ->
            val latLng = LatLng(car.lat, car.lng)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
            marker?.tag = car.id
        }
    }

    override fun showErrorToast() {
        Toast.makeText(this, R.string.error_toast, Toast.LENGTH_SHORT).show()
    }

    override fun moveCamera(startPosition: CameraPosition) {
        map.moveCamera(CameraUpdateFactory.newCameraPosition(startPosition))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val carId = marker.tag.toString()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        presenter.onMarkerClicked(carId)
        return true
    }

    override fun updateBottomSheet(car: Car) {
        findViewById<TextView>(R.id.car_name_text_view).text = car.model
        findViewById<TextView>(R.id.seats_text_view).text = car.seats.toString()
        findViewById<TextView>(R.id.remain_range_text_view).text =
            car.remainRange.toString() + resources.getString(R.string.remain_range_measure)

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

        presenter.fromEnumToColor(car.color)
        presenter.fromEnumToTransmission(car.transmission)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun showRoute(latLng: LatLng?) {
        Toast.makeText(this, latLng.toString(), Toast.LENGTH_LONG).show()
    }

    override fun setCarColorField(colorRussianTitle: Int, colorCode: Int) {
        val colorToDisplay = resources.getColor(colorCode)

        findViewById<TextView>(R.id.color_text_view).text =
            resources.getString(colorRussianTitle)
        findViewById<ImageView>(R.id.car_color_container_image_view).setColorFilter(
            colorToDisplay
        )
    }

    override fun setCarTransmission(transmissionRussianTitle: Int) {
        findViewById<TextView>(R.id.transmission_text_view).text =
            resources.getString(transmissionRussianTitle)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach(this)
    }
}