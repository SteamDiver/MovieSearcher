package com.example.yourmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourmovies.R;
import com.example.yourmovies.dto.MovieDbGenre;
import com.example.yourmovies.rest.ApiClient;
import com.example.yourmovies.rest.YourMoviesApi;
import com.example.yourmovies.ui.genres.activity.MovieRecommendationActivity;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenreViewHolder> {

    private Context context;
    private List<MovieDbGenre> genres;

    public GenresAdapter(Context context, List<MovieDbGenre> genres) {
        this.context = context;

        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreViewHolder(LayoutInflater.from(context).inflate(R.layout.genre_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, final int position) {
        holder.title.setText(genres.get(position).getName());

        holder.genresLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieRecommendationActivity.class);
                intent.putExtra("id", genres.get(position).getId());
                intent.putExtra("title", genres.get(position).getName());
                context.startActivity(intent);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit client = ApiClient.getClient(context.getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", ""));
                YourMoviesApi moviesApi = client.create(YourMoviesApi.class);
                moviesApi.removeFavoriteGenre(genres.get(position)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        genres.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, 1);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ConstraintLayout genresLayout;
        Button button;


        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.genre_name);
            button = itemView.findViewById(R.id.removeGenreBtn);
            genresLayout = itemView.findViewById(R.id.genresLayout);
        }
    }
}
