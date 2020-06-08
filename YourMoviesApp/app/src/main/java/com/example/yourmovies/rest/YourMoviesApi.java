package com.example.yourmovies.rest;

import com.example.yourmovies.dto.JwtRequest;
import com.example.yourmovies.dto.JwtResponse;
import com.example.yourmovies.dto.MovieDTO;
import com.example.yourmovies.dto.MovieDbGenre;
import com.example.yourmovies.dto.UserDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YourMoviesApi {
    @GET("yourmovies/search/movie/{page}")
    Call<List<MovieDTO>> searchMovie(@Path("page") int page, @Query("query") String query);

    @GET("yourmovies/recommendation/movie/{page}")
    Call<List<MovieDTO>> getRecommendation(@Path("page") int page, @Query("genres") List<Integer> genres);

    @GET("yourmovies/movie/info/{movieId}")
    Call<MovieDTO> getMovieInfo(@Path("movieId") int movieId);

    @GET("yourmovies/movie/{movieId}")
    Call<MovieDTO> getDiaryMovie(@Path("movieId") int movieId);

    @POST("yourmovies/movie")
    Call<MovieDTO> addMovieToDiary(@Body MovieDTO movie);

    @PUT("yourmovies/movie")
    Call<MovieDTO> addMovieNote(@Body MovieDTO movie);

    @GET("yourmovies/movie")
    Call<List<MovieDTO>> getDiaryMovies();


    @GET("yourmovies/genre/all")
    Call<List<MovieDbGenre>> getSupportedGenres();

    @GET("yourmovies/genre")
    Call<List<MovieDbGenre>> getFavoriteGenres();

    @POST("yourmovies/genre")
    Call<ResponseBody> addFavoriteGenre(@Body MovieDbGenre genre);

    @HTTP(method = "DELETE", path = "yourmovies/genre", hasBody = true)
    Call<ResponseBody> removeFavoriteGenre(@Body MovieDbGenre genre);

    @POST("user/register")
    Call<ResponseBody> register(@Body UserDTO user);

    @POST("user/authenticate")
    Call<JwtResponse> authenticate(@Body JwtRequest authenticationRequest);
}
