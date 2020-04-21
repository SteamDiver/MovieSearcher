package java.edu.vsu.yourmovies.ui.user;

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

import java.edu.vsu.yourmovies.MainActivity;
import com.example.yourmovies.R;
import java.edu.vsu.yourmovies.dto.JwtRequest;
import java.edu.vsu.yourmovies.dto.JwtResponse;
import java.edu.vsu.yourmovies.rest.ApiClient;
import java.edu.vsu.yourmovies.rest.YourMoviesApi;

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
                        SharedPreferences prefs = context.getSharedPreferences("token_pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("token", response.body().getToken());
                        edit.apply();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Login or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JwtResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), "Login or password is incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
