package java.edu.vsu.yourmovies.ui.genres.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourmovies.R;
import java.edu.vsu.yourmovies.adapters.MovieSearchAdapter;
import java.edu.vsu.yourmovies.dto.MovieDTO;
import java.edu.vsu.yourmovies.rest.ApiClient;
import java.edu.vsu.yourmovies.rest.YourMoviesApi;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRecommendationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<MovieDTO> movies;
    private Context context;
    private RelativeLayout loadingPanel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movies);

        if (savedInstanceState != null) {
            recyclerView.setAdapter(new MovieSearchAdapter(this, (List<MovieDTO>) savedInstanceState.getSerializable("searchMovies")));
        }

        loadingPanel = findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = this;

        YourMoviesApi moviesApi = ApiClient.getClient(getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", "")).create(YourMoviesApi.class);
        if (getIntent().hasExtra("id")) {
            setTitle(getIntent().getStringExtra("title"));
            moviesApi.getRecommendation(1, Collections.singletonList(getIntent().getIntExtra("id", 0)))
                    .enqueue(new Callback<List<MovieDTO>>() {
                        @Override
                        public void onResponse(Call<List<MovieDTO>> call, Response<List<MovieDTO>> response) {
                            loadingPanel.setVisibility(View.GONE);

                            movies = response.body();
                            recyclerView.setAdapter(new MovieSearchAdapter(context, response.body()));
                        }

                        @Override
                        public void onFailure(Call<List<MovieDTO>> call, Throwable t) {
                            loadingPanel.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (movies != null && movies.size() != 0) {
            Bundle state = new Bundle();
            state.putSerializable("searchMovies", (Serializable) movies);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("searchMovies", (Serializable) movies);
    }
}
