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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//The class that supports the Interval2 (Fill in Blank) activity
public class Interval2Activity extends AppCompatActivity{

    public static final String EXTRA_MESSAGE = "com.example.app3.MESSAGE";
    public static final String SUCCESS = "com.example.app3.SUCCESS"; //delivers the current success number
    public static final String ATTEMPTS = "com.example.app3.ATTEMPTS"; //delivers the current attempt number

    private SoundPool sp;
    private int size;
    private int low;
    private int high;
    private Random gen;
    private HashMap<Integer, Integer> pitchId;
    private String[] qualities;
    private String[] sizes;
    private String theQuality;
    private String theSize;
    private ArrayList<String> answers; //list of acceptable answers
    private int success;
    private int attempts;
    private Toast resultToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval2);

        //create the random
        gen = new Random();

        //randomly create the interval size (from 0 to 12)
        size = gen.nextInt(13);

        //generates the list of acceptable answers with size
        answers= new ArrayList<>();
        getAnswers(size);

        //randomly generate the low and high pitch of the interval based on size
        low = gen.nextInt(36 - size) + 1;
        high = low + size;

        //get the pitchId hashmap from MainActivity;
        pitchId = MainActivity.msp.getPitchId();

        //get the SoundPool from MainActivity
        sp = MainActivity.msp.getSoundPool();

        //create the arrays for the spinner
        qualities = new String[]{"Major", "Minor", "Perfect", "Augmented", "Diminished","Select a Quality"};
        sizes = new String[]{"Unison", "2", "3", "4", "5", "6", "7", "Octave","Select a Size"};

        theQuality = "nothing";
        theSize = "nothing";

        //set the dropdown spinners
        setSpinners();

        //hook the play button up with playing the interval
        Button playButton = (Button)findViewById(R.id.button11);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {
                playInterval(low, high);
            }
        });

        //set up the submit button
        setSubmitButton();

        //set up the showAnswer button
        setShowAnswer();

        success = getIntent().getIntExtra(Interval2Activity.SUCCESS, 0);
        attempts = getIntent().getIntExtra(Interval2Activity.ATTEMPTS, 0);
        updateRecord();
    }

    //generates the record display and updates
    public void updateRecord() {
        TextView record = (TextView) findViewById(R.id.textView4);
        DecimalFormat df = new DecimalFormat("#0.0");
        String accuracy;
        if (success == 0 && attempts == 0)
            accuracy = "N/A";
        else
            accuracy = df.format((double)100*success/attempts) + "%"; //keep 2 digits after .
        record.setText(getString(R.string._result_display_,success, attempts, accuracy)); //sets text to display results
    }

    //sets the two drop down spinners
    public void setSpinners() {
        CustomSpinner spinner1 = (CustomSpinner) findViewById(R.id.spinner);
        CustomSpinner spinner2 = (CustomSpinner) findViewById(R.id.spinner2);

        MySpinnerAdapter adapter1 = new MySpinnerAdapter(this,qualities);
        MySpinnerAdapter adapter2 = new MySpinnerAdapter(this,sizes);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner1.setSelection(adapter1.getCount()); //display hint

        spinner2.setAdapter(adapter2);
        spinner2.setSelection(adapter2.getCount()); //display hint

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        theQuality = qualities[0];
                        break;
                    case 1:
                        theQuality = qualities[1];
                        break;
                    case 2:
                        theQuality = qualities[2];
                        break;
                    case 3:
                        theQuality = qualities[3];
                        break;
                    case 4:
                        theQuality = qualities[4];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //NOTHING
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        theSize = sizes[0];
                        break;
                    case 1:
                        theSize = sizes[1];
                        break;
                    case 2:
                        theSize = sizes[2];
                        break;
                    case 3:
                        theSize = sizes[3];
                        break;
                    case 4:
                        theSize = sizes[4];
                        break;
                    case 5:
                        theSize = sizes[5];
                        break;
                    case 6:
                        theSize = sizes[6];
                        break;
                    case 7:
                        theSize = sizes[7];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //NOTHING
            }
        });
    }

    //play the two notes that forms the interval
    public void playInterval(int low, int high) {
        //play the two pitches
        sp.play(pitchId.get(low),1,1,0,0,1);
        sp.play(pitchId.get(high),1,1,0,0,1);
    }

    //retrieve the literal answer(s) with a certain interval size
    public void getAnswers(int i) {

        switch (i) {

            case 0:
                answers.add("Perfect Unison");
                break;

            case 1:
                answers.add("Augmented Unison");
                answers.add("Minor 2");
                break;

            case 2:
                answers.add("Major 2");
                break;

            case 3:
                answers.add("Augmented 2");
                answers.add("Minor 3");
                break;

            case 4:
                answers.add("Major 3");
                break;

            case 5:
                answers.add("Perfect 4");
                break;

            case 6:
                answers.add("Augmented 4");
                answers.add("Diminished 5");
                break;

            case 7:
                answers.add("Perfect 5");
                break;

            case 8:
                answers.add("Augmented 5");
                answers.add("Minor 6");
                break;

            case 9:
                answers.add("Major 6");
                break;

            case 10:
                answers.add("Augmented 6");
                answers.add("Minor 7");
                break;

            case 11:
                answers.add("Major 7");
                break;

            case 12:
                answers.add("Perfect Octave");
                break;

            default: answers.add("Invalid Interval Size");
                break;
        }
    }

    //specifies the cases when the "submit" button is clicked.
    public void setSubmitButton() {

        Button submit = (Button) findViewById(R.id.button12);
        final Handler h = new Handler();

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                final Intent intent = getIntent();

                if (theQuality.equals("nothing") || theSize.equals("nothing")) { //if missing input
                    showDialog();
                }
                else { //if not missing input
                    attempts++;//adds to the count for attempts
                    if (answers.contains(theQuality + " " + theSize)) { //if answer is correct
                        success++;
                        intent.putExtra(Interval2Activity.SUCCESS, success); //delivers the records to next activity
                        intent.putExtra(Interval2Activity.ATTEMPTS, attempts);
                        showToast(true);
                        final Runnable r = new Runnable() {
                            public void run() {
                                startActivity(intent);
                            }
                        };
                        h.postDelayed(r, 600); //delay the next activity by 600
                    }
                    else {
                        showToast(false);
                    }
                    updateRecord();
                }
            }
        });
    }

    //shows a dialog when missing answer inputs (quality or size)
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please select both the interval \nQUALITY & SIZE before submitting!"); //message for dialog to display
        builder.setNeutralButton("I got it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();

    }

    //shows the toast to display the result
    public void showToast(boolean boo) { //boo == true, if the answer is correct

        if (resultToast != null) //if there is already a visible toast, cancel it
            resultToast.cancel();

        //sets resultToast to appropriate text and color
        if (boo == true) {
            //generate toast
            resultToast = Toast.makeText(this, "You're right. Congratulations!", Toast.LENGTH_SHORT);
            //sets color
            ((TextView)resultToast.getView().findViewById(android.R.id.message)).setTextColor(Color.GREEN);
        }
        else {
            //generate toast
            resultToast = Toast.makeText(this, "That's incorrect. Try again.", Toast.LENGTH_SHORT);
            //sets color
            ((TextView)resultToast.getView().findViewById(android.R.id.message)).setTextColor(getResources().getColor(R.color.colorAccent));
        }
            resultToast.setGravity(Gravity.CENTER, 0, 0);
            resultToast.show();
    }

    //shows the answer (cheat button)
    public void showAnswer() {

        final String str;

        if (answers.size() == 1)  //build a string that represents the correct answer
            str = answers.get(0);
        else
            str = answers.get(0) + " or " + answers.get(1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The correct answer is: " + str + ".");
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

    //set the cheat button with showAnswer
    public void setShowAnswer() {
        ImageButton iButton = (ImageButton)findViewById(R.id.imageButton);
        iButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                showAnswer();
            }
        });
    }

    //back to main activity
    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
