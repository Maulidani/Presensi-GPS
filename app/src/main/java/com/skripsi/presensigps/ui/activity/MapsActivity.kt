package com.skripsi.presensigps.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skripsi.presensigps.R
import com.skripsi.presensigps.utils.PreferencesHelper

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = this.toString()

    private lateinit var sharedPref: PreferencesHelper
    private lateinit var userId: String

    private val btnMyLocation: FloatingActionButton by lazy { findViewById(R.id.fabMyLocation) }
    private val btnOfficeLocation: FloatingActionButton by lazy { findViewById(R.id.fabOfficeLocation) }
    private val btnPresence: MaterialButton by lazy { findViewById(R.id.btnPresence) }
    private val btnReport: MaterialButton by lazy { findViewById(R.id.btnReport) }

    private val locationRequestCode = 1001
    private val cameraPermissionCode = 1

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationResult: LocationResult
    lateinit var cameraUpdate: CameraUpdate

    lateinit var myLocation: LatLng
    lateinit var officeLocation: LatLng
    private var latOffice: Double = 0.0
    private var longOffice: Double = 0.0
    private var radius: Double = 0.0

    private var cameraZoom: Boolean = false
    private var distance: Float = 0.1f

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResultCallback: LocationResult) {
            super.onLocationResult(locationResultCallback)

            locationResult = locationResultCallback
            myLocation =
                LatLng(locationResult.locations[0].latitude, locationResult.locations[0].longitude)

            setLatLng(
                locationResult.locations[0].latitude, locationResult.locations[0].longitude
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun setLatLng(myLatitude: Double, myLongitude: Double) {

        myLocation = LatLng(myLatitude, myLongitude)

        officeLocation = LatLng(latOffice, longOffice)

        cameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition.builder().target(myLocation).zoom(19f).build()
        )

        if (!cameraZoom) mMap.animateCamera(cameraUpdate)
        cameraZoom = true

        mMap.clear()
        mMap.addMarker(MarkerOptions().position(myLocation).title("Lokasi Saya"))

        val circleOptions = CircleOptions()
        circleOptions.center(officeLocation)
        circleOptions.radius(radius)
        circleOptions.fillColor(0x30ff0000)
        circleOptions.strokeWidth(5f)
        circleOptions.strokeColor(Color.GRAY)

        mMap.addCircle(circleOptions)

        val officeLoc = Location("Office Location")
        officeLoc.latitude = latOffice
        officeLoc.longitude = longOffice

        val myLoc = Location("My Location")
        myLoc.latitude = myLatitude
        myLoc.longitude = myLongitude

        distance = officeLoc.distanceTo(myLoc)

        Log.e(TAG, "setLatLng: $distance")
    }

    //button onClick
    private fun button() {
        btnMyLocation.setOnClickListener {

            if (myLocation == null) {

            } else {
                cameraUpdate = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(
                        LatLng(
                            locationResult.locations[0].latitude,
                            locationResult.locations[0].longitude
                        )
                    ).zoom(19f).build()
                )
                mMap.animateCamera(cameraUpdate)
            }
        }
        btnOfficeLocation.setOnClickListener {

            if (officeLocation == null) {

            } else {
                cameraUpdate = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(
                        LatLng(
                            latOffice,
                            longOffice
                        )
                    ).zoom(19f).build()
                )
                mMap.animateCamera(cameraUpdate)
            }
        }
    }

    lateinit var type: String

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        sharedPref = PreferencesHelper(this)
//        userId = sharedPref.getString(Constant.PREF_USER_ID).toString()

        type = intent.getStringExtra("type").toString()
        latOffice = intent.getStringExtra("latitude")?.toDouble() ?: 0.0
        longOffice = intent.getStringExtra("longitude")?.toDouble() ?: 0.0
        radius = intent.getStringExtra("radius")?.toDouble() ?: 0.0

        if (type == "report") {
            btnOfficeLocation.visibility = View.GONE
            btnPresence.visibility = View.INVISIBLE
            btnReport.visibility = View.VISIBLE

            btnReport.setOnClickListener {
//                openCamera()
            }

        } else if (type == "presence") {
            btnOfficeLocation.visibility = View.VISIBLE
            btnPresence.visibility = View.VISIBLE
            btnReport.visibility = View.GONE

            btnPresence.setOnClickListener {
                if (distance <= radius) {
//                    openCamera()
                } else {
                    Log.e("distance:", "  diluar kawasan")
                }
            }
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 2000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        button()
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            checkSettingAndStartLocationUpdates()
        } else {
            askLocationPermission()
        }

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    //permission
    private fun checkSettingAndStartLocationUpdates() {
        val request: LocationSettingsRequest =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
        val client: SettingsClient = LocationServices.getSettingsClient(this)

        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(request)

        task.addOnSuccessListener {
            startLocationUpdates()
        }

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                val apiException: ResolvableApiException = it
                try {
                    apiException.startResolutionForResult(this, locationRequestCode)
                    askLocationPermission()
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun askLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                locationRequestCode
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                locationRequestCode
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mFusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private var thumbNail: Bitmap? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSettingAndStartLocationUpdates()
            }
        }
    }

}