package com.diamondprize.quizme;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.diamondprize.quizme.databinding.ActivityBermainMainBinding;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import java.util.ArrayList;

public class BermainMainActivity extends AppCompatActivity {
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBermainMainBinding binding = ActivityBermainMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    //fragment
        ArrayList<CategoryModel> CategoryModel = new ArrayList<>();
        CategoryAdapterPermainan adapter = new CategoryAdapterPermainan(this,CategoryModel);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new MainmainFragment());
        transaction.commit();
// banner fb
        adView = new AdView(this, "627988772663028_746530024142235", AdSize.BANNER_HEIGHT_50);

// Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        adContainer.addView(adView);

// Request an ad
        adView.loadAd();

    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}