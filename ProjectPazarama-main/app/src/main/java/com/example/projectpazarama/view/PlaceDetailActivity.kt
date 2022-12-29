package com.example.projectpazarama.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.projectpazarama.R
import com.example.projectpazarama.data.TravelOperations
import com.example.projectpazarama.databinding.ActivityPlaceDetailBinding
import com.example.projectpazarama.model.Place
import com.example.projectpazarama.model.PlaceImages
import com.example.projectpazarama.model.VisitHistory
import com.synnapps.carouselview.CarouselView

class PlaceDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlaceDetailBinding
    var place = Place()
    var placeId : Int? = null
    private var historyList : ArrayList<VisitHistory>? = null
    var placeInfo = Place()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(intent) {
            placeId = getIntExtra("place",0)
        }

        val travelOperations = TravelOperations(this)
        placeInfo = placeId?.let { travelOperations.getPlaceById(it) }!!
        historyList = travelOperations.getVisitHistory(placeId!!)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.historyRecycler.layoutManager = layoutManager
        binding.historyRecycler.adapter = DetailAdapter(historyList!!)


        binding.toolbar.toolbarTitle.text = placeInfo.Name
        binding.toolbar.toolbarBtn.setOnClickListener {
            onBackPressed()
        }

        binding.textShortDesc.text = placeInfo.Definition
        binding.textShortExplanation.text = placeInfo.Description
        if (placeInfo.Priority == 1) {
            binding.priorityIcon.setImageResource(R.drawable.ic_circle_green)
        } else if (placeInfo.Priority == 2) {
            binding.priorityIcon.setImageResource(R.drawable.ic_baseline_circle_24)
        } else if (placeInfo.Priority == 3) {
            binding.priorityIcon.setImageResource(R.drawable.ic_circle_gray)
        }

        var list = placeInfo.images


        val carouselView = findViewById<CarouselView>(R.id.carouselView)
        val dateTxt = findViewById<TextView>(R.id.dateTxt)
        if (list != null) {
            carouselView.setPageCount(list.size)
        }

        carouselView.setImageListener { position, imageView ->
            imageView.setImageBitmap(list!![position]?.Image)
        }

        carouselView.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                dateTxt.text = list!![position]?.Date
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        val addVisit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK) {
                val newList = travelOperations.getVisitHistory(placeId!!)
                (binding.historyRecycler.adapter as DetailAdapter).refreshList(newList)
            }
        }


        binding.addVisitButton.setOnClickListener {
            val intent = Intent(this, AddVisitActivity::class.java)
            intent.putExtra("place", placeId)
            addVisit.launch(intent)
        }
        binding.showLocationButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("placeId", placeId)
            startActivity(intent)
        }


    }



}