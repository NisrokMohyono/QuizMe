package com.diamondprize.quizme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.diamondprize.quizme.databinding.FragmentWebgameBinding;


public class WebgameFragment extends Fragment {
    public WebgameFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    FragmentWebgameBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentWebgameBinding.inflate(inflater, container, false);

        // Memberikan onClickListener untuk tombol
        binding.imageView1e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Berpindah ke Fragment 2 ketika tombol diklik
                switchToFragment2();
                Fragment fragment = new FreefireFragment(); // Ganti Fragment2 dengan fragment lain yang ingin ditampilkan
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        binding.imageView17.setOnClickListener(v -> {
            switchToFragment2();
            Fragment fragment = new MlFragment(); // Ganti Fragment2 dengan fragment lain yang ingin ditampilkan
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        });
        binding.imageView18.setOnClickListener(v -> {
            switchToFragment2();
            Fragment fragment = new DanaFragment(); // Ganti Fragment2 dengan fragment lain yang ingin ditampilkan
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        binding.imageView19.setOnClickListener(v -> {
            switchToFragment2();
            Fragment fragment = new GopayFragment(); // Ganti Fragment2 dengan fragment lain yang ingin ditampilkan
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return binding.getRoot();
    }
    private void switchToFragment2() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}