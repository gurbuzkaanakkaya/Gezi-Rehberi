package com.example.projectpazarama.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectpazarama.R
import com.example.projectpazarama.model.VisitHistory

class DetailAdapter(
     var list: List<VisitHistory>
) : RecyclerView.Adapter<DetailAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(visitHistory: VisitHistory) {
            val date = itemView.findViewById<TextView>(R.id.visitDate)
            val desc = itemView.findViewById<TextView>(R.id.visitDesc)

            date.text = visitHistory.Date
            desc.text = visitHistory.Description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.visit_history_recycler_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshList(newList:List<VisitHistory>){
        list = newList
        notifyDataSetChanged()
    }
}