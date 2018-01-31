package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

//The class that supports the Interval (Multiple Choice) activity
public class IntervalActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.app3.MESSAGE";
    public static final String SUCCESS = "com.example.app3.SUCCESS"; //delivers the current success number
    public static final String ATTEMPTS = "com.example.app3.ATTEMPTS"; //delivers the current attempt number

    private SoundPool sp;
    private int size;
    private int low;
    private int high;
    private Random gen;
    private HashMap<Integer, Integer> pitchId;
    private String correct;
    private Toast resultToast;
    private int success;
    private int attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);

        //initiates the two counters
        success = getIntent().getIntExtra(IntervalActivity.SUCCESS, 0);
        attempts = getIntent().getIntExtra(IntervalActivity.ATTEMPTS, 0);
        updateRecord();

        //create the random generator
        gen = new Random();

        //randomly create the interval size (from 0 to 12)
        size = gen.nextInt(13);

        //randomly generate the low and high pitch
        low = gen.nextInt(24)+1;
        high = low + size;

        //get the pitchId hashmap from MainActivity;
        pitchId = MainActivity.msp.getPitchId();

        //get the SoundPool from MainActivity
        sp = MainActivity.msp.getSoundPool();


        //hook the play button up with playing the interval
        Button playButton = (Button)findViewById(R.id.button8);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                playInterval(low, high);
            }
        });

        //sets the answer buttons
        setAnswerButtons();

    }

    //returns the literal answer given an interval size
    //if there are 2, then return a random one
    public String getAnswer(int i) {

        String answer;

        switch (i) {

            case 0:
                answer = "Perfect Unison";
                break;

            case 1:
                if (gen.nextFloat() < 0.5)
                    answer = "Augmented Unison";
                else
                    answer = "Minor 2";
                break;

            case 2:
                answer = "Major 2";
                break;

            case 3:
                if (gen.nextFloat() < 0.5)
                    answer = "Augmented 2";
                else
                    answer = "Minor 3";
                break;

            case 4:
                answer = "Major 3";
                break;

            case 5:
                answer = "Perfect 4";
                break;

            case 6:
                if (gen.nextFloat() < 0.5)
                    answer = "Augmented 4";
                else
                    answer = "Diminished 5";
                break;

            case 7:
                answer = "Perfect 5";
                break;

            case 8:
                if (gen.nextFloat() < 0.5)
                    answer = "Augmented 5";
                else
                    answer = "Minor 6";
                break;

            case 9:
                answer = "Major 6";
                break;

            case 10:
                if (gen.nextFloat() < 0.5)
                    answer = "Augmented 6";
                else
                    answer = "Minor 7";
                break;

            case 11:
                answer = "Major 7";
                break;

            case 12:
                answer = "Perfect Octave";
                break;
            default: answer = "Invalid Interval Size";
                break;
        }
        return answer;
    }

    //play the two notes that forms the interval
    public void playInterval(int low, int high) {
        //play the two pitches
        System.out.println("low note: " + low);
        System.out.println("high note: " + high);
        sp.play(pitchId.get(low),1,1,0,0,1);
        sp.play(pitchId.get(high),1,1,0,0,1);
    }

    //sets the 4 answer buttons.
    //there will be a random button (among the 4) that carries the right answer
    //and to the other three, a random (but distinct) wrong answer will be assigned.
    public void setAnswerButtons() {
        final ArrayList<Button> buttons = new ArrayList<>();
        buttons.add((Button)findViewById(R.id.button4));
        buttons.add((Button)findViewById(R.id.button5));
        buttons.add((Button)findViewById(R.id.button6));
        buttons.add((Button)findViewById(R.id.button7)); //now "buttons" contains all 4 buttons

        int right = gen.nextInt(4); //the random button that has the right answer
        correct = getAnswer(size); //the right answer (randomly pick one if there are 2)
        final Button rButton = buttons.get(right); //the id of the right button
        rButton.setText(correct); //sets the right answer to the right button
        buttons.remove(right); //so that the list has only the wrong buttons

        //below is to pick three random but distinct wrong answers
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            //here I add to "list" numbers from 0-12 EXCLUDING the right one
            if (i != size)
                list.add(i);
        }
        Collections.shuffle(list);//shuffle it
        String wAnswer1 = getAnswer(list.get(0));
        String wAnswer2 = getAnswer(list.get(1));
        String wAnswer3 = getAnswer(list.get(2)); //here are three distinct wrong answers

        buttons.get(0).setText(wAnswer1);
        buttons.get(1).setText(wAnswer2);
        buttons.get(2).setText(wAnswer3); //set the three buttons to the wrong answers

        //then I will specify the results for clicking these buttons
        final Handler h = new Handler();

        //sets the three wrong buttons
        for (int j = 0; j<3 ; j++) {
            final Button b = buttons.get(j);
            final int wrongHigh = low + list.get(j); //the index of the wrong high note
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    attempts++;
                    updateRecord();
                    showToast(false);
                    playInterval(low, wrongHigh); //plays the wrong interval (as selected) based on the same low note
                    b.setTextColor(getResources().getColor(R.color.colorAccent)); //change the color to pink (to indicate that it was wrong)
                }
            });
        }

        //sets the right button
        rButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                attempts++; //updates the current record
                success++;
                updateRecord();
                final Intent intent = getIntent();
                intent.putExtra(IntervalActivity.SUCCESS, success); //delivers the current record
                intent.putExtra(IntervalActivity.ATTEMPTS, attempts);
                rButton.setTextColor(Color.GREEN); //change color to green
                showToast(true);
                playInterval(low, high);//plays the right interval
                Runnable r = new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                };
                h.postDelayed(r,600); //delay by 600
            }
        });

    }

    //shows the toast to display the result
    public void showToast(boolean boo) { //boo == true, if the answer is correct

        if (resultToast != null) //if there is already a visible toast, cancel it
            resultToast.cancel();

        //sets resultToast to appropriate text and color
        if (boo) {
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
        TextView record = (TextView) findViewById(R.id.textView5);
        DecimalFormat df = new DecimalFormat("#0.0");
        String accuracy;
        if (success == 0 && attempts == 0)
            accuracy = "N/A";
        else
            accuracy = df.format((double)100*success/attempts) + "%"; //keep 2 digits after .
        record.setText(getString(R.string._result_display_,success, attempts, accuracy)); //sets text to display results
    }

    //shows the answer (cheat button)
    public void showAnswer(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The correct choice is: " + correct + ".");
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
        final AlertDialog ad = builder.create();
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
                        playInterval(low, high);
                    }
                });
                b2.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view) {
                        ad.dismiss();
                        Intent intent = getIntent();
                        intent.putExtra(Interval2Activity.SUCCESS, success);
                        intent.putExtra(Interval2Activity.ATTEMPTS, attempts);
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
