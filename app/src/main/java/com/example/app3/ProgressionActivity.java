package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class ProgressionActivity extends AppCompatActivity {

    public static String SUCCESS = "com.example.app3.success";
    public static String ATTEMPTS = "com.example.app3.attempts";

    private ArrayList<Runnable> runnables = new ArrayList<>(); //a list of runnables that essentially plays the chords
    private Handler handler = new Handler();
    private ChordBuilder builder;
    private HashMap<String,Progression> progressionDict;
    private SharedPreferences prefs;
//    private Boolean simple; //whether simple
//    private Boolean root; //whether in root
    //whether octaveless will be taken care of in chord builder
    private String rProgression; //the right chord quality
    private int rProgressionSize;
    private ArrayList<String> progressions; //the progressions that the user wants to add in
    private Toast resultToast;
    private int success;
    private int attempts;
    private Gson gson;
    private int basenote;
    private AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progression);
        //creates gson
        gson = new Gson();

        //the shared preference that I store my things in;
        prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);
        //gets the json String in SharedPreference that contains all the CustomChords
        String selectedProgressions = prefs.getString("Selected Progressions", null);
        System.out.println("From ProgressionActivity" + selectedProgressions);

        if (selectedProgressions == null) { //if no progression added
            progressions = new ArrayList<>(); //create a empty list
        }
        else { //if some progression is selected
            //get the list of progression names from the Json string
            progressions = gson.fromJson(selectedProgressions, new TypeToken<List<String>>(){}.getType());
        }
        //adds in the selected custom chords
        //gets the json String in SharedPreference that contains the selected custom progressions
        ArrayList<String> selectedCustom;
        String selectedCustomProgressions = prefs.getString("Selected Custom Progressions", null);
        System.out.println("Selected Custom Progression: " + selectedCustomProgressions);
        if (selectedCustomProgressions == null) { //if no custom progression is selected
            selectedCustom = new ArrayList<>(); //create a empty List
        }
        else {
            selectedCustom = gson.fromJson(selectedCustomProgressions, new TypeToken<List<String>>(){}.getType());
        }
        progressions.addAll(selectedCustom);
        System.out.println("Total selected progressions: " + progressions);



        //randomly select a basenote from 0-11
        Random gen = new Random();
        basenote = gen.nextInt(12);

        //generates builder
        builder = new ChordBuilder();

        //builds progressions dict
        progressionDict = buildProgressionDict();
        addAllCustomToDict();

//        //sets the play button
//        setPlayButton();

        //sets whether the "root" or "simple" are specified
        prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);

