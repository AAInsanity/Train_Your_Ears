package com.example.app3;

import android.util.Pair;

import java.util.ArrayList;


/**
 * Created by dengyitong on 2018/1/16.
 */

public class Progression {
    private ArrayList<KeyValuePair<Integer, String>> content;

    public Progression(KeyValuePair<Integer, String>... chords) {
        content = new ArrayList<>();
        for (KeyValuePair<Integer, String> chord : chords) {
            content.add(chord);
        }
    }

    public int getChordDegree(int index) {
        if (index > -1 && index < content.size()) {
            return content.get(index).getKey();
        } else return 100;
    }

    public String getChordQuality(int index) {
        if (index > -1 && index < content.size()) {
            return content.get(index).getValue();
        } else return "index out of bound";
    }

    public int size() {
        return content.size();
    }

    public void addChord(Integer degree, String quality){
        content.add(new KeyValuePair(degree,quality));
    }

    public void setDegree(int index, int degree) {
        content.get(index).setKey(degree);
    }

    public void setQuality(int index, String quality) {
        content.get(index).setValue(quality);
    }

    public String toString(){
        String str;
        str =content.toString();
        return str;
    }

    public ArrayList<KeyValuePair<Integer, String>> getContent(){
        return content;
    }

}
