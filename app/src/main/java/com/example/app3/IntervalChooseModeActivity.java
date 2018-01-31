package com.example.app3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//the activity that enables the user to choose whether to go to multiple choice
//or to fill-in-blank
public class IntervalChooseModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_choose_mode);
    }

    //go to multiple choice
    public void toMultipleChoice(View view) {
        Intent intent = new Intent(this, IntervalActivity.class);
        startActivity(intent);
    }

    //go to fill in blank
    public void toFillBlank(View view) {
        Intent intent = new Intent(this, Interval2Activity.class);
        startActivity(intent);
    }

}
