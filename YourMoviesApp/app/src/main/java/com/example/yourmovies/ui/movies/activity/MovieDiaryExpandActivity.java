package com.example.yourmovies.ui.movies.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yourmovies.R;
import com.example.yourmovies.dto.MovieDTO;
import com.example.yourmovies.rest.ApiClient;
import com.example.yourmovies.rest.YourMoviesApi;
import com.yandex.metrica.YandexMetrica;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDiaryExpandActivity extends AppCompatActivity {

    private ImageView poster;
    private TextView title, stats;
    private Retrofit client;
    private EditText description;
    private YourMoviesApi moviesApi;
    private RelativeLayout loadingPanel;
    private ScrollView scrlView;

    private void getData() {
        if (getIntent().hasExtra("id")) {
            moviesApi.getDiaryMovie(getIntent().getIntExtra("id", 0)).enqueue(new Callback<MovieDTO>() {
                @Override
                public void onResponse(Call<MovieDTO> call, Response<MovieDTO> response) {
                    if (response.body().getPoster() != null) {
                        byte[] decodedPoster = Base64.decode(response.body().getPoster(), Base64.DEFAULT);
                        Bitmap bm = BitmapFactory.decodeByteArray(decodedPoster, 0, decodedPoster.length);
                        poster.setImageBitmap(Bitmap.createScaledBitmap(bm, 320, 400, false));
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.noimage);
                        poster.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 342, 513, false));
                    }

                    title.setText(response.body().getTitle());
                    description.setText(response.body().getNote() != null ? response.body().getNote() : "Ваше мнение");

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
        setContentView(R.layout.fragment_movie_diary_expand);

        loadingPanel = findViewById(R.id.loadingPanel);
        scrlView = findViewById(R.id.scrl);
        scrlView.setVisibility(View.GONE);

        loadingPanel.setVisibility(View.VISIBLE);

        poster = findViewById(R.id.moviePoster);
        title = findViewById(R.id.movieTitle);
        stats = findViewById(R.id.movieStats);
        description = findViewById(R.id.movieDescription);

        client = ApiClient.getClient(getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", ""));
        moviesApi = client.create(YourMoviesApi.class);
        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        getData();
    }

    public void addMovieNote(View view) {
        int id = getIntent().getIntExtra("id", 0);
        MovieDTO dto = new MovieDTO();
        dto.setId(id);
        dto.setNote(description.getText().toString());
        final Map<String, Object> eventParameters = new HashMap<>();
        eventParameters.put("movie", id);
        moviesApi.addMovieNote(dto).enqueue(new Callback<MovieDTO>() {
            @Override
            public void onResponse(Call<MovieDTO> call, Response<MovieDTO> response) {
                Log.d("DEB", "ADDED");
                YandexMetrica.reportEvent("MovieNoteAddSuccess", eventParameters);
            }

            @Override
            public void onFailure(Call<MovieDTO> call, Throwable t) {
                Log.d("DEB", "FAILURE");
                Map<String, Object> eventParameters = new HashMap<>();
                eventParameters.put("reason", t.getMessage());
                YandexMetrica.reportEvent("MovieNoteAddFailed", eventParameters);
            }
        });
    }
}