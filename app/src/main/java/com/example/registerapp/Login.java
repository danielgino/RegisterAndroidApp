package com.example.registerapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends Fragment {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonGoToRegister;
    private FirebaseAuth mAuth;

    public Login() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonGoToRegister = view.findViewById(R.id.buttonGoToRegister);

        mAuth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(v -> loginUser());

        buttonGoToRegister.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new Register())
                    .commit();
        });

        return view;
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please fill al fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Login Successfully!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new Profile())
                                .commit();
                    } else {
                        Toast.makeText(getContext(),
                                "The user does not appear to exist or the details entered are incorrect.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
