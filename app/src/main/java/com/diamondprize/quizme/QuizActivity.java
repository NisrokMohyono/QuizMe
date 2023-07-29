package com.diamondprize.quizme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diamondprize.quizme.databinding.ActivityQuizBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question> questions;
    int index = 0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers = 1;

    private AdView mAdView;
    //timer saat kuis di klik 2 detik delay
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAdView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        final String catId = getIntent().getStringExtra("catId");
        Random random = new Random();
        final int rand = random.nextInt(12);

        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", rand)
                .orderBy("index")
                .limit(25).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.getDocuments().size() < 25) {
                        database.collection("categories")
                                .document(catId)
                                .collection("questions")
                                .whereLessThanOrEqualTo("index", rand)
                                .orderBy("index")
                                .limit(25).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for(DocumentSnapshot snapshot : queryDocumentSnapshots1) {
                                            Question question = snapshot.toObject(Question.class);
                                            questions.add(question);
                                        }
                                    setNextQuestion();
                                });
                    } else {
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            Question question = snapshot.toObject(Question.class);
                            questions.add(question);
                        }
                        setNextQuestion();
                    }
                });

        binding.quizBtn.setOnClickListener(v -> {
            startActivity(new Intent(QuizActivity.this, MainActivity.class));
            finishAffinity();
        });

        resetTimer();

        // koneksi internet
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            Toast.makeText(getApplicationContext(),"Connect SUCCEED", Toast.LENGTH_LONG).show();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setPositiveButton("Closed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            alert.setCancelable(false);
            alert.show();
        }

    }

    void resetTimer() {
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                StartAppAd.showAd(QuizActivity.this);
            }
        };
    }

    void showAnswer() {
        if(question.getAnswer().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAnswer().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion() {
        if(timer != null)
            timer.cancel();

        timer.start();
        if(index < questions.size()) {
            binding.questionCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())) {
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset() {
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer!=null) timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);
                reset();

                if(index <= questions.size()) {

                    index++;
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    //Toast.makeText(this, "Quiz Finished.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizActivity.this, MainActivity.class));
        finishAffinity();
    }


}