package com.diamondprize.quizme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diamondprize.quizme.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new account...");


        binding.createNewBtn.setOnClickListener(v -> {
            String email, pass, name, referCode;

            email = binding.emailBox.getText().toString();
            pass = binding.passwordBox.getText().toString();
            name = binding.nameBox.getText().toString();
            referCode = binding.referBox.getText().toString();

            final User user = new User(name, email, pass, referCode);

            dialog.show();
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();

                    database
                            .collection("users")
                            .document(uid)
                            .set(user).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()) {
                                    dialog.dismiss();
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, Objects.requireNonNull(task1.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    dialog.dismiss();
                    Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            });


        });
        binding.loginBtn.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));


    }


}