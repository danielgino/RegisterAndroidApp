package com.example.registerapp;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class Profile extends Fragment {

    private EditText editTextDescription;
    private Button buttonSave, buttonLoad;
    private TextView textViewResult;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    public Profile() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);

        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonLoad = view.findViewById(R.id.buttonLoad);
        textViewResult = view.findViewById(R.id.textViewResult);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        buttonSave.setOnClickListener(v -> {
            String description = editTextDescription.getText().toString().trim();
            if (TextUtils.isEmpty(description)) {
                Toast.makeText(getContext(), "Please Enter a Description", Toast.LENGTH_SHORT).show();
                return;
            }

            userRef.child("description").setValue(description)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Description Saved!", Toast.LENGTH_SHORT).show();
                        editTextDescription.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error Saving " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        buttonLoad.setOnClickListener(v -> {
            userRef.child("description").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String desc = snapshot.getValue(String.class);
                        textViewResult.setText(desc);
                    } else {
                        textViewResult.setText("No Description yet");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error:  " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        return view;
    }
}
