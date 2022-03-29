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
import com.example.movie2retrofit.model.Result

class movieAdapter(private val context: Context, val mItemClickListener: ItemClickListener) :
    RecyclerView.Adapter<movieAdapter.movieViewHolder>() {
    var movies = emptyList<Result>()

    inner class movieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageMoview: ImageView = itemView.findViewById(R.id.imageMovie)
        var title: TextView = itemView.findViewById(R.id.movieNameMain)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): movieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return movieViewHolder(view)
    }

    override fun onBindViewHolder(holder: movieViewHolder, position: Int) {
        val item = movies[position]
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + item.poster_path)
            .into(holder.imageMoview)
        holder.title.text = item.title
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(item.id) }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setList(list: List<Result>) {
        movies = list
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClick(id: Int)
    }
}