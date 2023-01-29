package com.softrobs.imdb_movies_list.data.remote.apiStateManagement

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response

object GetResponse {
    fun <T> fromFlow(safeAPICall: suspend () -> Response<T>): Flow<APIState<T>> = flow {
        emit(APIState.LOADING)
        try {
            val response = safeAPICall()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(APIState.SUCCESS(it))
                }
            } else {
                response.errorBody()?.let { error ->
                    error.close()
                    emit(APIState.ERROR(message = error.toString(),
                        isNetworkError = false,
                        errorCode = response.code(),
                        errorBody = response.errorBody()))
                }
            }
        }catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    Log.d("EXCEPTION_FROM_LOGIN_API" , throwable.code().toString())

                    APIState.ERROR(message = throwable.message!!,
                        isNetworkError = false,
                        errorCode = throwable.code(),
                        errorBody = throwable.response()?.errorBody())
                }
                else -> {
                    Log.d("EXCEPTION_FROM_LOGIN_API" , throwable.message!!.toString())

                    APIState.ERROR(message = throwable.message!!,
                        isNetworkError = true,
                        errorCode = null,
                        errorBody = null)
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}