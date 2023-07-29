package com.diamondprize.quizme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diamondprize.quizme.databinding.ActivityResultSpinnerBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.Objects;

public class ResultSpinnerActivity extends AppCompatActivity {
    ActivityResultSpinnerBinding binding;
    int POINTS = 5;
    private WebView webView;
    private RewardedAd mRewardedAd;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_spinner);

        //ads vidio admob
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this,  getString(R.string.reword_ad_unit_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;

                    }
                });

        binding = ActivityResultSpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int correctAnswers = getIntent().getIntExtra("correct", 1);
        long points = (long) correctAnswers * POINTS;

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        binding.restartBtn.setOnClickListener(v -> {
            binding = ActivityResultSpinnerBinding.inflate(getLayoutInflater());
            database.collection("users")
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .update("coins", FieldValue.increment(points));

            startActivity(new Intent(ResultSpinnerActivity.this, MainActivity.class));
            //show vidio admob
            if (mRewardedAd != null) {
                Activity activityContext = this;
                mRewardedAd.show(activityContext, rewardItem -> {
                    // Handle the reward.
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                });
            }
        });

        binding.shareBtn.setOnClickListener(v -> {
            StartAppAd.showAd(this);
            startActivity(new Intent(ResultSpinnerActivity.this, SpinnerActivity.class));
            finishAffinity();
        });

        // koneksi internet
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

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
        //iklan banner admob
        FrameLayout adContainerView = findViewById(R.id.ad_view);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adContainerView.addView(adView);
        loadBanner();

    }
    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}