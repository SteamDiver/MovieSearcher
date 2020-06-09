package com.example.yourmovies.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yourmovies.MainEmptyActivity;
import com.example.yourmovies.R;
import com.yandex.metrica.YandexMetrica;

public class FragmentLogout extends Fragment {
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
        View root = inflater.inflate(R.layout.fragment_logout, container, false);

        root.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void logout() {
        context.getSharedPreferences("token_pref", Context.MODE_PRIVATE).edit().clear().apply();

        YandexMetrica.reportEvent("UserLogout");
        Intent intent = new Intent(getContext(), MainEmptyActivity.class);
        getContext().startActivity(intent);
        getActivity().finish();
    }
}

