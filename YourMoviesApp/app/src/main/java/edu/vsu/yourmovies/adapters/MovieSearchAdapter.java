package java.edu.vsu.yourmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourmovies.R;
import java.edu.vsu.yourmovies.dto.MovieDTO;
import java.edu.vsu.yourmovies.ui.search.activity.MovieSearchExpandActivity;

import java.util.List;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.MovieViewHolder> {

    private Context context;
    private List<MovieDTO> movies;

    public MovieSearchAdapter(Context context, List<MovieDTO> movies) {
        this.context = context;

        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {
        holder.title.setText(movies.get(position).getTitle());
        holder.description.setText(movies.get(position).getOverview());
        final Bitmap poster;

        if (movies.get(position).getPoster() != null) {
            byte[] decodedPoster = Base64.decode(movies.get(position).getPoster(), Base64.DEFAULT);
            poster = BitmapFactory.decodeByteArray(decodedPoster, 0, decodedPoster.length);
            holder.movieImage.setImageBitmap(poster);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.noimage);
            poster = Bitmap.createScaledBitmap(bitmap, 342, 513, false);
            holder.movieImage.setImageBitmap(poster);
        }

        holder.moviesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieSearchExpandActivity.class);
                intent.putExtra("id", movies.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        ImageView movieImage;
        ConstraintLayout moviesLayout;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_txt);
            description = itemView.findViewById(R.id.description_txt);
            movieImage = itemView.findViewById(R.id.movie_image_view);
            moviesLayout = itemView.findViewById(R.id.moviesLayout);
        }
    }
}
