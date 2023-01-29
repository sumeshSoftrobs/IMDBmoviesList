package com.softrobs.imdb_movies_list.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softrobs.imdb_movies_list.data.remote.response.MovieSearchDataModel
import com.softrobs.imdb_movies_list.data.remote.response.Search
import com.softrobs.imdb_movies_list.databinding.MoviesListRecyclerLayoutBinding
import com.softrobs.imdb_movies_list.ui.listeners.MovieItemClickListener
import com.squareup.picasso.Picasso

class MoviesListRecyclerAdapter(
    private var moviesList:List<Search>,
    private val itemClickListener: MovieItemClickListener
) : RecyclerView.Adapter<MoviesListRecyclerAdapter.MoviesHolder>() {

    inner class MoviesHolder(val itemBinding: MoviesListRecyclerLayoutBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesHolder {
        val binding =
            MoviesListRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MoviesHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesHolder, position: Int) {
        val mItem:Search = moviesList[position]
        with(holder){
            Picasso.get().load(mItem.Poster).into(itemBinding.posterImg)
            itemBinding.title.text = mItem.Title
            itemBinding.releaseYear.text = mItem.Year
            itemBinding.posterImg.setOnClickListener(View.OnClickListener {
                itemClickListener.onMovieItemClicked(mItem.imdbID)
            })
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onMovieItemClicked(mItem.imdbID)
        })
    }

    override fun getItemCount(): Int {
       return moviesList.size
    }
    fun setFilter(modelList:ArrayList<Search>){
        moviesList = modelList
        notifyDataSetChanged()
    }

}