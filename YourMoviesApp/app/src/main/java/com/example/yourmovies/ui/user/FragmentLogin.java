package com.example.yourmovies.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yourmovies.MainActivity;
import com.example.yourmovies.R;
import com.example.yourmovies.dto.JwtRequest;
import com.example.yourmovies.dto.JwtResponse;
import com.example.yourmovies.rest.ApiClient;
import com.example.yourmovies.rest.YourMoviesApi;
import com.yandex.metrica.YandexMetrica;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentLogin extends Fragment {

    private EditText username;
    private EditText password;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        username = root.findViewById(R.id.et_username);
        password = root.findViewById(R.id.et_password);
        root.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void login() {
        String un = username.getText().toString();
        String pw = password.getText().toString();
        if (!un.equals("") && !pw.equals("")) {
            Retrofit client = ApiClient.getClient();
            YourMoviesApi moviesApi = client.create(YourMoviesApi.class);
            JwtRequest jwtRequest = new JwtRequest();
            jwtRequest.setUsername(un);
            jwtRequest.setPassword(pw);
            moviesApi.authenticate(jwtRequest).enqueue(new Callback<JwtResponse>() {
                @Override
                public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                    if (response.isSuccessful()) {
                        YandexMetrica.reportEvent("UserLoginSuccess");

                        SharedPreferences prefs = context.getSharedPreferences("token_pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("token", response.body().getToken());
                        edit.apply();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();

                        Map<String, Object> eventParameters = new HashMap<>();
                        eventParameters.put("reason", "login or password is incorrect");
                        YandexMetrica.reportEvent("UserLoginFailed", eventParameters);
                    }
                }

                @Override
                public void onFailure(Call<JwtResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Произошла ошибка при авторизации", Toast.LENGTH_LONG).show();

                    Map<String, Object> eventParameters = new HashMap<>();
                    eventParameters.put("reason", t.getMessage());
                    YandexMetrica.reportEvent("UserLoginFailed", eventParameters);
                }
            });
        }
    }


}
