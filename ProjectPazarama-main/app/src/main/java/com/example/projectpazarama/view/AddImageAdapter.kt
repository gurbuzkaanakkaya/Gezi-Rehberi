package com.example.projectpazarama.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpazarama.R
import com.example.projectpazarama.Util.ImageHelper
import com.example.projectpazarama.databinding.AddImageRecyclerRowBinding
import com.example.projectpazarama.model.PlaceImages

class AddImageAdapter(val imageList : ArrayList<PlaceImages>,val onDelete: ((deleteId: Int) -> Unit)?) : RecyclerView.Adapter<AddImageAdapter.ImageHolder>() {
    class ImageHolder(val binding: AddImageRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = AddImageRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.binding.image.setImageBitmap(imageList.get(position).Image)
        holder.binding.delete.setOnClickListener {
            if (imageList.get(position).Id == null){
                imageList.removeAt(position)
                notifyDataSetChanged()
            }else{
                onDelete?.invoke(imageList.get(position).Id!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}