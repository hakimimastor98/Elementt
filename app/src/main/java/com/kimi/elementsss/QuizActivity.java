package com.kimi.elementsss;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kimi.elementsss.fragment.QuizFragment;

public class QuizActivity extends AppCompatActivity {

    public static final String LOG_TAG = QuizFragment.class.getSimpleName();

    RadioButton question1_choice3;
    RadioButton question2_choice3;
    RadioButton question3_choice2;
    RadioButton question4_choice1;
    RadioButton question5_choice2;
    RadioButton question6_choice1;
    RadioButton question7_choice1;
    RadioButton question8_choice2;
    RadioButton question9_choice3;
    RadioButton question10_choice2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_quizsecond);





    }

    public void submitAnswers(View view) {
        CharSequence resultsDisplay;
        Log.e(LOG_TAG, " " + this.findViewById(R.id.question3_choice3));
        int answer1_score;
        int answer2_score;
        int answer3_score;
        int answer4_score;
        int answer5_score;
        int answer6_score;
        int answer7_score;
        int answer8_score;
        int answer9_score;
        int answer10_score;
        int final_score;


        Boolean answer1;

        question1_choice3 = (RadioButton) this.findViewById(R.id.question1_choice3);
        answer1 = question1_choice3.isChecked();
        if (answer1) {
            answer1_score = 1;
        } else {
            answer1_score = 0;
        }

        Boolean answer2;
        question2_choice3 = (RadioButton) this.findViewById(R.id.question2_choice3);
        answer2 = question2_choice3.isChecked();
        if (answer2) {
            answer2_score = 1;
        } else {
            answer2_score = 0;
        }

        Boolean answer3;
        question3_choice2 = (RadioButton) this.findViewById(R.id.question3_choice2);
        answer3 = question3_choice2.isChecked();
        if (answer3) {
            answer3_score = 1;
        } else {
            answer3_score = 0;
        }

        Boolean answer4;
        question4_choice1 = (RadioButton) this.findViewById(R.id.question4_choice1);
        answer4 = question4_choice1.isChecked();
        if (answer4) {
            answer4_score = 1;
        } else {
            answer4_score = 0;
        }

        Boolean answer5;
        question5_choice2 = (RadioButton) this.findViewById(R.id.question5_choice2);
        answer5 = question5_choice2.isChecked();
        if (answer5) {
            answer5_score = 1;
        } else {
            answer5_score = 0;
        }

        Boolean answer6;
        question6_choice1 = (RadioButton) this.findViewById(R.id.question6_choice1);
        answer6 = question6_choice1.isChecked();
        if (answer6) {
            answer6_score = 1;
        } else {
            answer6_score = 0;
        }

        Boolean answer7;
        question7_choice1 = (RadioButton) this.findViewById(R.id.question7_choice1);
        answer7 = question7_choice1.isChecked();
        if (answer7) {
            answer7_score = 1;
        } else {
            answer7_score = 0;
        }

        Boolean answer8;
        question8_choice2 = (RadioButton) this.findViewById(R.id.question8_choice2);
        answer8 = question7_choice1.isChecked();
        if (answer8) {
            answer8_score = 1;
        } else {
            answer8_score = 0;
        }

        Boolean answer9;
        question9_choice3 = (RadioButton) this.findViewById(R.id.question9_choice3);
        answer9 = question9_choice3.isChecked();
        if (answer9) {
            answer9_score = 1;
        } else {
            answer9_score = 0;
        }

        Boolean answer10;
        question10_choice2 = (RadioButton) this.findViewById(R.id.question10_choice2);
        answer10 = question9_choice3.isChecked();
        if (answer10) {
            answer10_score = 1;
        } else {
            answer10_score = 0;
        }

        final_score = answer1_score + answer2_score + answer3_score + answer4_score + answer5_score +
                answer6_score + answer7_score + answer8_score + answer9_score + answer10_score;

        if (final_score == 10) {
            resultsDisplay = "Perfect! You scored 10 out of 10";
        } else {
            resultsDisplay = "Try again. You scored " + final_score + " out of 10";
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, resultsDisplay, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }





}