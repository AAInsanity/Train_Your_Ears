package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Stack;

public class ChordActivity extends AppCompatActivity {

    public static String SUCCESS = "com.example.app3.success";
    public static String ATTEMPTS = "com.example.app3.attempts";

    private ChordBuilder builder;
    private static SoundPool sp = MainActivity.msp.getSoundPool();
    private static HashMap<Integer,Integer> pitchId = MainActivity.msp.getPitchId();
    private SharedPreferences prefs;
    private Boolean simple; //whether simple
    private Boolean root; //whether in root
    //whether octaveless will be taken care of in chord builder
    private String rQuality; //the right chord quality
    private ArrayList<Integer> rPitches; //the right pitches
    private ArrayList<String> qualities; //the qualities that the user wants to add in
    private Toast resultToast;
    private int success;
    private int attempts;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord);

        //creates gson
        gson = new Gson();

        //generates builder
        builder = new ChordBuilder();

        //sets the play button
        setPlayButton();

        //sets whether the "root" or "simple" are specified
        prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);
        root = prefs.getBoolean("root", false);
        simple = prefs.getBoolean("simple", false);
        System.out.println("root? " + root);
        System.out.println("simple? " + simple);

        qualities = new ArrayList<>();
        addQualities(); //fill "qualities" with selected chord qualities

        //print out the qualities that have been selected
        System.out.println("Selected Qualities: ");
        for (String str : qualities) {
            System.out.println(str);
        }

        Collections.shuffle(qualities);
        rQuality = qualities.get(0); //gets a random quality as right quality

        //randomly build a chord from that quality
        rPitches = buildChordFromQuality(rQuality);
        System.out.println("lowest pitch: " + Collections.min(rPitches));

        //sets up the answer buttons
        setAnswerButtons();

        //sets the records; and updates the display
        success = getIntent().getIntExtra(ChordActivity.SUCCESS, 0);
        attempts = getIntent().getIntExtra(ChordActivity.ATTEMPTS, 0);
        updateRecord();

    }


    //randomly build a chord (based on a random pitch) with given quality
    //for certain chords, the chord generated must be in root position (such as sus2, sus4 triads)
    //for other chords, the chord can be in any position                                                                                                                                                                                     inversion (unless specified otherwise)
    public ArrayList<Integer> buildChordFromQuality(String quality) {

        ArrayList<Integer> pitchesToPlay;//the pitches that would be played

        if (simple) { //if simple is selected
            pitchesToPlay=builder.buildRandomChordSimple(quality);
        }
        else if (root) {//if simple is not selected and root is selected
            pitchesToPlay=builder.buildRandomChordRoot(quality);
        }
        else {//if neither simple nor root is selected
            switch (quality) {
                case "sus 2 triad":
                case "sus 4 triad": //for certain chord qualities that would be confusing in inversions
                    pitchesToPlay = builder.buildRandomChordRoot(quality); //build random root position chord
                    break;

                default: //for all the other chords
                    pitchesToPlay = builder.buildRandomChord(quality); //build random chord
                    break;
            }
        }
       return pitchesToPlay;

    }

    //reads the sharedpreference and adds qualities into "qualities"
    public void addQualities() {
        SharedPreferences sp = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);
        if (sp.getBoolean("maj3", false)) //if maj3 is selected
            qualities.add("major triad"); //add "major triad" to qualities
        if (sp.getBoolean("min3", false))
            qualities.add("minor triad");
        if (sp.getBoolean("aug3", false))
            qualities.add("augmented triad");
        if (sp.getBoolean("dim3", false))
            qualities.add("diminished triad");
        if (sp.getBoolean("sus4", false))
            qualities.add("sus 4 triad");
        if (sp.getBoolean("sus2", false))
            qualities.add("sus 2 triad");
        if (sp.getBoolean("maj7", false)) //seventh chords
            qualities.add("major seventh");
        if (sp.getBoolean("min7", false))
            qualities.add("minor seventh");
        if (sp.getBoolean("dom7", false))
            qualities.add("dominant seventh");
        if (sp.getBoolean("halfdim7", false))
            qualities.add("half diminished seventh");
        if (sp.getBoolean("dim7", false))
            qualities.add("diminished seventh");
        if (sp.getBoolean("augmaj7", false))
            qualities.add("aug/maj seventh");
        if (sp.getBoolean("minmaj7", false))
            qualities.add("min/maj seventh");
        if (sp.getBoolean("maj9", false))
            qualities.add("major ninth");
        if (sp.getBoolean("min9", false)) //seventh chords
            qualities.add("minor ninth");
        if (sp.getBoolean("dom9", false))
            qualities.add("dominant ninth");
        if (sp.getBoolean("dommin9", false))
            qualities.add("dom/min ninth");
        if (sp.getBoolean("s5s9", false))
            qualities.add("altered ♯5♯9");
        if (sp.getBoolean("s5f9", false))
            qualities.add("altered ♯5♭9");
        if (sp.getBoolean("f5s9", false))
            qualities.add("altered ♭5♯9");
        if (sp.getBoolean("f5f9", false))
            qualities.add("altered ♭5♭9");

        //adds the customChord qualities to "qualities" (to be chosen from)
        String customChords = prefs.getString("Custom Chords", null);
        if (customChords!=null) {
            ArrayList<CustomChord> listChords = gson.fromJson(customChords, new TypeToken<List<CustomChord>>(){}.getType());
            for (CustomChord c1: listChords){
                if (c1.isSelected())
                    qualities.add(c1.getChordName());
            }
        }


        //to add more

    }

    //plays every note given by the chord builder's buildRandomChord method
    public static void playChord(ArrayList<Integer> pitches) {
//        System.out.println("Quality played: ");
//        System.out.println(rQuality);
        System.out.println("Pitches played: ");
        for (int i: pitches) {
            System.out.println(i);
            sp.play(pitchId.get(i), 1, 1, 0, 0, 1);
        }
    }

    //sets the "play" button
    public void setPlayButton() {
        Button play = (Button) findViewById(R.id.button15);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playChord(rPitches);
            }
        });
    }

    //to chord select activity
    public void toSelectChords(View v) {
        Intent intent = new Intent(this, ChordSelectActivity.class);
        startActivity(intent);
    }

    //sets up the answer buttons
    public void setAnswerButtons() {
        Stack<Button> buttons = new Stack<>();
        buttons.push((Button)findViewById(R.id.button18));
        buttons.push((Button)findViewById(R.id.button19));
        buttons.push((Button)findViewById(R.id.button20));
        buttons.push((Button)findViewById(R.id.button21));
        buttons.push((Button)findViewById(R.id.button22));
        buttons.push((Button)findViewById(R.id.button23)); //adds all 6 answer buttons to stack "buttons"

        int numSelections = qualities.size(); //tells how many qualities that user chooses

        for(int i=0; i<6-numSelections; i++) {//for any redundant buttons
            Button b1 = buttons.pop();
            b1.setVisibility(View.GONE);//make it disappear
        }

        //now, "buttons" contains only the occupied ones

        Collections.shuffle(buttons);
        final Button rButton = buttons.pop(); //this is a random button as the right button;
        rButton.setText(rQuality); //sets it to the right quality
        rButton.setOnClickListener(new View.OnClickListener() { //sets the on click for right button
            @Override
            public void onClick(View view) {
                attempts++;
                success++;
                updateRecord();
                rButton.setTextColor(Color.GREEN);
                showToast(true);
                Intent intent = getIntent();
                intent.putExtra(ChordActivity.SUCCESS, success);
                intent.putExtra(ChordActivity.ATTEMPTS, attempts);
                startActivity(intent);
            }
        });

        //now, "buttons" contains only the wrong ones

        Stack<String> wrongQualities = new Stack<>();
        for (String item: qualities) { //pushes all the wrong qualities
            if (!item.equals(rQuality))
                wrongQualities.push(item);
        }
        Collections.shuffle(wrongQualities);

        for (final Button b:buttons) { //for the wrong buttons
            b.setText(wrongQualities.pop()); //sets it to one of the wrong answers
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
        System.out.println(rQuality);
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

    //updates the record display
    public void updateRecord() {
        TextView record = (TextView) findViewById(R.id.textView7);
        DecimalFormat df = new DecimalFormat("#0.0");
        String accuracy;
        if (success == 0 && attempts == 0)
            accuracy = "N/A";
        else
            accuracy = df.format((double)100*success/attempts) + "%"; //keep 2 digits after .
        record.setText(getString(R.string._result_display_,success, attempts, accuracy)); //sets text to display results
    }

    //displays a dialog that shows the answer
    public void showAnswer(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The correct answer is: " + rQuality + ".");
        builder.setNeutralButton("Listen Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //nothing
            }
        });
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //nothing
            }
        });

        // Question: why bother specifying the onclick actions
        // after the dialog is being displayed (using an onshowlistener)

        // Explanation:
        // I need to prevent the dialog from disappearing after the "listen again" button is clicked
        // And for that I need to use view.onClickListener instead of DialogInterface.onClickListener
        // since the latter will automatically set the dialog to disappear after any button on the dialog is clicked
        // and to use view.onclicklistener, the button must first be displayed on screen.
        // that why I need a onshowlistener to make sure that the view is displayed

        final AlertDialog ad = builder.create();
        ad.setCancelable(false);
        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b1 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
                Button b2 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                b1.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        playChord(rPitches);
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        Intent intent = getIntent();
                        intent.putExtra(ChordActivity.SUCCESS, success);
                        intent.putExtra(ChordActivity.ATTEMPTS, attempts);
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
