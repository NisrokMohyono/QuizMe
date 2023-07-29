package com.diamondprize.quizme;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.diamondprize.quizme.databinding.ActivityKumpulanGameBinding;

public class KumpulanGameActivity extends AppCompatActivity {

    ActivityKumpulanGameBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kumpulan_game);
        binding = ActivityKumpulanGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new WebgameFragment());
        transaction.commit();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, new WebgameFragment())
                    .commit();
        }

    }
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

}