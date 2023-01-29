package com.softrobs.imdb_movies_list.data.remote.apiStateManagement

import okhttp3.ResponseBody

sealed class APIState<out T> {
    data class SUCCESS<out R>(val getResponse: R) : APIState<R>()
    data class ERROR(val message: String, val isNetworkError: Boolean,
                     val errorCode: Int?,
                     val errorBody: ResponseBody?) : APIState<Nothing>()
    object LOADING : APIState<Nothing>()
}