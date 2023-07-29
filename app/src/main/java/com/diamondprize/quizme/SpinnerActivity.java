package com.diamondprize.quizme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diamondprize.quizme.SpinWheel.LuckyWheelView;
import com.diamondprize.quizme.SpinWheel.model.LuckyItem;
import com.diamondprize.quizme.databinding.ActivitySpinnerBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpinnerActivity extends AppCompatActivity {

    ActivitySpinnerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Load an ad into the AdMob banner view.
        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("spinner").build();
        adView.loadAd(adRequest);



        // koneksi internet
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            Toast.makeText(getApplicationContext(),"Connect With Internet", Toast.LENGTH_LONG).show();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please check your internet connection and try again")
                    .setPositiveButton("Closed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            alert.setCancelable(false);
            alert.show();
        }

        List<LuckyItem> data = new ArrayList<>();

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "1";
        luckyItem1.secondaryText = "COINS";
        luckyItem1.textColor = Color.parseColor("#212121");
        luckyItem1.color = Color.parseColor("#eceff1");
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "35";
        luckyItem2.secondaryText = "COINS";
        luckyItem2.color = Color.parseColor("#00cf00");
        luckyItem2.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "1";
        luckyItem3.secondaryText = "COINS";
        luckyItem3.textColor = Color.parseColor("#212121");
        luckyItem3.color = Color.parseColor("#eceff1");
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "2";
        luckyItem4.secondaryText = "COINS";
        luckyItem4.color = Color.parseColor("#7f00d9");
        luckyItem4.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "1";
        luckyItem5.secondaryText = "COINS";
        luckyItem5.textColor = Color.parseColor("#212121");
        luckyItem5.color = Color.parseColor("#eceff1");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "100";
        luckyItem6.secondaryText = "COINS";
        luckyItem6.color = Color.parseColor("#dc0000");
        luckyItem6.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "1";
        luckyItem7.secondaryText = "COINS";
        luckyItem7.textColor = Color.parseColor("#212121");
        luckyItem7.color = Color.parseColor("#eceff1");
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "0";
        luckyItem8.secondaryText = "COINS";
        luckyItem8.color = Color.parseColor("#008bff");
        luckyItem8.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem8);


        binding.wheelview.setData(data);
        binding.wheelview.setRound(15);

        binding.spinBtn.setOnClickListener(v -> {
            Random r = new Random();
            int randomNumber = r.nextInt(8);
            binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);
        });

        binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                updateCash(index);
            }
        });
    }

    void updateCash(int index) {
        long cash = 0;
        switch (index) {
            case 0:
                cash = 1;
                break;
            case 1:
                cash = 35;
                break;
            case 2:
                cash = 1;
                break;
            case 3:
                cash = 2;
                break;
            case 4:
                cash = 1;
                break;
            case 5:
                cash = 100;
                break;
            case 6:
                cash = 1;
                break;
            case 7:
                cash = 0;
                break;
        }

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database
                .collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .update("coins", FieldValue.increment(cash)).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SpinnerActivity.this, "Coins added in account.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SpinnerActivity.this, ResultSpinnerActivity.class));
                        finish();

                    }
                });

    }

    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

}