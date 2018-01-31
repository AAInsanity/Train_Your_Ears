package com.example.app3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomProgressionActivity1 extends AppCompatActivity {

    public static String CHORD_NUMBER = "number of chords";
    private int numChords = -1;
    private Gson gson = new Gson();
    private Progression progression;
    private RecyclerView mRecyclerView1;
    private DefaultProgressionAdapter mAdapter1;
    private HashMap<String, Progression> customDict;
    private ArrayList<String> allCustom;
    private ArrayList<String> selectedCustom;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progression1);

        setSpinners();
    }

    //sets the spinners
    public void setSpinners(){
        CustomSpinner cs = (CustomSpinner)findViewById(R.id.customSpinner3);

        MySpinnerAdapter adapter1 = new MySpinnerAdapter(this, new String[]{"1","2","3","4","5","6","7","8","# of Chords"});

        cs.setAdapter(adapter1);
        cs.setSelection(adapter1.getCount());

        cs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
                switch(i){
                    case 0:
                        numChords = 1;
                        break;
                    case 1:
                        numChords = 2;
                        break;
                    case 2:
                        numChords = 3;
                        break;
                    case 3:
                        numChords = 4;
                        break;
                    case 4:
                        numChords = 5;
                        break;
                    case 5:
                        numChords = 6;
                        break;
                    case 6:
                        numChords = 7;
                        break;
                    case 7:
                        numChords = 8;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){

            }
        });

        //gets the sharedpreferences and its editor
        prefs = getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, MODE_PRIVATE);
        editor = prefs.edit();

        //gets the json String in SharedPreference that contains all the custom progressions
        String customProgressions = prefs.getString("Custom Progressions", null);
        System.out.println(customProgressions);

        if (customProgressions == null) { //if no custom progression is added
            customDict = new HashMap<>(); //create a empty map
        }
        else {
            customDict = gson.fromJson(customProgressions, new TypeToken<HashMap<String,Progression>>(){}.getType());
        }


        //gets the json String in SharedPreference that contains the selected custom progressions
        String selectedCustomProgressions = prefs.getString("Selected Custom Progressions", null);
        System.out.println(selectedCustomProgressions);
        if (selectedCustomProgressions == null) { //if no custom progression is selected
            selectedCustom = new ArrayList<>(); //create a empty List
        }
        else {
            selectedCustom = gson.fromJson(selectedCustomProgressions, new TypeToken<List<String>>(){}.getType());
        }


        //sets up recycler view for 4 chord progressions
        mRecyclerView1 = (RecyclerView) findViewById(R.id.RV3);
        //sets up the grid recyclerview
        allCustom = new ArrayList(customDict.keySet());
        System.out.println("All Custom Size: "  + allCustom.size());
        mAdapter1 = new DefaultProgressionAdapter(CustomProgressionActivity1.this, allCustom, selectedCustom, 1);
        mRecyclerView1.setAdapter(mAdapter1);
        GridLayoutManager gridManager = new GridLayoutManager(this, 2); //3 items in each row
        mRecyclerView1.setLayoutManager(gridManager);
        //sets up the ItemTouchHelper
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                String name = allCustom.get(position);
                customDict.remove(name); //removes the corresponding name-pitches KeyValuePair from mapPitches
                allCustom.remove(name);
                selectedCustom.remove(name);

//                mData.remove(position);
                mAdapter1.notifyItemRemoved(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }
        });
        helper.attachToRecyclerView(mRecyclerView1);

    }

    public void toBuildProgression(View view){
        if (numChords < 1) showDialog();
        else {
            Intent intent = new Intent(this, CustomProgressionActivity2.class);
            intent.putExtra(CHORD_NUMBER, numChords);
            startActivity(intent);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 123) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                String result = data.getStringExtra("prog");
//                progression = gson.fromJson(result, new TypeToken<Progression>(){}.getType());
//            }
//            else if (resultCode == Activity.RESULT_CANCELED) {
//                //do nothing
//            }
//        }
//    }

//    public void addProgression(View view){
//        EditText et = (EditText)findViewById(R.id.editText);
//        String name = et.getText().toString();
//        if (progression != null) System.out.println("name is: " + name + "\nProgression is: " + progression.toString());
//    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please select how many chords you want for the new progression.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    public void apply(View view){
        String selectedCustomProgressions = gson.toJson(selectedCustom); //converts the updated selectedCustomProgressions to a String
        String customProgressions = gson.toJson(customDict); //converts the updated selectedCustomProgressions to a String
        editor.putString("Selected Custom Progressions", selectedCustomProgressions); //put it in prefs under "Selected Progressions"
        editor.putString("Custom Progressions", customProgressions); //put it in prefs under "Selected Progressions"
        editor.apply();
        Intent intent = new Intent(this, ProgressionSelectActivity.class);
        startActivity(intent);
    }

}
