package com.diamondprize.quizme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.diamondprize.quizme.databinding.ActivityMainBinding;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MaxRewardedAdListener {

    ActivityMainBinding binding;

    private MaxRewardedAd rewardedAd;
    private int           retryAttempt;
    private RewardedAd mRewardedAd;
    private MaxAdView adView;

    FirebaseFirestore database;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sdk admob
        MobileAds.initialize(this, initializationStatus -> {
        });

        //sdk applovin
        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, configuration -> {
            // AppLovin SDK is initialized, start loading ads
        });
        //sdk fb
        AudienceNetworkAds.initialize(this);

        setSupportActionBar(binding.toolbar);
        StartAppSDK.init(this, getString(R.string.Start_io), true);


        ArrayList<CategoryModel> categories = new ArrayList<>();
        CategoryAdapter adapter = new CategoryAdapter(this,categories);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new HomeFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(i -> {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            switch (i) {
                case 0:
                    transaction1.replace(R.id.content, new HomeFragment());
                    transaction1.commit();
                    break;
                case 1:

                    if (mRewardedAd != null) {
                        Activity activityContext = MainActivity.this;
                        mRewardedAd.show(activityContext, rewardItem -> {
                            // Handle the reward.
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        });
                    }

                    transaction1.replace(R.id.content, new LeaderboardsFragment());
                    transaction1.commit();
                    break;
                case 2:
                    transaction1.replace(R.id.content, new ProfileFragment());
                    transaction1.commit();
                    break;
                case 3:
                    transaction1.replace(R.id.content, new BlankFragment());
                    transaction1.commit();
                    break;
            }
            return false;
        });


        //ads vidio admob
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.reword_ad_unit_id),
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

        //ads vidio applovin
        rewardedAd = MaxRewardedAd.getInstance( "e08450ad506b3f7b", this );
        rewardedAd.setListener(this);
        rewardedAd.loadAd();


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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wd_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.wdwallet) {
            if ( rewardedAd.isReady() )
            {
                rewardedAd.showAd();
            }
            Intent intent =new Intent(MainActivity.this,  KumpulanGameActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    //ads vidio applovin

    @Override
    public void onRewardedVideoStarted(MaxAd ad) {

    }

    @Override
    public void onRewardedVideoCompleted(MaxAd ad) {

    }

    @Override
    public void onUserRewarded(MaxAd ad, MaxReward reward) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {
        // rewarded ad is hidden. Pre-load the next ad
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


    protected void onDestroy(){
        super.onDestroy();
        finish();
    }



}