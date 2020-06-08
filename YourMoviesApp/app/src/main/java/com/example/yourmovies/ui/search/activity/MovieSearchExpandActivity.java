package com.example.yourmovies.ui.search.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourmovies.R;
import com.example.yourmovies.dto.MovieDTO;
import com.example.yourmovies.rest.ApiClient;
import com.example.yourmovies.rest.YourMoviesApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieSearchExpandActivity extends AppCompatActivity {

    private ImageView poster;
    private TextView title, stats, description;
    private YourMoviesApi moviesApi;
    private RelativeLayout loadingPanel;
    private ScrollView scrlView;

    private void getData() {
        if (getIntent().hasExtra("id")) {
            moviesApi.getMovieInfo(getIntent().getIntExtra("id", 0)).enqueue(new Callback<MovieDTO>() {
                @Override
                public void onResponse(Call<MovieDTO> call, Response<MovieDTO> response) {

                    //TODO something wrong here
                    if (response.body().getPoster() != null) {
                        byte[] decodedPoster = Base64.decode(response.body().getPoster(), Base64.DEFAULT);
                        poster.setImageBitmap(BitmapFactory.decodeByteArray(decodedPoster, 0, decodedPoster.length));
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.noimage);
                        poster.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 342, 513, false));
                    }

                    title.setText(response.body().getTitle());
                    description.setText(response.body().getOverview());

                    String release = "Дата выхода: " + response.body().getReleaseDate();
                    String revenue = "Кассовые сборы: " + (response.body().getRevenue() != 0 ? String.valueOf(response.body().getRevenue()) : "неизвестно");
                    String voteAverage = "Оценка: " + (response.body().getVoteAverage() != 0 ? String.valueOf(response.body().getVoteAverage()) : "неизвестно");
                    String isAdult = "Для взрослых: " + (response.body().isAdult() ? "Да" : "Нет");

                    String movieInfo = release + "\n" + revenue + "\n" + voteAverage + "\n" + isAdult;
                    stats.setText(movieInfo);

                    loadingPanel.setVisibility(View.GONE);
                    scrlView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MovieDTO> call, Throwable t) {

                }
            });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_search_expand);


        loadingPanel = findViewById(R.id.loadingPanel);
        scrlView = findViewById(R.id.scrl);
        scrlView.setVisibility(View.GONE);

        loadingPanel.setVisibility(View.VISIBLE);

        poster = findViewById(R.id.moviePoster);
        title = findViewById(R.id.movieTitle);
        stats = findViewById(R.id.movieStats);
        description = findViewById(R.id.movieDescription);

        Retrofit client = ApiClient.getClient(getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", ""));
        moviesApi = client.create(YourMoviesApi.class);

        getData();
    }

    public void addMovie(final View view) {
        int id = getIntent().getIntExtra("id", 0);
        MovieDTO dto = new MovieDTO();
        dto.setId(id);
        moviesApi.addMovieToDiary(dto).enqueue(new Callback<MovieDTO>() {
            @Override
            public void onResponse(Call<MovieDTO> call, Response<MovieDTO> response) {
                Toast.makeText(view.getContext(), "Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MovieDTO> call, Throwable t) {
                Log.d("DEB", "FAILURE");
            }
        });
    }
}