package com.diamondprize.quizme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.diamondprize.quizme.databinding.FragmentLeaderboardsBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;


public class LeaderboardsFragment extends Fragment {

    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLeaderboardsBinding binding;
    User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final ArrayList<User> users = new ArrayList<>();
        final LeaderboardsAdapter adapter = new LeaderboardsAdapter(getContext(), users);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //coins
        database.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get().addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    binding.currentCoins.setText(String.valueOf(user.getCoins()));
                    binding.textView11.setText(String.valueOf(user.getName()));
                    binding.currentCoins.setText(user.getCoins() + "");

                });


        //leaderboad coin
        database.collection("users")
                .orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        User user = snapshot.toObject(User.class);
                        users.add(user);
                    }
                    adapter.notifyDataSetChanged();
                });


        // Load an ad into the AdMob banner view.
        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}