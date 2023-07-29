package com.diamondprize.quizme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.diamondprize.quizme.databinding.FragmentBlankBinding;
import com.startapp.sdk.adsbase.StartAppAd;


public class BlankFragment extends Fragment {
    public BlankFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentBlankBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBlankBinding.inflate(inflater, container, false);

        binding.pes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:@nisrokmohyono@gmail.com"));
                startActivity(intent);
                StartAppAd.showAd(requireContext().getApplicationContext());
            }
        });

        binding.fb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/nisrokmohyono"));
                startActivity(intent);
                StartAppAd.showAd(requireContext().getApplicationContext());
            }
        });

        binding.ig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                StartAppAd.showAd(requireContext().getApplicationContext());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/nisrokmohyono"));
                startActivity(intent);
            }
        });

        binding.lkd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                StartAppAd.showAd(requireContext().getApplicationContext());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/nisrokmohyono/"));
                startActivity(intent);
            }
        });

        binding.tele.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Nisrokmohyono"));
                startActivity(intent);
            }
        });

        binding.ptr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                StartAppAd.showAd(requireContext().getApplicationContext());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://id.pinterest.com/NISROKMOHYONO/"));
                startActivity(intent);
            }
        });

        binding.polisi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nisrokmohyono.blogspot.com/p/privacy-policy-diamond-prize-quizme.html"));
                startActivity(intent);
            }
        });

        binding.term.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nisrokmohyono.blogspot.com/p/terms-conditions-diamond-prize-quizme.html"));
                startActivity(intent);
            }
        });


        return binding.getRoot();

    }



}