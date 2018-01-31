package com.example.app3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomProgressionActivity2 extends AppCompatActivity {

    private HashMap<Button,Integer> buttonDict = new HashMap<>(); //a hashmap that can look up the index of button with button itself
    private int numChords; //number of chords in the progression that will be created
    final private Progression myProgression = new Progression();
    private HashMap<String, Progression> customDict;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progression2);

        numChords = getIntent().getIntExtra(CustomProgressionActivity1.CHORD_NUMBER, 5);

        gson = new Gson();

        //gets the sharedpreferences and its editor
        prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);
        editor = prefs.edit();
        //gets the json String in SharedPreference that contains all the CustomChords
        String customProgressions = prefs.getString("Custom Progressions", null);
        System.out.println(customProgressions);

        if (customProgressions == null) { //if no custom progression is added
            customDict = new HashMap<>(); //create a empty map
        }
        else {
            customDict = gson.fromJson(customProgressions, new TypeToken<HashMap<String,Progression>>(){}.getType());
        }

        //builds the progression
        for (int i=0;i<numChords; i++){
            myProgression.addChord(-1,""); // a chord will be added with default degree -1 and quality ""
        }

        final Button b1 = (Button) findViewById(R.id.button46);
        final Button b2 = (Button) findViewById(R.id.button47);
        final Button b3 = (Button) findViewById(R.id.button48);
        final Button b4 = (Button) findViewById(R.id.button49);
        final Button b5 = (Button) findViewById(R.id.button50);
        final Button b6 = (Button) findViewById(R.id.button51);
        final Button b7 = (Button) findViewById(R.id.button52);
        final Button b8 = (Button) findViewById(R.id.button53);

        Button[] buttonArray = new Button[]{b1,b2,b3,b4,b5,b6,b7,b8};
        for (int i = 0; i < numChords; i++){
            buttonDict.put(buttonArray[i],i);
        }
        for (int i=7; i >= numChords; i--){
            buttonArray[i].setVisibility(View.GONE);
        }

        for (final Button b: buttonDict.keySet()) {
            final int index = buttonDict.get(b); //the index of the button, start from 0
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomProgressionActivity2.this);
                    View v = getLayoutInflater().inflate(R.layout.spinner_dialog, null);
                    final CustomSpinner cs1 = v.findViewById(R.id.customSpinner4);
                    MySpinnerAdapter adapter1 = new MySpinnerAdapter(CustomProgressionActivity2.this,
                            new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Degree"});
                    cs1.setAdapter(adapter1);
                    //sets the initial selection of spinner 1
                    if (myProgression.getChordDegree(index)<0){ //that means no degree is currently chosen
                        cs1.setSelection(adapter1.getCount());
                    }
                    else {
                        cs1.setSelection(myProgression.getChordDegree(index));
                    }
                    final CustomSpinner cs2 = v.findViewById(R.id.customSpinner5);
                    MySpinnerAdapter adapter2 = new MySpinnerAdapter(CustomProgressionActivity2.this,
                            new String[]{"major triad", "minor triad", "augmented triad", "diminished triad", "sus 4 triad", "sus 2 triad",
                                    "major seventh", "minor seventh", "dominant seventh", "half diminished seventh", "diminished seventh",
                                    "major ninth", "minor ninth", "dominant ninth", "Quality"});
                    cs2.setAdapter(adapter2);
                    //sets the initial selection of spinner 1
                    if (myProgression.getChordQuality(index) == ""){ //that means no quality is currently chosen
                        cs2.setSelection(adapter2.getCount());
                    }
                    else {
                        cs2.setSelection(adapter2.getPosition(myProgression.getChordQuality(index)));
                    }

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int degree;
                            if (cs1.getSelectedItem().toString() == "Degree") degree = -1;
                            else {
                                degree = Integer.parseInt(cs1.getSelectedItem().toString());
                                myProgression.setDegree(index, degree);
                            }
                            String quality = cs2.getSelectedItem().toString();
                            if (quality == "Quality") quality = "";
                            else myProgression.setQuality(index, quality);
                            if (degree == -1 && quality != "")
                                b.setText("Degree: \n\nQuality: \n" + quality);
                            else if (degree != -1)
                                b.setText("Degree: \n" + degree + "\nQuality: \n" + quality);
                            System.out.println(myProgression.toString());
                        }
                    });
                    builder.setView(v);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    public boolean isValid(){
        boolean boo = true;
        for (int i=0; i<numChords; i++) {
            int degree = myProgression.getChordDegree(i);
            if (degree<0) {
                boo = false;
                break;
            }
            String quality = myProgression.getChordQuality(i);
            if (quality == ""){
                boo = false;
                break;
            }
        }
        return boo;
    }

    //show a dialog to warn the user that the same chord name is already present in the database
    public void showDialog(final String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A chord with the same "+ str + " is already present in database.");
        builder.setNegativeButton("Got it", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }

    public void showDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please specify degree and quality for all chords in your progression.");
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialog3(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please give a name for your progression.");
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addProgression(View view){
        //make sure edit text isn't empty
        if (((EditText)findViewById(R.id.editText2)).getText().toString().length() < 1) {showDialog3(); return;}
        //make sure all chords are specified
        if (!isValid()) {showDialog2(); return;}
        //make sure that the new name is not a duplicate
        HashMap<String, Progression> defaultProgressions = ProgressionActivity.buildProgressionDict();
        String name =((EditText)findViewById(R.id.editText2)).getText().toString();
        if (customDict.containsKey(name) || defaultProgressions.keySet().contains(name)) {
            showDialog(name);
            return;
        } //if the name is already present in the custom progression dictionary

        customDict.put(name,myProgression);
        String customProgressions = gson.toJson(customDict); //converts the updated mData to a String
        editor.putString("Custom Progressions", customProgressions); //put it in prefs under "Custom Chords"
        editor.apply();
        Intent intent = new Intent(this, CustomProgressionActivity1.class);
        startActivity(intent);
    }
}
