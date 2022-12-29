package com.example.projectpazarama.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.provider.MediaStore
import android.widget.LinearLayout.HORIZONTAL
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpazarama.R
import com.example.projectpazarama.data.TravelOperations
import com.example.projectpazarama.databinding.ActivityAddPlaceBinding
import com.example.projectpazarama.model.Place
import com.example.projectpazarama.model.PlaceImages
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.LocalDate

class AddPlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPlaceBinding
    private lateinit var imageAdapter: AddImageAdapter
    private lateinit var imageList : ArrayList<PlaceImages>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap : Bitmap? = null
    var latitude :String? = null
    var longitude:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerLauncher()
        var priorityValue = 1




        val priorityList = arrayListOf("Birinci Öncelik", "İkinci Öncelik", "Üçüncü Öncelik")
        val adapt : ArrayAdapter<String> = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, priorityList)
        binding.spinner.adapter = adapt
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, id: Long) {
                if (priorityList[i] == "Birinci Öncelik") { //yeşil
                    binding.spinnerIcon.setImageResource(R.drawable.ic_circle_green)
                } else if (priorityList[i] == "İkinci Öncelik") { // mavi
                    println("girdi")
                    binding.spinnerIcon.setImageResource(R.drawable.ic_baseline_circle_24)
                } else if (priorityList[i] == "Üçüncü Öncelik") { //gri
                    binding.spinnerIcon.setImageResource(R.drawable.ic_circle_gray)
                }
                priorityValue = i+1
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }
        }
        val travelOperations = TravelOperations(this)



        imageList = ArrayList<PlaceImages>()
        imageAdapter = AddImageAdapter(imageList,null)
        binding.recyclerView2.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView2.adapter = imageAdapter

        binding.toolbar.toolbarTitle.text = getString(R.string.yer_ekle)
        binding.toolbar.toolbarBtn.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddLocation.setOnClickListener {
            val intent = Intent(this,MapsActivity::class.java)
            intent.putExtra("check","newLocation")
            locationResLauncher.launch(intent)
        }


        binding.btnSavePlace.setOnClickListener {
            val placeName = binding.editPlaceName.text.toString()
            val placeDefinition = binding.editPlaceShortDefinition.text.toString()
            val placeDesc = binding.editPlaceShortDesc.text.toString()

            if(latitude.isNullOrEmpty() || longitude.isNullOrEmpty()){
                Toast.makeText(this,"Lütfen bir konum ekleyiniz",Toast.LENGTH_SHORT).show()
            }else{
                val place = Place()
                place.apply {
                    Name=placeName
                    Definition=placeDefinition
                    Description=placeDesc
                    Latitude = latitude
                    Longitude = longitude
                    Priority = priorityValue
                }

                travelOperations.addPlace(place,imageList)
                setResult(RESULT_OK)
                finish()


            }
        }
    }
    fun actionButton(view : View){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Snackbar.make(view,"İzin gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver",View.OnClickListener {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallerry = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallerry)
        }
    }
    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val intentFromResult = it.data
                if(intentFromResult != null){
                    val imageData = intentFromResult.data
                    if(imageData != null) {
                        try {
                            if(Build.VERSION.SDK_INT >= 28){
                                var source = ImageDecoder.createSource(this.contentResolver, imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                            }else{
                                selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                            }
                            var placeImages = PlaceImages()
                            val c = Calendar.getInstance()


                            placeImages.Date =SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().time).toString()
                            println("DATE VALUE : "+placeImages.Date)
                            placeImages.Image = selectedBitmap?.copy(Bitmap.Config.ARGB_8888, false)
                            imageList.add(placeImages)
                            imageAdapter.notifyDataSetChanged()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if(result){
                //permission granted
                val intentToGallerry = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallerry)
            }else{
                Toast.makeText(this,"İzin Verilmeli",Toast.LENGTH_SHORT).show()
            }
        }
    }

    val locationResLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            latitude = it.data!!.getStringExtra("latitude")
            longitude = it.data!!.getStringExtra("longitude")

            binding.btnAddLocation.setBackgroundColor(resources.getColor(R.color.locationSuccessColor,null))
            binding.btnAddLocation.text = "Konum Eklendi"
        }
    }

}