//        progressions = new ArrayList<>();
//        addProgressions(); //fill "qualities" with selected chord qualities

        //print out the qualities that have been selected
        System.out.println("Selected Qualities: ");
        for (String str : progressions) {
            System.out.println(str);
        }

        Collections.shuffle(progressions);
        rProgression = progressions.get(0); //gets a random quality as right quality
        rProgressionSize = progressionDict.get(rProgression).size();
        constructRunnables();

        //sets up the answer buttons
        setAnswerButtons();

        //sets the records; and updates the display
        success = getIntent().getIntExtra(SUCCESS, 0);
        attempts = getIntent().getIntExtra(ATTEMPTS, 0);
        updateRecord();
    }


    //construct a dictionary of different progressions
    public static HashMap<String,Progression> buildProgressionDict(){
        HashMap<String,Progression> progressionDict = new HashMap<>();
        //4 chord progressions
        progressionDict.put("I-ii-V7-I",new Progression(new KeyValuePair(0, "major triad"), new KeyValuePair(2,"minor triad"), new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0,"major triad")));
        progressionDict.put("I-IV-V7-I",new Progression(new KeyValuePair(0, "major triad"), new KeyValuePair(5,"major triad"), new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0,"major triad")));
        progressionDict.put("I-V7/V-V7-I",new Progression(new KeyValuePair(0, "major triad"), new KeyValuePair(2,"dominant seventh"), new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0,"major triad")));
        progressionDict.put("I-ii-V7/vi-vi",new Progression(new KeyValuePair(0, "major triad"), new KeyValuePair(2,"minor triad"), new KeyValuePair(4,"dominant seventh"),new KeyValuePair(9,"minor triad")));
        progressionDict.put("I-bVII-IV-I",new Progression(new KeyValuePair(0, "major triad"), new KeyValuePair(10,"major triad"), new KeyValuePair(5,"major triad"),new KeyValuePair(0,"major triad")));
        progressionDict.put("I-bVI-V7-I",new Progression(new KeyValuePair(0, "major triad"), new KeyValuePair(8,"major triad"), new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0,"major triad")));

        progressionDict.put("i-ii°-V7-i",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(2,"diminished triad"), new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0,"minor triad")));
        progressionDict.put("i-iv-V7-i",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(5,"minor triad"), new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0,"minor triad")));
        progressionDict.put("i-V7/V-V7-i",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(2,"dominant seventh"), new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0,"minor triad")));
        progressionDict.put("i-VI-III-VII",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(8,"major triad"), new KeyValuePair(3,"major triad"),new KeyValuePair(10,"major triad")));
        progressionDict.put("i-VI-VII-i",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(8,"major triad"), new KeyValuePair(10,"major triad"),new KeyValuePair(0,"minor triad")));
        progressionDict.put("i-VII-iv-VI",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(10,"major triad"), new KeyValuePair(5,"minor triad"),new KeyValuePair(8,"major triad")));
        progressionDict.put("i-III-VII-VI",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(3,"major triad"), new KeyValuePair(10,"major triad"),new KeyValuePair(8,"major triad")));
        progressionDict.put("i-v-VII-iv",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(7,"minor triad"), new KeyValuePair(10,"major triad"),new KeyValuePair(5,"minor triad")));

        //5 chord progressions
        progressionDict.put("I-vi-ii-V7-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(9, "minor triad"),new KeyValuePair(2, "minor triad"),
                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "major triad")));
        progressionDict.put("I-IV-ii-V7-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(5, "major triad"),new KeyValuePair(2, "minor triad"),
                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "major triad")));
        progressionDict.put("I-vi-IV-V7-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(9, "minor triad"),new KeyValuePair(5, "major triad"),
                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "major triad")));
        progressionDict.put("I-V-IV-V7-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(7, "major triad"),new KeyValuePair(5, "major triad"),
                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "major triad")));
        progressionDict.put("I-V-IV-bVII-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(7, "major triad"),new KeyValuePair(5, "major triad"),
                new KeyValuePair(10,"major triad"),new KeyValuePair(0, "major triad")));
        progressionDict.put("I-V-vi-iii-IV", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(7, "major triad"),new KeyValuePair(9, "minor triad"),
                new KeyValuePair(4,"minor triad"),new KeyValuePair(5, "major triad")));
        progressionDict.put("I-V7/ii-ii-V7-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(9, "dominant seventh"),new KeyValuePair(2, "minor triad"),
                new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0, "major triad")));
        progressionDict.put("I-bIII-IV-V7-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(3, "major triad"),new KeyValuePair(5, "major triad"),
                new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0, "major triad")));
        progressionDict.put("I-V7/IV-IV-V7-I", new Progression(new KeyValuePair(0, "major triad"),new KeyValuePair(0, "dominant seventh"),new KeyValuePair(5, "major triad"),
                new KeyValuePair(7,"dominant seventh"),new KeyValuePair(0, "major triad")));

//        progressionDict.put("i-VI-ii°-V7-i", new Progression(new KeyValuePair(0, "minor triad"),new KeyValuePair(9, "major triad"),new KeyValuePair(2, "diminished triad"),
//                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "minor triad")));
        progressionDict.put("i-iv-ii°-V7-i", new Progression(new KeyValuePair(0, "minor triad"),new KeyValuePair(5, "minor triad"),new KeyValuePair(2, "diminished triad"),
                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "minor triad")));
