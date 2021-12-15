package com.skripsi.presensigps.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.skripsi.presensigps.R

class MapsInfoActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var intentLat = ""
    private var intentLng = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_info)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val type = intent.getStringExtra("type").toString()
        intentLat = intent.getStringExtra("latitude").toString()
        intentLng = intent.getStringExtra("longitude").toString()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val location = LatLng(intentLat.toDouble(), intentLng.toDouble())
        mMap.addMarker(MarkerOptions().position(location).title("Lokasi Laporan"))

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(
            CameraPosition.builder().target(location).zoom(19f).build()
        )

        mMap.animateCamera(cameraUpdate)
        mMap.addMarker(MarkerOptions().position(location).title("Lokasi Saya"))
    }
}