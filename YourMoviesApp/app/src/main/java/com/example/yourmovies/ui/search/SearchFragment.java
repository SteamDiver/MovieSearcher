package com.example.yourmovies.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yourmovies.R;
import com.example.yourmovies.adapters.MovieSearchAdapter;
import com.example.yourmovies.dto.MovieDTO;
import com.example.yourmovies.rest.ApiClient;
import com.example.yourmovies.rest.YourMoviesApi;
import com.yandex.metrica.YandexMetrica;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    private View root;
    private YourMoviesApi moviesApi;
    private String queryField;
    private List<MovieDTO> movies;
    private EditText searchField;
    private RecyclerView recyclerView;
    private SharedPreferences preferences;
    private Bundle state;
    private RelativeLayout loadingPanel;
    private LinearLayout loadingPanel2;

    private void search(final String query) {
        Log.d("DEB", "search: " + query);

        loadingPanel2.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.VISIBLE);


        queryField = searchField.getText().toString();
        moviesApi.searchMovie(1, searchField.getText().toString())
                .enqueue(new Callback<List<MovieDTO>>() {
                    @Override
                    public void onResponse(Call<List<MovieDTO>> call, Response<List<MovieDTO>> response) {
                        loadingPanel.setVisibility(View.GONE);
                        movies = response.body();

                        if (movies == null || movies.size() == 0) {
                            loadingPanel2.setVisibility(View.VISIBLE);

                        } else {
                            Map<String, Object> eventParameters = new HashMap<String, Object>();
                            eventParameters.put("query", query);
                            eventParameters.put("moviesCount", movies.size());
                            YandexMetrica.reportEvent("SearchSuccess", eventParameters);
                            recyclerView.setAdapter(new MovieSearchAdapter(root.getContext(), response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MovieDTO>> call, Throwable t) {
                        loadingPanel.setVisibility(View.GONE);

                        Log.d("DEB", "onResponse: " + t.getMessage());

                        Map<String, Object> eventParameters = new HashMap<String, Object>();
                        eventParameters.put("reason", t.getMessage());
                        YandexMetrica.reportEvent("SearchFailed", eventParameters);
                    }
                });
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        loadingPanel = root.findViewById(R.id.loadingPanel);
        loadingPanel2 = root.findViewById(R.id.notFound);
        loadingPanel.setVisibility(View.GONE);
        loadingPanel2.setVisibility(View.GONE);
        searchField = root.findViewById(R.id.search_field);

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search(searchField.getText().toString());
                }
                return false;
            }
        });


        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));


        Log.d("DEB", "onCreateView: " + getActivity().getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", ""));

        Retrofit client = ApiClient.getClient(getActivity().getSharedPreferences("token_pref", Context.MODE_PRIVATE).getString("token", ""));
        moviesApi = client.create(YourMoviesApi.class);

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
    public void onPause() {
        super.onPause();
        Log.d("DEB", "PAUSE");
        if (movies != null && movies.size() != 0) {
            state = new Bundle();
            state.putSerializable("searchMovies", (Serializable) movies);
            state.putString("searchQuery", queryField);
        }
    }
}