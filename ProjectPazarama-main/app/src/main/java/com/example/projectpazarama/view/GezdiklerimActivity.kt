package com.example.projectpazarama.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectpazarama.R
import com.example.projectpazarama.data.TravelOperations
import com.example.projectpazarama.databinding.ActivityGezdiklerimBinding
import com.example.projectpazarama.databinding.ActivityGezeceklerimBinding
import com.example.projectpazarama.model.Place

class GezdiklerimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGezdiklerimBinding
    private val travelOperation: TravelOperations = TravelOperations(this@GezdiklerimActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGezdiklerimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListeners()
    }

    private fun initViews() {
        with(binding) {
            listGezdiklerim.layoutManager =
                LinearLayoutManager(this@GezdiklerimActivity, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            listGezdiklerim.adapter = initPlacesAdapter(travelOperation.getVisitedPlaces())

            include.tvGezdiklerim.setTextColor(ResourcesCompat.getColor(resources,R.color.purple_500,null))
            include.tvGezilecekler.setTextColor(ResourcesCompat.getColor(resources,R.color.gray,null))

            binding.toolbar.toolbarTitle.text = getString(R.string.gezdiklerim)
            binding.toolbar.toolbarBtn.visibility = View.GONE
        }
    }

    private fun initPlacesAdapter(places: ArrayList<Place>): PlacesAdapter {
        return PlacesAdapter(placeList = places, onClickListener = {
            val intent = Intent(this@GezdiklerimActivity, PlaceDetailActivity::class.java)
            intent.putExtra("place", this.Id)
            startActivity(intent)
        })
    }

    private fun initListeners() {
        with(binding){
            include.ibGezilecekler.setOnClickListener {
                goGezeceklerim()
            }
            include.ibYerEkle.setOnClickListener {
                goYerEkle()
            }
        }
    }

    private fun goYerEkle() {
        val intent = Intent(this@GezdiklerimActivity, AddPlaceActivity::class.java)
        startActivity(intent)
    }

    private fun goGezeceklerim() {
        val intent = Intent(this@GezdiklerimActivity, GezeceklerimActivity::class.java)
        startActivity(intent)
    }
}