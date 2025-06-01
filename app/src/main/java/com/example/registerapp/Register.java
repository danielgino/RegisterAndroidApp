package com.example.registerapp;
import android.annotation.SuppressLint;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.BreakIterator;


public class Register extends Fragment {

    private EditText editTextUsername, editTextPassword,editName, editTextPhone, editTextAddress;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public Register() {

    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editName=view.findViewById(R.id.editName);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        buttonRegister = view.findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        buttonRegister.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        String email = editTextUsername.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "Please Fill All fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(email,name, phone, address);
                        usersRef.child(uid).setValue(user);
                        Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new Login())
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Error on Register" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
