package com.example.samojlov_av_homework_module_13_number_8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val list: MutableList<Clothes>) :
    RecyclerView.Adapter<CustomAdapter.ClothesViewHolder>() {

    class ClothesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameItemTV)
        val description: TextView =itemView.findViewById(R.id.descriptionItemTV)
        val image: ImageView = itemView.findViewById(R.id.imageSmallCircleIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return  ClothesViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        val clothes = list[position]
        holder.nameText.text = clothes.name
        holder.description.text = clothes.description
        holder.image.setImageResource(clothes.image!!)
    }
}