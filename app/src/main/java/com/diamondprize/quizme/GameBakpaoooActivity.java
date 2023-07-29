package com.diamondprize.quizme;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class GameBakpaoooActivity extends AppCompatActivity {
    private RewardedAd mRewardedAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_bakpaooo);
        WebView mWebView = findViewById(R.id.activity_main_webview);
        mWebView.loadUrl("https://quiz-study-again.web.app/games/baozi/");
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        AdView mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RewardedAd.load(this, getString(R.string.reword_ad_unit_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                    }
                });


    }
    @Override
    public void onBackPressed() {
        if (mRewardedAd != null) {
            Activity activityContext = GameBakpaoooActivity.this;
            mRewardedAd.show(activityContext, rewardItem -> {
                Log.d(TAG, "Selamat Rewards telah Diterima.");
                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();
            });
        }
        super.onBackPressed();
    }
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }
}