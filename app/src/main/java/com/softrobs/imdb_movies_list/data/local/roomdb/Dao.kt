package com.softrobs.imdb_movies_list.data.local.roomdb

import androidx.room.*
import androidx.room.Dao
import com.softrobs.imdb_movies_list.data.remote.response.Search

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun storeAllMovies(getAllMovies: List<Search>)

    @Query("SELECT DISTINCT * FROM getAllMovies ORDER BY id DESC")
    suspend fun getAllMoviesDetails(): List<Search>

    @Query("DELETE FROM getAllMovies")
    suspend fun removeOldData()

}