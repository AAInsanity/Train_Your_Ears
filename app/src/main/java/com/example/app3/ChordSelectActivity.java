package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.app3.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ChordSelectActivity extends AppCompatActivity {

    public static final String MY_PREFERENCE = "preference";
    private SharedPreferences.Editor editor;
    public SharedPreferences prefs;
    public Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chord_select);

        gson = new Gson();

        //the shared preference that I store my things in;
        prefs = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);

        //the editor for the shared-preference named MY_PREFERENCE
        editor = prefs.edit();

        //sets the default condition for the checkboxes and switches
        setCheckboxDefault();
        setSwitchDefault();

        //sets the switches
        setOnSwitch();

    }

    //sets the actions when an item in the options menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //if the home button is selected
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //sets the default condition of the checkboxes (whether the button is initially clicked)
    public void setCheckboxDefault() {

        //gets the checkboxes that exist
        CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox);
        CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox cb4 = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox cb5 = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox cb6 = (CheckBox)findViewById(R.id.checkBox6);
        CheckBox cb7 = (CheckBox)findViewById(R.id.checkBox7);
        CheckBox cb8 = (CheckBox)findViewById(R.id.checkBox8);
        CheckBox cb9 = (CheckBox)findViewById(R.id.checkBox9);
        CheckBox cb10 = (CheckBox)findViewById(R.id.checkBox10);
        CheckBox cb11 = (CheckBox)findViewById(R.id.checkBox11);
        CheckBox cb12 = (CheckBox)findViewById(R.id.checkBox12);
        CheckBox cb13 = (CheckBox)findViewById(R.id.checkBox13);
        CheckBox cb14 = (CheckBox)findViewById(R.id.checkBox14);
        CheckBox cb15 = (CheckBox)findViewById(R.id.checkBox15);
        CheckBox cb16 = (CheckBox)findViewById(R.id.checkBox16);
        CheckBox cb17 = (CheckBox)findViewById(R.id.checkBox17);
        CheckBox cb18 = (CheckBox)findViewById(R.id.checkBox18);
        CheckBox cb19 = (CheckBox)findViewById(R.id.checkBox19);
        CheckBox cb20 = (CheckBox)findViewById(R.id.checkBox20);
        CheckBox cb21 = (CheckBox)findViewById(R.id.checkBox21);


        Boolean checked1 = prefs.getBoolean("maj3", false);
        Boolean checked2 = prefs.getBoolean("min3", false);
        Boolean checked3 = prefs.getBoolean("aug3", false);
        Boolean checked4 = prefs.getBoolean("dim3", false);
        Boolean checked5 = prefs.getBoolean("sus4", false);
        Boolean checked6 = prefs.getBoolean("sus2", false);
        Boolean checked7 = prefs.getBoolean("maj7", false);
        Boolean checked8 = prefs.getBoolean("min7", false);
        Boolean checked9 = prefs.getBoolean("dom7", false);
        Boolean checked10 = prefs.getBoolean("halfdim7", false);
        Boolean checked11 = prefs.getBoolean("dim7", false);
        Boolean checked12 = prefs.getBoolean("augmaj7", false);
        Boolean checked13 = prefs.getBoolean("minmaj7", false);
        Boolean checked14 = prefs.getBoolean("maj9", false);
        Boolean checked15 = prefs.getBoolean("min9", false);
        Boolean checked16 = prefs.getBoolean("dom9", false);
        Boolean checked17 = prefs.getBoolean("dommin9", false);
        Boolean checked18 = prefs.getBoolean("s5s9", false); //s = sharp; f = flat
        Boolean checked19 = prefs.getBoolean("s5f9", false);
        Boolean checked20 = prefs.getBoolean("f5s9", false);
        Boolean checked21 = prefs.getBoolean("f5f9", false);


        cb1.setChecked(checked1);
        cb2.setChecked(checked2);
        cb3.setChecked(checked3);
        cb4.setChecked(checked4);
        cb5.setChecked(checked5);
        cb6.setChecked(checked6);
        cb7.setChecked(checked7);
        cb8.setChecked(checked8);
        cb9.setChecked(checked9);
        cb10.setChecked(checked10);
        cb11.setChecked(checked11);
        cb12.setChecked(checked12);
        cb13.setChecked(checked13);
        cb14.setChecked(checked14);
        cb15.setChecked(checked15);
        cb16.setChecked(checked16);
        cb17.setChecked(checked17);
        cb18.setChecked(checked18);
        cb19.setChecked(checked19);
        cb20.setChecked(checked20);
        cb21.setChecked(checked21);


        //to add more
            


    }

    //sets the default state of the switch buttons
    public void setSwitchDefault() {
        Switch s1 = (Switch)findViewById(R.id.switch1);
        Switch s2 = (Switch)findViewById(R.id.switch2);
        Switch s3 = (Switch)findViewById(R.id.switch3);
        s1.setChecked(prefs.getBoolean("root", false));
        s2.setChecked(prefs.getBoolean("octaveless", false));
        s3.setChecked(prefs.getBoolean("simple", false));
    }


    //sets the reactions for the three switch buttons
    public void setOnSwitch() {
        Switch s1 = (Switch)findViewById(R.id.switch1); //whether root
        Switch s2 = (Switch)findViewById(R.id.switch2); //whether octaveless
        Switch s3 = (Switch)findViewById(R.id.switch3); //whether simple
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //sets whether simple
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    editor.putBoolean("root", true);
                else
                    editor.putBoolean("root", false);
                //editor.apply();
            }
        });
        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //sets whether root
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    editor.putBoolean("octaveless", true);
                else
                    editor.putBoolean("octaveless", false);
                //editor.apply();
            }
        });
        s3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //sets whether root
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    editor.putBoolean("simple", true);
                else
                    editor.putBoolean("simple", false);
                //editor.apply();
            }
        });

    }

    //sets the reactions for clicking the check boxes
    public void onCheckboxesClicked(View v) {

        // Is the view now checked?
        boolean checked = ((CheckBox) v).isChecked();

        switch (v.getId()) {

            //if is the button for major triad
            case R.id.checkBox:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("maj3", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("maj3", false);
                }
                break;

            //if is the button for minor triad
            case R.id.checkBox2:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("min3", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("min3", false);
                }
                break;

            //if is the button for augmented triad
            case R.id.checkBox3:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("aug3", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("aug3", false);
                }
                break;

            //if is the button for diminished triad
            case R.id.checkBox4:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("dim3", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("dim3", false);
                }
                break;

            //if is the button for sus4 triad
            case R.id.checkBox5:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("sus4", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("sus4", false);
                }
                break;

            //if is the button for sus2 triad
            case R.id.checkBox6:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("sus2", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("sus2", false);
                }
                break;

            //if is the button for major seventh
            case R.id.checkBox7:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("maj7", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("maj7", false);
                }
                break;

            //if is the button for minor seventh
            case R.id.checkBox8:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("min7", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("min7", false);
                }
                break;

            //if is the button for dominant seventh
            case R.id.checkBox9:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("dom7", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("dom7", false);
                }
                break;

            //if is the button for half diminished seventh
            case R.id.checkBox10:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("halfdim7", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("halfdim7", false);
                }
                break;

            //if is the button for diminished seventh
            case R.id.checkBox11:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("dim7", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("dim7", false);
                }
                break;

            //if is the button for aug/maj seventh
            case R.id.checkBox12:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("augmaj7", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("augmaj7", false);
                }
                break;

            //if is the button for min/maj seventh
            case R.id.checkBox13:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("minmaj7", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("minmaj7", false);
                }
                break;

            //if is the button for major 9
            case R.id.checkBox14:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("maj9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("maj9", false);
                }
                break;

            //if is the button for minor 9
            case R.id.checkBox15:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("min9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("min9", false);
                }
                break;

            //if is the button for dominant 9
            case R.id.checkBox16:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("dom9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("dom9", false);
                }
                break;

            //if is the button for dominant minor 9
            case R.id.checkBox17:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("dommin9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("dommin9", false);
                }
                break;

            //if is the button for altered #5#9
            case R.id.checkBox18:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("s5s9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("s5s9", false);
                }
                break;

            //if is the button for altered #5b9
            case R.id.checkBox19:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("s5f9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("s5f9", false);
                }
                break;

            //if is the button for altered b5#9
            case R.id.checkBox20:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("f5s9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("f5s9", false);
                }
                break;

            //if is the button for altered b5b9
            case R.id.checkBox21:
                if (checked) {
                    //if is checked, then put true
                    editor.putBoolean("f5f9", true);
                } else {
                    //if not checked, then put false
                    editor.putBoolean("f5f9", false);
                }
                break;

            //to add more
        }
//        editor.apply();
    }

    //activates when the "apply" button is hit, apply the changes and go to play chord activity (if nothing is selected then show dialog)
    public void toPlayChord(View view) {
        editor.apply();
        if (isEmpty()) //if nothing selected then show dialog
            showDialog();
        else { //else go to play chord and apply the changes
            Intent intent = new Intent(this, ChordActivity.class);
            startActivity(intent);
        }
    }

    //see if there is no chord quality selected for the chord activity
    public boolean isEmpty() {

        boolean boo = true; //assume there is no chords selected

        Map<String,?> mapDefChords = prefs.getAll(); //mapDefChords is the prefs' map of chords (everything but without root, simple, octaveless or Custom Chord)\
                                                    //aka all the default chords
        mapDefChords.remove("root");
        mapDefChords.remove("octaveless");
        mapDefChords.remove("simple");
        mapDefChords.remove("Custom Chords");
        mapDefChords.remove("Selected Progressions");
        System.out.println(mapDefChords.toString());

        if (mapDefChords.containsValue(true)) { //if any of the default chords is selected to be true
            System.out.println("1");
            boo = false;
        }
        else { //if none of the default chords are selected
                //then to make isEmpty = false, "Custom Chords" must be present and some of its custom chords must be selected

            String customChord = prefs.getString("Custom Chords", null);

            if (customChord != null) { //Custom Chords is present
                ArrayList<CustomChord> listChords = gson.fromJson(customChord, new TypeToken<List<CustomChord>>(){}.getType()); //translates the string to arraylist of custom chords
                for (CustomChord c1: listChords) {
                    if (c1.isSelected()) {//if any one chord in Custom Chords is selected
                        System.out.println("2");
                        boo = false;
                        break;
                    }
                }
            }
        }

        System.out.println("boo: " + boo);
        return boo;
    }

    //set up the dialog to show if no chords were selected
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please select at lease ONE type of chords you want for the drill.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    //go to custom chord activity
    public void toCustomChord(View view) {
        Intent intent = new Intent(this,CustomChordActivity.class);
        startActivity(intent);
    }

}
