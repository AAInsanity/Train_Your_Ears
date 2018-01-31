package com.example.app3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PitchChooseModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_choose_mode);
    }

    public void toSingle(View view){
        Intent intent = new Intent(this, PitchActivity2.class);
        startActivity(intent);
    }

    public void toAfterChord(View view){
        Intent intent = new Intent(this, PitchActivity.class);
        startActivity(intent);
    }

}
