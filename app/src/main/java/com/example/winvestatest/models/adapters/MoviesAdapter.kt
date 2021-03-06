package com.example.winvestatest.models.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.winvestatest.core.utility.AppConfiguration
import com.example.winvestatest.models.Result
import com.example.winvestatest.models.interfaces.MovieItemClick
import com.example.winvestatest.databinding.MovieItemBinding

class MoviesAdapter(
    private val context: Context,
    private var list: ArrayList<Result>,
    private val movieItemClick: MovieItemClick
) :
    RecyclerView.Adapter<MoviesAdapter.MoviesHolder>() {

    class MoviesHolder(val mBinding: MovieItemBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        return MoviesHolder(MovieItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItems(newList: List<Result>) {
        val old = list.size

        list.addAll(newList)
        val new = list.size
        Log.d("Size", "${list.size}")
        notifyItemRangeInserted(old, new)
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        //holder.mBinding.imageView
        if (list[position].poster_path != "null")
            Glide
                .with(context)
                .load(AppConfiguration.IMAGE_BASE_URL + list[position].poster_path)
                .into(holder.mBinding.imageView)
        holder.mBinding.imageView.setOnClickListener {
            movieItemClick.onItemClick(position, list[position].id!!)
        }

    }
}