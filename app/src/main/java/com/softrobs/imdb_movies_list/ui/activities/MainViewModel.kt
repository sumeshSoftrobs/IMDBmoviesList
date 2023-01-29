package com.softrobs.imdb_movies_list.ui.activities

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softrobs.imdb_movies_list.data.remote.NetworkRepository
import com.softrobs.imdb_movies_list.data.remote.apiStateManagement.APIState
import com.softrobs.imdb_movies_list.data.remote.response.MovieDetailsModel
import com.softrobs.imdb_movies_list.data.remote.response.MovieSearchDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NetworkRepository
    ) : ViewModel() {
    private val moviesListResponseFlow =
        Channel<APIState<MovieSearchDataModel>>(Channel.BUFFERED)
    val observeMovieListReceivedResponse = moviesListResponseFlow.receiveAsFlow()

    fun invokeMovieListApi(search: String, apiKeyString: String) {
        viewModelScope.launch {
            repository.getMoviesListAsync(search = search, apiKey = apiKeyString)
                .catch { e ->
                    Log.d("MovieListApi : ", e.toString())

                }.collect {
                    response ->
                    moviesListResponseFlow.send(response)
                }
        }
    }
}