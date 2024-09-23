package com.example.samojlov_av_homework_module_13_number_8

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
class CustomAdapter(private val list: MutableList<Clothes>) :
    RecyclerView.Adapter<CustomAdapter.ClothesViewHolder>() {

    private var onClothesClickListener: OnClothesClickListener? = null

    interface OnClothesClickListener {
        fun onClothesClick(clothes: Clothes, position: Int)
    }

    class ClothesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameItemTV)
        val image: ImageView = itemView.findViewById(R.id.imageSmallCircleIV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ClothesViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        val clothes = list[position]
        holder.nameText.text = clothes.name

        if (clothes.image!!.toIntOrNull() != null) holder.image.setImageResource(clothes.image!!.toInt())
        else {
            val type = typeOf<Bitmap>().javaType
            val image = Gson().fromJson<Bitmap>(clothes.image!!,type)
            holder.image.setImageBitmap(image)
        }


        holder.itemView.setOnClickListener {
            if (onClothesClickListener != null) {
                onClothesClickListener!!.onClothesClick(clothes, position)
            }
        }
    }

    fun setOnClothesClickListener(onClothesClickListener: OnClothesClickListener) {
        this.onClothesClickListener = onClothesClickListener
    }
}