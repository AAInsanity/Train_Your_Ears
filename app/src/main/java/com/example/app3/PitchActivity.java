package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class PitchActivity extends AppCompatActivity {

    public static final Random gen = new Random();
    private Handler handler;
    private String[] pitches;
    private String[] accidentals;
    private int correctPitch; //c or d.. (0-11) //used to determine the answer
    private int pitchIndex; //c_2 or c_3 (1-36) //used to get the actual note
    private ArrayList<Integer> theChord;
    private Toast resultToast;
    private ArrayList<String> answers;
    private String thePitch = "";
    private String theAccidental = "";
    private int success;
    private int attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch);

        handler = new Handler();

        pitches = new String[]{"c","d","e","f","g","a","b","Pitch"};
        accidentals = new String[]{"♮","♯","♭"};

        setSpinners();

        correctPitch = gen.nextInt(12);
        answers = getAnswers(correctPitch);
        System.out.println("answers: " + answers.toString());
        pitchIndex = ChordBuilder.pitchChart[correctPitch][gen.nextInt(3)]; //generate a random note, e.g from f get f_3
        System.out.println("pitch index: " + pitchIndex);
        theChord = ChordBuilder.buildChordContainingPitch(pitchIndex);

        success = getIntent().getIntExtra("SUC", 0);
        attempts = getIntent().getIntExtra("ATT", 0);
        updateRecord();
    }

    //sets the spinners
    public void setSpinners(){
        CustomSpinner cs1 = (CustomSpinner)findViewById(R.id.spinner3);
        CustomSpinner cs2 = (CustomSpinner)findViewById(R.id.spinner4);

        MySpinnerAdapter adapter1 = new MySpinnerAdapter(this,pitches);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,R.layout.my_spinner_layout,R.id.textViewSpinner,accidentals); //


        cs1.setAdapter(adapter1);
        cs1.setSelection(adapter1.getCount()); //display hint

        cs2.setAdapter(adapter2);


        cs1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                switch(i){
                    case 0:
                        thePitch = pitches[0];
                        break;
                    case 1:
                        thePitch = pitches[1];
                        break;
                    case 2:
                        thePitch = pitches[2];
                        break;
                    case 3:
                        thePitch = pitches[3];
                        break;
                    case 4:
                        thePitch = pitches[4];
                        break;
                    case 5:
                        thePitch = pitches[5];
                        break;
                    case 6:
                        thePitch = pitches[6];
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

        cs2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                switch(i){
                    case 0:
                        theAccidental = accidentals[0];
                        break;
                    case 1:
                        theAccidental = accidentals[1];
                        break;
                    case 2:
                        theAccidental = accidentals[2];
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });



    }

    //gets the literal answer(s) with a given
    public ArrayList<String> getAnswers(int p){

        ArrayList<String> answers = new ArrayList<>();

        switch(p){
            case 0:
                answers.add("c♮");
                break;
            case 1:
                answers.add("c♯");
                answers.add("d♭");
                break;
            case 2:
                answers.add("d♮");
                break;
            case 3:
                answers.add("d♯");
                answers.add("e♭");
                break;
            case 4:
                answers.add("e♮");
                break;
            case 5:
                answers.add("f♮");
                break;
            case 6:
                answers.add("f♯");
                answers.add("g♭");
                break;
            case 7:
                answers.add("g♮");
                break;
            case 8:
                answers.add("g♯");
                answers.add("a♭");
                break;
            case 9:
                answers.add("a♮");
                break;
            case 10:
                answers.add("a♯");
                answers.add("b♭");
                break;
            case 11:
                answers.add("b♮");
                break;
        }
        return answers;
    }

    //plays a specified pitch
    public static void playPitch(int pitch){
        MainActivity.msp.getSoundPool().play(MainActivity.msp.getPitchId().get(pitch),1,1,0,0,1);
    }

    //plays the chord, and then play the pitch (to identify) after 1 second
    public void playChordPitch(View view){
        ChordActivity.playChord(theChord);
        Runnable r = new Runnable(){
            public void run(){
                playPitch(pitchIndex);
            }
        };
        handler.postDelayed(r,1000);
    }

    //shows the dialog tO warn user about missing input
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please select a pitch before submitting!"); //message for dialog to display
        builder.setNeutralButton("I got it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    //show a toast indicate whether user is right or wrong
    public void showToast(boolean boo) { //boo == true, if the answer is correct

        if (resultToast != null) //if there is already a visible toast, cancel it
            resultToast.cancel();

        //sets resultToast to appropriate text and color
        if (boo) {
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

    public void submit(View view){

        final Intent intent = getIntent();

        if (thePitch.equals("")) { //if missing input for pitch
            showDialog();
        }
        else { //if not missing input
            attempts++;//adds to the count for attempts
            if (answers.contains(thePitch + theAccidental)) { //if answer is correct
                success++;
                intent.putExtra("SUC", success); //delivers the records to next activity
                intent.putExtra("ATT", attempts);
                showToast(true);
                final Runnable r = new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                };
                handler.postDelayed(r, 600); //delay the next activity by 600
            }
            else {
                showToast(false);
            }
            updateRecord();
        }
    }

    //generates the record display and updates
    public void updateRecord() {
        TextView record = (TextView) findViewById(R.id.textView12);
        DecimalFormat df = new DecimalFormat("#0.0");
        String accuracy;
        if (success == 0 && attempts == 0)
            accuracy = "N/A";
        else
            accuracy = df.format((double)100*success/attempts) + "%"; //keep 2 digits after .
        record.setText(getString(R.string._result_display_,success, attempts, accuracy)); //sets text to display results
    }

    //main menu
    public void mainMenu(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    //shows the answer
    public void showAnswer(final View view){
        String str;
        if (answers.size() == 1) {
            str = "The correct answer is " + answers.get(0) + ".";
        }
        else {
            str = "The correct answer is " + answers.get(0) + " or " + answers.get(1) + ".";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setCancelable(false); //dialog will not cancellable
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
        // and to use view.onclicklistener, the button must first be created
        // that why I need a onshowlistener to make sure that the view is created

        final AlertDialog ad = builder.create();

        ad.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                //since the dialog is shown, I can get the 2 buttons in the dialog
                Button b1 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
                Button b2 = ad.getButton(DialogInterface.BUTTON_POSITIVE);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        playChordPitch(view);
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        Intent intent = getIntent();
                        intent.putExtra("SUC", success);
                        intent.putExtra("ATT", attempts);
                        startActivity(intent);
                    }
                });
            }
        });

        ad.show();

    }


}
