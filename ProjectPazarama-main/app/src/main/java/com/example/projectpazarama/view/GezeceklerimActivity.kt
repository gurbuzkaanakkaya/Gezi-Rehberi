package com.example.projectpazarama.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpazarama.R
import com.example.projectpazarama.data.TravelOperations
import com.example.projectpazarama.databinding.ActivityGezeceklerimBinding
import com.example.projectpazarama.model.Place

class GezeceklerimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGezeceklerimBinding
    private val travelOperation: TravelOperations = TravelOperations(this@GezeceklerimActivity)

    private lateinit var adapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGezeceklerimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListeners()

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it: ActivityResult ->

            if (it.resultCode == RESULT_OK) {
                var visitedPlacesUpdate = travelOperation.getUnVisitedPlaces()
                binding.listGezeceklerim.adapter = initPlacesAdapter(visitedPlacesUpdate)
            }
        }
        binding.include.ibYerEkle.setOnClickListener {
            startForResult.launch(Intent(this@GezeceklerimActivity,AddPlaceActivity::class.java))
        }
    }

    private fun initViews() {
        with(binding) {

            listGezeceklerim.layoutManager =
                LinearLayoutManager(this@GezeceklerimActivity, LinearLayoutManager.VERTICAL, false)
            listGezeceklerim.adapter = initPlacesAdapter(travelOperation.getUnVisitedPlaces())

            include.tvGezilecekler.setTextColor(ResourcesCompat.getColor(resources,R.color.purple_500,null))
            include.tvGezdiklerim.setTextColor(ResourcesCompat.getColor(resources,R.color.gray,null))

            binding.toolbar.toolbarTitle.text = getString(R.string.gezilecekler)
            binding.toolbar.toolbarBtn.visibility = View.GONE
        }
    }

    private fun initPlacesAdapter(places: ArrayList<Place>): PlacesAdapter {
        return PlacesAdapter(placeList = places, onClickListener = {
            val intent = Intent(this@GezeceklerimActivity, PlaceDetailActivity::class.java)
            intent.putExtra("place", this.Id)
            startActivity(intent)
        })
    }

    private fun initListeners() {
        with(binding){
            include.ibGezdiklerim.setOnClickListener {
                goGezdiklerim()
            }

        }
    }
    private fun goGezdiklerim() {
        val intent = Intent(this@GezeceklerimActivity, GezdiklerimActivity::class.java)
        startActivity(intent)
    }
}