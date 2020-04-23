package java.edu.vsu.yourmovies.ui.genres;

import android.content.Context;
import android.os.Bundle;
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
import java.edu.vsu.yourmovies.adapters.GenresAdapter;
import java.edu.vsu.yourmovies.dto.MovieDbGenre;
import java.edu.vsu.yourmovies.rest.ApiClient;
import java.edu.vsu.yourmovies.rest.YourMoviesApi;

import java.util.ArrayList;
import java.util.List;

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
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                moviesApi.addFavoriteGenre(supportedGenres.get(position)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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