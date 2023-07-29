package com.diamondprize.quizme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.diamondprize.quizme.databinding.ActivityResultBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ResultActivity extends AppCompatActivity implements MaxRewardedAdListener {
    private MaxRewardedAd rewardedAd;
    ActivityResultBinding binding;
    int POINTS = 1;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        long points = (long) correctAnswers * POINTS;
        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));
        binding.earnedCoins.setText(String.valueOf(points));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .update("coins", FieldValue.increment(points));

        //ads vidio applovin
        rewardedAd = MaxRewardedAd.getInstance( "e08450ad506b3f7b", this );
        rewardedAd.setListener(this);
        rewardedAd.loadAd();

        binding.restartBtn.setOnClickListener(v -> {
            if ( rewardedAd.isReady() )
            {
                rewardedAd.showAd();
            }
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finishAffinity();
        });

        binding.shareBtn.setOnClickListener(v -> {
            if ( rewardedAd.isReady() )
            {
                rewardedAd.showAd();
            }
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
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
                    .setPositiveButton("Closed", (dialogInterface, i) -> finish());
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

    @Override
    public void onBackPressed() {
        if ( rewardedAd.isReady() )
        {
            rewardedAd.showAd();
        }
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
        finishAffinity();
    }

    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {
        rewardedAd.loadAd();
    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {

    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

    }
}