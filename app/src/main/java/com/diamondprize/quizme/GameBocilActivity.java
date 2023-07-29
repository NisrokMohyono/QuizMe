package com.diamondprize.quizme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class GameBocilActivity extends AppCompatActivity {
    private WebView webView;
    private FirebaseFirestore firestore;
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    private Button delayedButton;
    int POINTS = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_bocil);

        //tambah coin
        int correctAnswers = getIntent().getIntExtra("correct", 1);
        long points = (long) correctAnswers * POINTS;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
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

        // Memeriksa koneksi internet
        if (!NetworkUtil.isInternetConnected(this)) {
            // Tampilkan pesan peringatan jika tidak tersambung

        }

        String catId = getIntent().getStringExtra("catId");
        firestore = FirebaseFirestore.getInstance();
        // Dapatkan URL dari Firestore berdasarkan catId
        firestore.collection("urlpermainan")
                .document(catId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String url = document.getString("url");
                                loadWebView(url);
                            } else {
                                // Dokumen tidak ditemukan di Firestore
                            }
                        } else {
                            // Penanganan kesalahan ketika tidak dapat mengambil data dari Firestore
                            Exception e = task.getException();
                            if (e != null) {
                                // Tampilkan pesan kesalahan atau lakukan tindakan lain yang sesuai
                            }
                        }
                    }
                });

        delayedButton = findViewById(R.id.delayedButton);
        delayedButton.setVisibility(View.INVISIBLE);
        // Ubah menjadi INVISIBLE agar tombol tidak terlihat awalnya

        // Mengatur handler untuk menampilkan tombol setelah jeda waktu tertentu (misalnya, 30 detik)
        int delayMillis = 30000; // 30 detik dalam milidetik
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Menampilkan tombol setelah jeda waktu
                delayedButton.setVisibility(View.VISIBLE);
            }
        }, delayMillis);

        // Memberikan onClickListener untuk tombol
        delayedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Menyembunyikan tombol ketika diklik
                delayedButton.setVisibility(View.INVISIBLE);
                showCongratulationsDialog();

                //show vidio admob
                if (mRewardedAd != null) {
                    Activity activityContext = GameBocilActivity.this;
                    mRewardedAd.show(activityContext, rewardItem -> {
                        // Handle the reward.
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                    });
                }
                // coin bertambah klik button
                database.collection("users")
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .update("coins", FieldValue.increment(points));
            }

        });

        //iklan banner admob
        FrameLayout adContainerView = findViewById(R.id.ad_view);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adContainerView.addView(adView);
        loadBanner();
        //admob Interstitial
        InterstitialAd.load(this,getString(R.string.interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });

    }

    //dialog button
    private void showCongratulationsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selamat!");
        builder.setMessage("Coin Anda telah sukses di tambahkan !");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Tindakan yang ingin dilakukan setelah tombol OK diklik
                dialog.dismiss(); // Menutup dialog
            }
        });
        builder.show();
    }

    //web view url
    private void loadWebView(String url) {
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

    }
    //iklan banner admob
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
    private AdView adView;

    @Override
    public void onBackPressed() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(GameBocilActivity.this);
        }
        super.onBackPressed();
    }
}
