package com.example.app3;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Map;



//Main Activity
//Initial page of the app
public class MainActivity extends AppCompatActivity {

    public static final MySoundPool msp = new MySoundPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);
        System.out.println("shared preference size? " + prefs.getAll().size());
        for (Map.Entry<String, ?> entry: prefs.getAll().entrySet()) {
            System.out.println("key: " + entry.getKey() + "  value: " + entry.getValue());
        }
    }

    //goes to interval activities
    public void toInterval(View view) {
        Intent intent = new Intent(this,IntervalChooseModeActivity.class);
        startActivity(intent);
    }

    //goes to chord activities (select chords)
    public void toChord(View view) {
        //Intent intent = new Intent(this, ChordSelectActivity.class);
        Intent intent = new Intent(this, ChordSelectActivity.class);
        startActivity(intent);
    }

    //goes to pitch activities
    public void toPitch(View view) {
        Intent intent = new Intent(this,PitchChooseModeActivity.class);
        startActivity(intent);
    }

    //goes to progression activities
    public void toProgression(View view) {
        Intent intent = new Intent(this,ProgressionSelectActivity.class);
        startActivity(intent);
    }

}