//        progressionDict.put("i-VI-iv-V7-i", new Progression(new KeyValuePair(0, "minor triad"),new KeyValuePair(9, "major triad"),new KeyValuePair(5, "minor triad"),
//                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "minor triad")));
        progressionDict.put("i-V-iv-V7-i", new Progression(new KeyValuePair(0, "minor triad"),new KeyValuePair(7, "major triad"),new KeyValuePair(5, "minor triad"),
                new KeyValuePair(7, "dominant seventh"),new KeyValuePair(0, "minor triad")));
        progressionDict.put("i-VII-VI-V-i", new Progression(new KeyValuePair(0, "minor triad"),new KeyValuePair(10, "major triad"),new KeyValuePair(8, "major triad"),
                new KeyValuePair(7, "major triad"),new KeyValuePair(0, "minor triad")));
        progressionDict.put("i-iv-VII-III-i", new Progression(new KeyValuePair(0, "minor triad"),new KeyValuePair(5,"minor triad"),new KeyValuePair(10, "major triad"),
                new KeyValuePair(3, "major triad"),new KeyValuePair(0, "minor triad")));
        progressionDict.put("i-VI-III-VII-i",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(8,"major triad"), new KeyValuePair(3,"major triad"),
                new KeyValuePair(10,"major triad"),new KeyValuePair(0,"minor triad")));
        progressionDict.put("i-v-VI-VII-i",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(7,"minor triad"), new KeyValuePair(8,"major triad"),
                new KeyValuePair(10,"major triad"),new KeyValuePair(0,"minor triad")));
        progressionDict.put("i-v-iv-VII-i",new Progression(new KeyValuePair(0, "minor triad"), new KeyValuePair(7,"minor triad"), new KeyValuePair(5,"minor triad"),
                new KeyValuePair(10,"major triad"),new KeyValuePair(0,"minor triad")));

        return progressionDict;
    }

    //adds all custom chords to progressionDict, including those that are not selected.
    private void addAllCustomToDict(){
        HashMap<String, Progression> customDict;
        //gets the json String in SharedPreference that contains all the custom progressions
        String customProgressions = prefs.getString("Custom Progressions", null);
        System.out.println(customProgressions);
        if (customProgressions == null) { //if no custom progression is added
            customDict = new HashMap<>(); //create a empty map
        }
        else {
            customDict = gson.fromJson(customProgressions, new TypeToken<HashMap<String,Progression>>(){}.getType());
        }
        progressionDict.putAll(customDict);
    }




    //construct a series of runnables that plays a set of chords representing the progression
    public void constructRunnables(){
        final Progression p1 = progressionDict.get(rProgression);
        int pSize = p1.size();
        for(int i=0; i<pSize; i++) {
            final int degree = p1.getChordDegree(i);
            final String quality = p1.getChordQuality(i);
            final ArrayList<Integer> chordNotes = builder.buildRandomChordRoot(quality, (basenote+degree)%12);
            Runnable r = new Runnable(){
                public void run(){
                    ChordActivity.playChord(chordNotes);
                }
            };
            runnables.add(r);
        }
    }

    //linked with the play button
    public void playProgression(View view) {
        final Button playButton = (Button)view;
        playButton.setEnabled(false);
        int i=0;
        for (Runnable r1: runnables) {
            handler.postDelayed(r1, i*1000);
            i++;
        }
        Runnable unfreeze = new Runnable(){
            public void run(){
                playButton.setEnabled(true);
            }
        };
        handler.postDelayed(unfreeze, i*1000);
    }


    public void playProgression() {
        int i=0;
        for (Runnable r1: runnables) {
            handler.postDelayed(r1, i*1000);
            i++;
        }
    }

    //sets up the answer buttons
    public void setAnswerButtons() {
        Stack<Button> buttons = new Stack<>();
        buttons.push((Button)findViewById(R.id.button39));
        buttons.push((Button)findViewById(R.id.button40));
        buttons.push((Button)findViewById(R.id.button41));
        buttons.push((Button)findViewById(R.id.button42)); //adds all 4 answer buttons to stack "buttons"

        int numRelevantSelections = 0; //how many progressions (of the same size as the right quality) there are selected
        for (String item: progressions) { //for each selected string
            if (progressionDict.get(item).size()==rProgressionSize) //will only be used as a wrong answer if a progression has the same number of chords as the right progression
                numRelevantSelections++;
        }


        for(int i=0; i<4-numRelevantSelections; i++) {//for any redundant buttons
            Button b1 = buttons.pop();
            b1.setVisibility(View.GONE);//make it disappear
        }

        //now, "buttons" contains only the occupied ones

        Collections.shuffle(buttons);
        final Button rButton = buttons.pop(); //this is a random button as the right button;
        rButton.setText(rProgression); //sets it to the right quality
        rButton.setOnClickListener(new View.OnClickListener() { //sets the on click for right button
            @Override
            public void onClick(View view) {
                attempts++;
                success++;
                updateRecord();
                rButton.setTextColor(Color.GREEN);
                showToast(true);
                Intent intent = getIntent();
                intent.putExtra(SUCCESS, success);
                intent.putExtra(ATTEMPTS, attempts);
                startActivity(intent);
            }
        });

        //now, "buttons" contains only the wrong ones

        Stack<String> wrongAnswers = new Stack<>();
        for (String item: progressions) { //pushes all the wrong qualities
            if ((!item.equals(rProgression)) && progressionDict.get(item).size()==rProgressionSize) //will only be used as a wrong answer if a progression has the same number of chords as the right progression
                wrongAnswers.push(item);
        }
        Collections.shuffle(wrongAnswers);

        for (final Button b:buttons) { //for the wrong buttons
            b.setText(wrongAnswers.pop()); //sets it to one of the wrong answers
            b.setOnClickListener(new View.OnClickListener(){ //sets the on click actions
                public void onClick(View view){
                    attempts++;
                    updateRecord();
                    b.setTextColor(getResources().getColor(R.color.colorAccent));
                    showToast(false);
                }
            });
        }

        System.out.println("Correct Quality:");
        System.out.println(rProgression);
    }


    //updates the record display
    public void updateRecord() {
        TextView record = (TextView) findViewById(R.id.textView15);
        DecimalFormat df = new DecimalFormat("#0.0");
        String accuracy;
        if (success == 0 && attempts == 0)
            accuracy = "N/A";
        else
            accuracy = df.format((double)100*success/attempts) + "%"; //keep 2 digits after .
        record.setText(getString(R.string._result_display_,success, attempts, accuracy)); //sets text to display results
    }

    //shows a toast to represent the result
    public void showToast(Boolean boo) {
        if (resultToast != null) //if there is already a visible toast, cancel it
            resultToast.cancel();

        //sets resultToast to appropriate text and color
        if (boo) { //if answer is correct
            //sets text
            resultToast = Toast.makeText(this,
                    "You're right. Congratulations!", Toast.LENGTH_SHORT);
            //sets color
            ((TextView)resultToast.getView().findViewById(android.R.id.message)).setTextColor(Color.GREEN);
        }
        else {
            //sets text
            resultToast = Toast.makeText(this,
                    "That's incorrect. Try again.", Toast.LENGTH_SHORT);
            //sets color
            ((TextView)resultToast.getView().findViewById(android.R.id.message)).setTextColor(getResources().getColor(R.color.colorAccent));
        }
        resultToast.setGravity(Gravity.CENTER, 0, 0);
        resultToast.show();
    }

    //shows the answer (cheat button)
    public void showAnswer(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The correct choice is: " + rProgression + ".");
        builder.setNeutralButton("Listen Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                    }
                }
        );
        builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                    }
                }
        );
//        final AlertDialog
        ad = builder.create();
        ad.setCancelable(false); //so that the dialog is not dismissed by any clicks
        ad.setOnShowListener(new DialogInterface.OnShowListener(){
            //the purpose of using the on-show listener is to set the dialog's button after
            //the dialog is shown on screen, so that clicking the "play" button will not cause
            //the dialog to disappear.
            //THAT BEING SAID, I don't really know why it works like this.
            public void onShow(DialogInterface dialog) {
                Button b1 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
                Button b2 = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
                b1.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        playProgression();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        ad.dismiss();
                        Intent intent = getIntent();
                        intent.putExtra(SUCCESS, success);
                        intent.putExtra(ATTEMPTS, attempts);
                        startActivity(intent);
                    }
                });
            }
        });
        ad.show();
    }

    //back to main activity
    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
