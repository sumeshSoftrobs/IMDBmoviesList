package com.softrobs.imdb_movies_list.data.remote

import android.content.Context
import com.softrobs.imdb_movies_list.data.remote.apiStateManagement.APIState
import com.softrobs.imdb_movies_list.data.remote.apiStateManagement.GetResponse
import com.softrobs.imdb_movies_list.data.remote.response.MovieDetailsModel
import com.softrobs.imdb_movies_list.data.remote.response.MovieSearchDataModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val service: Service) {

    suspend fun getMoviesListAsync(search:String,apiKey:String):Flow<APIState<MovieSearchDataModel>>{
        return GetResponse.fromFlow {
            service.getMoviesList(search,apiKey)
        }
    }
    suspend fun getMovieDetails(id:String,apiKey: String):Flow<APIState<MovieDetailsModel>>{
        return GetResponse.fromFlow {
            service.getMovieDetails(details = id, apiKey = apiKey)
        }
    }
}