package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "com.example.myapplication.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private boolean answerWasShown=false;

    private Question[] questions = new Question[]{
      new Question(R.string.q_activity, true),
      new Question(R.string.q_find_resources, false),
      new Question(R.string.q_listener, true),
      new Question(R.string.q_resources, true),
      new Question(R.string.q_version, false),
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("QUIZ_TAG", "Wywołana została metoda: onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    private int currentIndex=0;
    private int countcorrect=0;

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("2", "onStart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("2", "onDestroy()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("2", "onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("2", "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("2", "onResume()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("2", "onCreate()");

        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        nextButton=findViewById(R.id.next_button);
        promptButton=findViewById(R.id.prompt_button);
        questionTextView=findViewById(R.id.question_text_view);
        setNextQuestion();

        if(savedInstanceState!=null) {currentIndex=savedInstanceState.getInt(KEY_CURRENT_INDEX);}

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerCorrectness(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerWasShown=false;
                if(currentIndex==questions.length-1){
                    endOfQuiz();
                    currentIndex=-1;
                    countcorrect=0;
                }
                else{
                    currentIndex=(currentIndex+1)%questions.length;
                    setNextQuestion();
                }
            }
        });
        promptButton.setOnClickListener(v -> {
            if(currentIndex!=-1) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].isTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){return;}
        if(requestCode==REQUEST_CODE_PROMPT){
            if(data==null){return;}
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer=questions[currentIndex].isTrueAnswer();
        int resultMessageId=0;
        if(answerWasShown){
            resultMessageId=R.string.answer_was_shown;
        }
        else{
            if(userAnswer==correctAnswer){
                resultMessageId=R.string.correct_answer;
                countcorrect++;
            }
            else{
                resultMessageId=R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }

    private void setNextQuestion(){
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    private void endOfQuiz(){
        questionTextView.setText("Koniec quizu!\nOdpowiedziano " + countcorrect+ " razy dobrze!");;
    }
}