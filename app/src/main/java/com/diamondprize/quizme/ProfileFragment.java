package com.diamondprize.quizme;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diamondprize.quizme.databinding.FragmentProfileBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentProfileBinding binding;
    FirebaseFirestore database;
    User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get().addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    binding.EmailAddress.setText(String.valueOf(user.getEmail()));
                    binding.nameBox.setText(String.valueOf(user.getName()));
                    binding.namePass.setText(String.valueOf(user.getPass()));
                    binding.nameCoin.setText(String.valueOf(user.getCoins()));
                    binding.nameCoinrp.setText(String.valueOf(user.getCoins()));

                    //binding.currentCoins.setText(user.getCoins() + "");

                });



        binding.sendKeluar.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));


        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load an ad into the AdMob banner view.
        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}