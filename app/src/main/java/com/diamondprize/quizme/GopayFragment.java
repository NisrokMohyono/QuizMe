package com.diamondprize.quizme;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class GopayFragment extends Fragment {


    private Button mNextLevelButton;
    private InterstitialAd mInterstitialAd;


    public GopayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    com.diamondprize.quizme.databinding.FragmentGopayBinding binding;
    FirebaseFirestore database;
    User user;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = com.diamondprize.quizme.databinding.FragmentGopayBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get().addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    binding.currentCoins.setText(String.valueOf(user.getCoins()));
                    binding.currentCoins.setText(user.getCoins() + "");

                });


        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final ArrayList<User> users = new ArrayList<>();
        final LeaderboardsAdapterWithdeaws adapter = new LeaderboardsAdapterWithdeaws(getContext(), users);

        // Load an ad into the AdMob banner view.
        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);


        //WEBVIEW
        WebView webView = binding.webView;
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        // Set WebChromeClient untuk menampilkan loading progress
        // Load URL ke WebView
        String url = "https://nisrok-m.blogspot.com/p/e-wallet-gopay.html"; // Ganti dengan URL yang ingin Anda tampilkan
        webView.loadUrl(url);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create the next level button, which tries to show an interstitial when clicked.
        mNextLevelButton = binding.sendRequest;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getActivity().getApplicationContext() == null) return;
        final Context appContext = getActivity().getApplicationContext();

        mNextLevelButton.setEnabled(false);
        mNextLevelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                showInterstitial(appContext);
                if(user.getCoins() > 12000) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    String payPal = binding.emailBox.getText().toString();
                    String jumlahkoin = binding.jumlahkoin.getText().toString();
                    gopay_withdraws request = new gopay_withdraws(uid, payPal, user.getName(), jumlahkoin);
                    database
                            .collection("gopay_withdraws")
                            .document(uid)
                            .set(request).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Request sent successfully.", Toast.LENGTH_SHORT).show());

                } else {
                    Toast.makeText(getContext(), "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        MobileAds.initialize(appContext, initializationStatus -> {
        });
        // Load the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        loadInterstitialAd(appContext);


    }

    private void loadInterstitialAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, getString(R.string.interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mNextLevelButton.setEnabled(true);

                        Toast.makeText(getContext(), "E-WALLET GOPAY", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInterstitialAd = null;

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.

                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        mNextLevelButton.setEnabled(true);

                        String error = String.format(
                                Locale.ENGLISH,
                                "domain: %s, code: %d, message: %s",
                                loadAdError.getDomain(),
                                loadAdError.getCode(),
                                loadAdError.getMessage());
                        Toast.makeText(
                                        getContext(),
                                        "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void showInterstitial(Context context) {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && getActivity() != null) {
            mInterstitialAd.show(getActivity());
        } else {
            Toast.makeText(context, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel(context);
        }
    }

    private void goToNextLevel(Context context) {
        // Show the next level and reload the ad to prepare for the level after.
        if (mInterstitialAd == null) {
            loadInterstitialAd(context);
        }
    }

    //untuk fragment
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}