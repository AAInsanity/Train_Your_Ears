package com.example.app3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dengyitong on 7/26/17.
 */

public class CustomChord {

    // dont forget to delete the shared preference when the custom chord is deleted
    private String chordName;
    private Boolean isSelected;
    private ArrayList<Integer> pitches;

    public CustomChord(String chordName, ArrayList<Integer> pitches) {

        this.chordName = chordName;

        this.isSelected = true;

        this.pitches = pitches;

    }

    public String getChordName() {
        return chordName;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean boo) {
        isSelected = boo;
    }

    public ArrayList<Integer> getPitches() { return pitches; }



}
