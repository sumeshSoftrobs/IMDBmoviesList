package com.softrobs.imdb_movies_list.ui.activities

import com.softrobs.imdb_movies_list.data.local.roomdb.RoomRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softrobs.imdb_movies_list.data.remote.response.Search
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(private val repository: RoomRepository) : ViewModel() {
    fun insertMovies(movie: List<Search>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movie)
        }
    }

    var roomDatabase = MutableLiveData<List<Search?>>()
    fun getAllMovies() {
        viewModelScope.launch {
            roomDatabase.postValue(repository.getMovie())
        }
    }

    fun removeOldData() {
        viewModelScope.launch {
            repository.removeOldData()
        }
    }
}