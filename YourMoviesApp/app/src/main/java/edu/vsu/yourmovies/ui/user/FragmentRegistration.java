package java.edu.vsu.yourmovies.ui.user;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourmovies.R;
import java.edu.vsu.yourmovies.dto.UserDTO;
import java.edu.vsu.yourmovies.rest.ApiClient;
import java.edu.vsu.yourmovies.rest.YourMoviesApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentRegistration extends Fragment {

    private EditText username;
    private EditText password;
    private EditText repassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        username = root.findViewById(R.id.et_username);
        password = root.findViewById(R.id.et_password);
        repassword = root.findViewById(R.id.et_repassword);
        root.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        return root;
    }

    public void register() {
        String un = username.getText().toString();
        String pw = password.getText().toString();
        String rpw = repassword.getText().toString();

        if (!un.equals("") && !pw.equals("") && !rpw.equals("")) {
            if (rpw.equals(pw)) {
                Retrofit client = ApiClient.getClient();
                YourMoviesApi moviesApi = client.create(YourMoviesApi.class);
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(un);
                userDTO.setPassword(pw);
                moviesApi.register(userDTO).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("DEB", "onResponse: SUCCESFULL REGISTER");
                        Fragment fragment = new FragmentLogin();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.login_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            } else {

            }
        }
    }

}
