package com.softrobs.imdb_movies_list.data.local.roomdb

import com.softrobs.imdb_movies_list.data.remote.response.Search
import javax.inject.Inject

class RoomRepository @Inject constructor(private val dao: Dao) {

    suspend fun getMovie() = dao.getAllMoviesDetails()

    suspend fun addMovie(movie: List<Search>) = dao.storeAllMovies(movie)

    suspend fun removeOldData() = dao.removeOldData()

}