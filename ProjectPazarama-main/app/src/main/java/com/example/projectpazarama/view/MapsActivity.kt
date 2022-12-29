package com.example.projectpazarama.view


import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projectpazarama.R
import com.example.projectpazarama.data.TravelOperations


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projectpazarama.databinding.ActivityMapsBinding
import com.example.projectpazarama.model.Place
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMapLongClickListener {
    private lateinit var gMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences
    private var trackInfo : Boolean? = null
    private var selectedLatitude : Double? = null
    private var selectedLongitude : Double? = null
    var placeOfOldLocation : Place? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()
        sharedPreferences = this.getSharedPreferences("com.example.projectpazarama.view", MODE_PRIVATE)
        trackInfo = false
        binding.go.isEnabled = true

        selectedLatitude = 0.0
        selectedLongitude = 0.0

        binding.toolbar.toolbarTitle.text = getString(R.string.konum_sec)
        binding.toolbar.toolbarBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.setOnMapLongClickListener(this)
        val intent = intent
        val info = intent.getStringExtra("check")
        if (info.equals("newLocation")){
            binding.save.visibility = View.VISIBLE
            binding.go.visibility = View.GONE
            locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            locationListener = object  : LocationListener {
                override fun onLocationChanged(location: Location) {
                    trackInfo = sharedPreferences.getBoolean("trackInfo",false)
                    if (trackInfo == false){val userLocation = LatLng(location.latitude,location.longitude)
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                        sharedPreferences.edit().putBoolean("trackInfo",true).apply()
                    }
                }
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    Snackbar.make(binding.root,"Konum izini gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }.show()
                }else{
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (lastLocation != null){
                    val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
                }
                gMap.isMyLocationEnabled = true
            }
        }else{
            gMap.clear()
            placeOfOldLocation = TravelOperations(this).getPlaceById(intent.getIntExtra("placeId",0))
            placeOfOldLocation?.let { place ->
                val oldLocation = place.Latitude?.let { place.Longitude?.let { it1 ->
                    LatLng(it.toDouble(), it1.toDouble()) } }
                oldLocation?.let { MarkerOptions().position(it).title(place.Name) }
                    ?.let { gMap.addMarker(it) }
                oldLocation?.let { CameraUpdateFactory.newLatLngZoom(it,7f) }
                    ?.let { gMap.moveCamera(it) }
                binding.save.visibility = View.GONE
                binding.go.visibility = View.VISIBLE
            }
        }
    }
    private fun registerLauncher(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastLocation != null){
                        val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
                    }
                    gMap.isMyLocationEnabled = true
                }
            }else{
                Toast.makeText(this@MapsActivity,"İzin gerekli",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMapLongClick(markerLocation: LatLng) {
        gMap.clear()
        gMap.addMarker(MarkerOptions().position(markerLocation))
        selectedLatitude = markerLocation.latitude
        selectedLongitude = markerLocation.longitude
        println("Selected latitude : "+selectedLatitude)
        println("Selected longitude : "+ selectedLongitude)
        binding.save.visibility = View.VISIBLE
        binding.go.visibility = View.GONE

    }
    fun addPlaceLocation(view : View){
        if(selectedLatitude != null && selectedLongitude != null){
            val intent = Intent()
            intent.putExtra("latitude",selectedLatitude.toString())
            intent.putExtra("longitude",selectedLongitude.toString())
            setResult(RESULT_OK,intent)
            finish()

        }
    }
    fun go(view : View){
        var uri = Uri.parse("geo:$selectedLatitude,$selectedLongitude")
        var intent = Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }
}