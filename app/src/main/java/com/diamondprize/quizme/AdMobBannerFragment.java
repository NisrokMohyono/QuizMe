package com.diamondprize.quizme;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diamondprize.quizme.databinding.FragmentAdmobBannerBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdMobBannerFragment extends Fragment {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

    private FragmentAdmobBannerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAdmobBannerBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load an ad into the AdMob banner view.
        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getActivity().getApplicationContext() == null) return;
        final Context appContext = getActivity().getApplicationContext();
        // Toasts the test ad message on the screen.
        // Remove this after defining your own ad unit ID.
        Toast.makeText(appContext, TOAST_TEXT, Toast.LENGTH_LONG).show();
    }


}