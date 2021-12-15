package com.skripsi.presensigps.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
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
import com.skripsi.presensigps.network.ApiClient
import com.skripsi.presensigps.network.ResponseModel
import com.skripsi.presensigps.utils.PreferencesHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = this.toString()
    private lateinit var progressDialog: ProgressDialog

    private lateinit var sharedPref: PreferencesHelper
    private lateinit var userId: String

    private val btnBack: FloatingActionButton by lazy { findViewById(R.id.fabBack) }
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

    private var type = ""
    private var intentLat = ""
    private var intentLng = ""

    private var reqBody: RequestBody? = null
    private var partImage: MultipartBody.Part? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResultCallback: LocationResult) {
            super.onLocationResult(locationResultCallback)

            locationResult = locationResultCallback

            myLocation =
                LatLng(
                    locationResult.locations[0].latitude,
                    locationResult.locations[0].longitude
                )

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

        progressDialog.dismiss()

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

        btnBack.setOnClickListener {
            finish()
        }
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

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("memuat informasi...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        sharedPref = PreferencesHelper(this)
//        userId = sharedPref.getString(Constant.PREF_USER_ID).toString()

        type = intent.getStringExtra("type").toString()
        intentLat = intent.getStringExtra("latitude").toString()
        intentLng = intent.getStringExtra("longitude").toString()

        if (type == "report") {

            btnOfficeLocation.visibility = View.GONE
            btnPresence.visibility = View.INVISIBLE
            btnReport.visibility = View.VISIBLE

            btnReport.setOnClickListener {
                startActivity(
                    Intent(this, SendReportActivity::class.java)
                        .putExtra("latitude", myLocation.latitude)
                        .putExtra("longitude", myLocation.longitude)
                )
            }

        } else if (type == "presence") {
            getLocation()

            btnOfficeLocation.visibility = View.VISIBLE
            btnPresence.visibility = View.VISIBLE
            btnReport.visibility = View.GONE

        } else {

            btnPresence.visibility = View.GONE
            btnReport.visibility = View.GONE

            btnMyLocation.visibility = View.GONE
            btnOfficeLocation.visibility = View.GONE
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

    private fun getLocation() {

        ApiClient.SetContext(this).instances.apiGetLocation().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    val status = response.body()?.status
                    val data = response.body()?.data_today

                    if (status == true) {

//                        progressDialog.dismiss()

                        latOffice = data?.latitude!!
                        longOffice = data.longitude
                        radius = data.radius

                        btnPresence.setOnClickListener {
                            if (distance <= radius) {

                                ImagePicker.with(this@MapsActivity)
                                    .cameraOnly()
                                    .cropSquare()
                                    .compress(1024)         //Final image size will be less than 1 MB(Optional)
                                    .createIntent { intent ->
                                        startForProfileImageResult.launch(intent)
                                    }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Anda diluar jarak kantor",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("distance:", "  diluar kawasan")
                            }
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Gagal : " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Gagal : " + t.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
//                    imageView.setImageURI(fileUri)

                val image: File = File(fileUri.path!!)

                reqBody = image.asRequestBody("image/*".toMediaTypeOrNull())

                partImage = MultipartBody.Part.createFormData("image", image.name, reqBody!!)

                addPresence(partImage)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(applicationContext, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun addPresence(img: MultipartBody.Part?) {

        if (img != null) {
            ApiClient.SetContext(this).instances.apiAddPresence(img).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        val status = response.body()?.status

                        if (status == true) {
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Gagal : " + response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Gagal : " + t.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }
}