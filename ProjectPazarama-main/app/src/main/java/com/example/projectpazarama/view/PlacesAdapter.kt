package com.example.projectpazarama.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpazarama.R
import com.example.projectpazarama.model.Place

class PlacesAdapter(
    private var placeList: List<Place>,
    private inline val onClickListener: Place.() -> Unit
) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(place: Place, onClickListener: Place.() -> Unit) {

            val placeName = itemView.findViewById<TextView>(R.id.tvPlaceName)
            val placeDefination = itemView.findViewById<TextView>(R.id.tvPlaceDefination)
            val placeDescription = itemView.findViewById<TextView>(R.id.tvPlaceDescription)
            val placeVisitedDate = itemView.findViewById<TextView>(R.id.tvPlaceVisitedDate)
            val placePriority = itemView.findViewById<ImageView>(R.id.imPlacePriority)
            val placeImage = itemView.findViewById<ImageView>(R.id.ivPlace)

            placeName.text = place.Name
            placeDefination.text = place.Definition
            placeDescription.text = place.Description

            if (place.Visited == true) {
                placeVisitedDate.text = place.LastVisitDate
                placeVisitedDate.visibility = View.VISIBLE
                placePriority.visibility = View.GONE
            } else {

                var priorityDrawable =
                    ResourcesCompat.getDrawable(itemView.resources, R.drawable.ic_add, null)!!
                when (place.Priority) {
                    1 -> priorityDrawable = ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.ic_circle_green,
                        null
                    )!!
                    2 -> priorityDrawable = ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.ic_baseline_circle_24,
                        null
                    )!!
                    3 -> priorityDrawable = ResourcesCompat.getDrawable(
                        itemView.resources,
                        R.drawable.ic_circle_gray,
                        null
                    )!!
                }

                placePriority.setImageDrawable(priorityDrawable)

                placeImage.setImageBitmap(place.images?.firstOrNull()?.Image)
                placeVisitedDate.visibility = View.GONE
                placePriority.visibility = View.VISIBLE
            }

            itemView.setOnClickListener {
                onClickListener.invoke(place)
            }
        }
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(placeList[position], onClickListener)
    }

    fun refreshList(visitedPlaces: ArrayList<Place>) {
        this.placeList = visitedPlaces
    }
}
