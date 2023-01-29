package com.softrobs.imdb_movies_list.data.remote.response

data class MovieSearchDataModel(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)