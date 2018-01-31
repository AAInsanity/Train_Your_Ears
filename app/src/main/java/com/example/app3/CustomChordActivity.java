package com.example.app3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CustomChordActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<CustomChord> mData;
    private CustomChordAdapter mAdapter;
    private EditText editText;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private ArrayList<PianoButton> pianoButtons; //contains all the piano buttons
    private HashMap<String, int[]> mapPitches; //a temporary map that saves all the name-pitch KeyValuePairs (to search for any duplicate name or pitches)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_chord);

        gson = new Gson(); //initialize a new Gson

        mapPitches =ChordBuilder.mapPitches; //here, mapPitches contains all the built-in chords
        ChordBuilder.loadCustomChords(mapPitches); //loads the custom chords too

        //gets the sharedpreferences and its editor
        prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);
        editor = prefs.edit();


        //the recyclerview where displays all the added chord
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        //gets the json String in SharedPreference that contains all the CustomChords
        String customChords = prefs.getString("Custom Chords", null);
        System.out.println(customChords);

        if (customChords == null) { //if no custom chord is added
            mData = new ArrayList<>(); //create a empty list
        }
        else {
            mData = gson.fromJson(customChords, new TypeToken<List<CustomChord>>(){}.getType());
            //build the mData with the string CustomChords from sharedpreference
        }

        //sets up the grid recyclerview
        mAdapter = new CustomChordAdapter(CustomChordActivity.this, mData);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridManager = new GridLayoutManager(this, 3); //3 items in each row
        mRecyclerView.setLayoutManager(gridManager);

        //sets up the ItemTouchHelper
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                String name = mData.get(position).getChordName();
                mapPitches.remove(name); //removes the corresponding name-pitches KeyValuePair from mapPitches

                mData.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }
        });
        helper.attachToRecyclerView(mRecyclerView);

        //editText to put in the chord name
        editText = (EditText)findViewById(R.id.editText);

        pianoButtons = new ArrayList<>();
        //adds the piano buttons to "pianoButtons"
        pianoButtons.add((PianoButton)findViewById(R.id.button_c3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_c_3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_d3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_d_3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_e3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_f3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_f_3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_g3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_g_3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_a3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_a_3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_b3));
        pianoButtons.add((PianoButton)findViewById(R.id.button_c4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_c_4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_d4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_d_4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_e4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_f4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_f_4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_g4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_g_4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_a4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_a_4));
        pianoButtons.add((PianoButton)findViewById(R.id.button_b4));

    }

    //returns the pitches that the user currently selects on the piano layout
    public ArrayList<Integer> getCurrentPitches(boolean lowestToZero){

        //lowestToZero is true when you want the lowest pitch to be 0;
        //e.g when the piano input is {3,5,7}, you get {0,2,4}
        //this should be true when you want to save the pitches
        //should be false when you want to play the pitches


        ArrayList<Integer> pitches= new ArrayList<>();

        for (PianoButton pb: pianoButtons) { //loop through all 24 piano buttons
            if (pb.isSelected()) //if the button is selected
                pitches.add(pb.getIndex()); //then adds the corresponding index to "pitches"
        }

        //here, pitches should contain the indices of the selected pitches

        if (lowestToZero) {
            int minPitch = Collections.min(pitches); //the lowest pitch selected (note: this will crash if "pitches" is empty)
            for (int i = 0; i < pitches.size(); i++) {
                int original = pitches.get(i);
                pitches.set(i, original - minPitch); //make sure that the lowest note in the chord is 0;
            }
        }

        //here, pitches should contain the selected pitches' indices with the lowest note's index being 0;

        System.out.println("Pitches: " + pitches.toString());

        return pitches;
    }

    //sets all the pianoButtons to be
    //1. not Selected (this controls what happens when it is clicked again)
    //2. not Activated (this controls the image displayed)
    public void resetAllPianoButtons() {
        for (PianoButton pb: pianoButtons) {
            pb.setSelected(false);
            pb.setActivated(false);
        }
    }



    //to add a new chord
    public void addChord(View view) {

        if (editText.getText().toString().length()!=0 &&
                getCurrentPitches(false).size()!=0) { //if edittext and pianolayout aren't empty

            String name = editText.getText().toString();
            ArrayList<Integer> pitches = getCurrentPitches(true);

            if (!namePresent(name) && !pitchesPresent(pitches)) { //if both name and pitches are new to database

                //adds the new customchord created with the text from edittext and the pitches from the piano layout
                mData.add(0, new CustomChord(name, pitches));

                //sets the piano buttons to be up
                resetAllPianoButtons();

                //clear the edit text
                editText.setText("");

                //notifies the recyclerview to adapt the change
                mAdapter.notifyItemInserted(0);
                mRecyclerView.smoothScrollToPosition(0);

                //updates the mapPitches (in order to check for same name or pitches)
                mapPitches.put(name,toIntArray(pitches));

            }

            else if (pitchesPresent(pitches) && namePresent(name)){ // if both the name and pitches are present
                showDialog("name and pitches");
            }

            else if (pitchesPresent(pitches)){ //if only the pitches are present
                showDialog("pitches");
            }

            else if (namePresent(name)){ //if only the name is present
                showDialog("name");
            }

        }
        //if edittext or piano is empty then show dialog
        else{
            showDialog2();
        }
    }

    //translates a arraylist<integer> to int[]
    public static int[] toIntArray(ArrayList<Integer> list){
        int[] array = new int[list.size()];
        for (int i=0; i<list.size();i++){
            array[i] = list.get(i);
        }
        return array;
    }

//    //save any new chord added to the sharedprefrence
//    public void applyChanges() {
//        String CustomChords = gson.toJson(mData); //converts the updated mData to a String
//        editor.putString("Custom Chords", CustomChords); //put it in prefs under "Custom Chords"
//        editor.apply();
//        System.out.println(CustomChords);
//    }

    //save any new chord added to the sharedprefrence
    public void apply(View view) {
        String CustomChords = gson.toJson(mData); //converts the updated mData to a String
        editor.putString("Custom Chords", CustomChords); //put it in prefs under "Custom Chords"
        editor.apply();
//        System.out.println(CustomChords);

        Intent intent = new Intent(this,ChordSelectActivity.class);
        startActivity(intent);
    }

    //play the current selected pitches all together
    public void playCurrentPitches(View view) {
        ArrayList<Integer> currentPitches = getCurrentPitches(false);
        //System.out.println("current pitches: " + currentPitches.toString());

        SoundPool sp = MainActivity.msp.getSoundPool();
        HashMap<Integer, Integer> pitchId = MainActivity.msp.getPitchId();

        for (int p: currentPitches) {
            sp.play(pitchId.get(p+7),1,1,0,0,1);
        }

    }

    //check if the name is already present in the database
    public boolean namePresent(String name){
        boolean boo = false;

        if (mapPitches.containsKey(name.toLowerCase())) { //if the current mapPitches already has name as one of its keys
            boo = true;
        }

        return boo;
    }

    //check if the pitches is already present in the database
    public boolean pitchesPresent(ArrayList<Integer> currentPitches){

        boolean boo = false;

        int[] pitchArray = toIntArray(currentPitches);

        for (int[] a1: mapPitches.values()){
            if (Arrays.equals(a1,pitchArray)){ //compare if the current selected pitches array is equal to any
                                                //array that is already present
                boo = true;
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

//    //Go back to the chord select activity
//    public void toChord(View view) {
//        Intent intent = new Intent(this, ChordSelectActivity.class);
//        startActivity(intent);
//    }

    public void showDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please put in both the chord's name and pitches before adding the chord.");
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
