package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgressionSelectActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private ArrayList<String> selected;
    private DefaultProgressionAdapter mAdapter1;
    private DefaultProgressionAdapter mAdapter2;
    private SharedPreferences.Editor editor;
    public SharedPreferences prefs;
    public Gson gson;
    public static ArrayList<String> fourChord;
    static {
        String[] temp = new String[]{"I-ii-V7-I","I-IV-V7-I","I-V7/V-V7-I","I-ii-V7/vi-vi","I-bVII-IV-I","I-bVI-V7-I","I-V7/iv-iv-I","i-ii°-V7-i",
                "i-iv-V7-i","i-V7/V-V7-i","i-VI-III-VII","i-VI-VII-i","i-VII-iv-VI","i-III-VII-VI","i-v-VII-iv"};
        fourChord = new ArrayList<>(Arrays.asList(temp));
    }
    public static ArrayList<String> fiveChord;
    static {
        String[] temp = new String[]{"I-vi-ii-V7-I","I-IV-ii-V7-I","I-vi-IV-V7-I","I-V-IV-V7-I","I-V-IV-bVII-I","I-V-vi-iii-IV","I-V7/ii-ii-V7-I",
                "I-bIII-IV-V7-I","I-V7/IV-IV-V7-I","i-iv-ii°-V7-i","i-V-iv-V7-i","i-VII-VI-V-i","i-iv-VII-III-i","i-VI-III-VII-i",
                "i-v-VI-VII-i", "i-v-iv-VII-i"};
        fiveChord = new ArrayList<>(Arrays.asList(temp));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progression_select);

        gson = new Gson();

        //the shared preference that I store my things in;
        prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);

        //the editor for the shared-preference named MY_PREFERENCE
        editor = prefs.edit();

        //gets the json String in SharedPreference that contains all the CustomChords
        String selectedProgressions = prefs.getString("Selected Progressions", null);
        System.out.println("From ProgressionSelectActivity: " + selectedProgressions);

        if (selectedProgressions == null) { //if no progression added
            selected = new ArrayList<>(); //create a empty list
        }
        else {
            selected = gson.fromJson(selectedProgressions, new TypeToken<List<String>>(){}.getType());
            //build the mData with the string CustomChords from sharedpreference
        }

        //sets up recycler view for 4 chord progressions
        mRecyclerView1 = (RecyclerView) findViewById(R.id.RV1);
        //sets up the grid recyclerview
        mAdapter1 = new DefaultProgressionAdapter(ProgressionSelectActivity.this, fourChord, selected,2);
        mRecyclerView1.setAdapter(mAdapter1);
        GridLayoutManager gridManager = new GridLayoutManager(this, 2); //3 items in each row
        mRecyclerView1.setLayoutManager(gridManager);

        //sets up recycler view for 5 chord progressions
        mRecyclerView2 = (RecyclerView) findViewById(R.id.RV2);
        //sets up the grid recyclerview
        mAdapter2 = new DefaultProgressionAdapter(ProgressionSelectActivity.this, fiveChord, selected,2);
        mRecyclerView2.setAdapter(mAdapter2);
        GridLayoutManager gridManager2 = new GridLayoutManager(this, 2); //3 items in each row
        mRecyclerView2.setLayoutManager(gridManager2);
    }

    //set up the dialog to show if no chords were selected
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please select at lease ONE progression for the drill.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    public void apply (View view) {
        ArrayList<String> selectedCustom;
        String selectedCustomProgressions = prefs.getString("Selected Custom Progressions", null);
        System.out.println("Selected Custom Progression: " + selectedCustomProgressions);
        if (selectedCustomProgressions == null) { //if no custom progression is selected
            selectedCustom = new ArrayList<>(); //create a empty List
        }
        else {
            selectedCustom = gson.fromJson(selectedCustomProgressions, new TypeToken<List<String>>(){}.getType());
        }
        if (selected.size()<1 && selectedCustom.size()<1)
            showDialog();
        else {
            String selectedProgressions = gson.toJson(selected); //converts the updated mData to a String
            editor.putString("Selected Progressions", selectedProgressions); //put it in prefs under "Selected Progressions"
            editor.apply();
            Intent intent = new Intent(this, ProgressionActivity.class);
            startActivity(intent);
        }
    }

    public void toCustomProgression(View view){
        String selectedProgressions = gson.toJson(selected); //converts the updated mData to a String
        editor.putString("Selected Progressions", selectedProgressions); //put it in prefs under "Selected Progressions"
        editor.apply();
        Intent intent = new Intent(this,CustomProgressionActivity1.class);
        startActivity(intent);
    }

}
