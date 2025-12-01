package com.example.nct_lite.data.genre

/**
 * Wraps [GenreApi] to centralize remote interactions for the genre domain.
 */
class GenreRemoteDataSource(private val api: GenreApi) {

    suspend fun getGenres() = api.getGenres()

//    suspend fun getGenreById(id: String) = api.getGenreById(id)
}
