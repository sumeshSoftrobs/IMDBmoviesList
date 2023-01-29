package com.softrobs.imdb_movies_list.data.remote

import com.softrobs.imdb_movies_list.data.remote.response.MovieDetailsModel
import com.softrobs.imdb_movies_list.data.remote.response.MovieSearchDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Service {
    @GET("/")
    suspend fun getMoviesList(
        @Query("s") searchKey:String,
        @Query("apikey") apiKey:String
    ):Response<MovieSearchDataModel>

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") details:String,
        @Query("apikey") apiKey: String
    ):Response<MovieDetailsModel>

}