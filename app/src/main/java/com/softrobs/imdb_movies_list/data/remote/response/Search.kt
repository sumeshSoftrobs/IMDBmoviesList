package com.softrobs.imdb_movies_list.data.remote.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName ="getAllMovies",
    indices = [Index(value = ["imdbId"], unique = true)])
data class Search(

    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    @ColumnInfo(name = "imdbId")
    val imdbID: String
)