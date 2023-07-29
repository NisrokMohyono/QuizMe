package com.diamondprize.quizme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.diamondprize.quizme.databinding.FragmentWalletBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class WalletFragment extends Fragment {

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    User user;
   public Spinner spinner;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get().addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    binding.currentCoins.setText(String.valueOf(user.getCoins()));

                    //binding.currentCoins.setText(user.getCoins() + "");

                });

        binding.sendRequest.setOnClickListener(v -> {
            if(user.getCoins() > 1000) {
                String uid = FirebaseAuth.getInstance().getUid();
                String payPal = binding.emailBox.getText().toString();
                String jumlahkoin = binding.jumlahkoin.getText().toString();
                String permainan = binding.namagames.getText().toString();
                String idplayer = binding.idgame.getText().toString();
                WithdrawRequest request = new WithdrawRequest(uid, payPal, user.getName(),jumlahkoin, permainan , idplayer);
                database
                        .collection("withdraws")
                        .document(uid)
                        .set(request).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Request sent successfully.", Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(getContext(), "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
            }

        });


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}