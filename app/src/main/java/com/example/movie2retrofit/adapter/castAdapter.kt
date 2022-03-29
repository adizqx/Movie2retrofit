package com.example.movie2retrofit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie2retrofit.R
import com.example.movie2retrofit.castModel.Cast

class castAdapter(private val context: Context): RecyclerView.Adapter<castAdapter.castViewHolder>() {
    var cast = emptyList<Cast>()

    inner class castViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var castImage: ImageView = itemView.findViewById(R.id.castImage)
        var castTitle: TextView = itemView.findViewById(R.id.castTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): castViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent, false)
        return castViewHolder(view)
    }

    override fun onBindViewHolder(holder: castViewHolder, position: Int) {
        val item = cast[position]
        holder.castTitle.text = item.name
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + item.profile_path).into(holder.castImage)
    }

    override fun getItemCount(): Int {
        return cast.size
    }

    fun setCastList(list: List<Cast>){
        cast = list
        notifyDataSetChanged()
    }


}