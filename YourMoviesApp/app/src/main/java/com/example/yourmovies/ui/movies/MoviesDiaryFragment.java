package com.example.yourmovies.ui.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourmovies.R;
import com.example.yourmovies.adapters.MovieDiaryAdapter;
import com.example.yourmovies.adapters.MovieSearchAdapter;
import com.example.yourmovies.dto.MovieDTO;
import com.example.yourmovies.rest.ApiClient;
import com.example.yourmovies.rest.YourMoviesApi;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MoviesDiaryFragment extends Fragment {

    private View root;
    private YourMoviesApi moviesApi;
    private String queryField;
    private List<MovieDTO> movies;
    private EditText searchField;
    private RecyclerView recyclerView;
    private SharedPreferences preferences;
    private Bundle state;
    private RelativeLayout loadingPanel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_movies, container, false);

        loadingPanel = root.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        Retrofit client = ApiClient.getClient(getActivity().getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", ""));
        moviesApi = client.create(YourMoviesApi.class);

        moviesApi.getDiaryMovies().enqueue(new Callback<List<MovieDTO>>() {
            @Override
            public void onResponse(Call<List<MovieDTO>> call, Response<List<MovieDTO>> response) {
                loadingPanel.setVisibility(View.GONE);

                movies = response.body();
                if (movies == null || movies.size() == 0) {
                } else {
                    recyclerView.setAdapter(new MovieDiaryAdapter(root.getContext(), response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<MovieDTO>> call, Throwable t) {
                loadingPanel.setVisibility(View.GONE);
            }
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recyclerView.setAdapter(new MovieSearchAdapter(root.getContext(), (List<MovieDTO>) savedInstanceState.getSerializable("searchMovies")));
            searchField.setText((String) savedInstanceState.getSerializable("searchQuery"));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("searchMovies", (Serializable) movies);
        outState.putString("searchQuery", queryField);
    }
}