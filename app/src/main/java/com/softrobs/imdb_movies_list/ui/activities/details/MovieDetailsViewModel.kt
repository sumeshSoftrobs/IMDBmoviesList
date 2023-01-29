package com.softrobs.imdb_movies_list.ui.activities.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softrobs.imdb_movies_list.data.remote.NetworkRepository
import com.softrobs.imdb_movies_list.data.remote.apiStateManagement.APIState
import com.softrobs.imdb_movies_list.data.remote.response.MovieDetailsModel
import com.softrobs.imdb_movies_list.data.remote.response.MovieSearchDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val repository: NetworkRepository) : ViewModel() {
    private val moviesDetailsResponseFlow =
        Channel<APIState<MovieDetailsModel>>(Channel.BUFFERED)
    val observeMovieDetailsReceivedResponse = moviesDetailsResponseFlow.receiveAsFlow()

    fun invokeMovieDetailsApi(details: String, apiKeyString: String) {
        viewModelScope.launch {
            repository.getMovieDetails(id = details , apiKey = apiKeyString)
                .catch { e ->
                    Log.d("MovieDetailsApi : ", e.toString())

                }.collect {
                        response ->
                    moviesDetailsResponseFlow.send(response)
                }
        }
    }
}