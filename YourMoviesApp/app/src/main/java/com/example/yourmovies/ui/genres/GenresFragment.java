package com.example.yourmovies.ui.genres;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourmovies.R;
import com.example.yourmovies.adapters.GenresAdapter;
import com.example.yourmovies.dto.MovieDbGenre;
import com.example.yourmovies.rest.ApiClient;
import com.example.yourmovies.rest.YourMoviesApi;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GenresFragment extends Fragment {

    private YourMoviesApi moviesApi;
    private ArrayList<MovieDbGenre> supportedGenres;
    private ArrayList<MovieDbGenre> favoriteGenres;
    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_genres, container, false);

        final Spinner genresSpinner = root.findViewById(R.id.genre_spinner);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));


        Retrofit client = ApiClient.getClient(getActivity().getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", ""));
        moviesApi = client.create(YourMoviesApi.class);

        moviesApi.getSupportedGenres().enqueue(new Callback<List<MovieDbGenre>>() {
            @Override
            public void onResponse(Call<List<MovieDbGenre>> call, Response<List<MovieDbGenre>> response) {

                ArrayList<String> genres = new ArrayList<>();
                supportedGenres = (ArrayList<MovieDbGenre>) response.body();

                for (MovieDbGenre genre : response.body()) {
                    genres.add(genre.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), R.layout.support_simple_spinner_dropdown_item, genres);
                genresSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<MovieDbGenre>> call, Throwable t) {

            }
        });

        moviesApi.getFavoriteGenres().enqueue(new Callback<List<MovieDbGenre>>() {
            @Override
            public void onResponse(Call<List<MovieDbGenre>> call, Response<List<MovieDbGenre>> response) {
                favoriteGenres = (ArrayList<MovieDbGenre>) response.body();
                recyclerView.setAdapter(new GenresAdapter(root.getContext(), favoriteGenres));
            }

            @Override
            public void onFailure(Call<List<MovieDbGenre>> call, Throwable t) {

            }
        });

        genresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, final int position, final long id) {
                moviesApi.addFavoriteGenre(supportedGenres.get(position)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        moviesApi.getFavoriteGenres().enqueue(new Callback<List<MovieDbGenre>>() {
                            @Override
                            public void onResponse(Call<List<MovieDbGenre>> call, Response<List<MovieDbGenre>> response) {
                                favoriteGenres = (ArrayList<MovieDbGenre>) response.body();
                                Log.d("12hell", "onItemSelected: " + favoriteGenres.size());

                                Map<String, Object> eventParameters = new HashMap<>();
                                eventParameters.put("genre", supportedGenres.get(position).getName());
                                YandexMetrica.reportEvent("GenreAddSuccess", eventParameters);

                                recyclerView.setAdapter(new GenresAdapter(root.getContext(), favoriteGenres));
                            }

                            @Override
                            public void onFailure(Call<List<MovieDbGenre>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });

        return root;
    }
}