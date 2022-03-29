package com.example.movie2retrofit.api

import com.example.movie2retrofit.castModel.castModel
import com.example.movie2retrofit.model.Movies
import com.example.movie2retrofit.modelDetail.movieDetails
import com.example.movie2retrofit.trailerModel.trailerModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("3/movie/popular")
    fun getMovies(@Query("api_key")key: String): Call<Movies>

    @GET("3/movie/{movie_id}")
    fun getMoviesDetails(@Path("movie_id")movieId: Int,@Query("api_key")key: String): Call<movieDetails>

    @GET("3/movie/{movie_id}/credits")
    fun getCast(@Path("movie_id")movieId: Int,@Query("api_key")key: String): Call<castModel>

    @GET("3/movie/{movie_id}/videos")
    fun getTrailer(@Path("movie_id")movieId: Int,@Query("api_key")key: String): Call<trailerModel>

    companion object {
        var BASE_URL = "https://api.themoviedb.org/"
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}
