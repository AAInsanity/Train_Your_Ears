package com.example.app3;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by dengyitong on 7/12/17.
 */

public class ChordBuilder {

    public static final int[][] pitchChart; //2-d array

    static{
        pitchChart = new int[][]{
                /*0*/{7,19,31}, //c
                /*1*/{8,20,32}, //c#
                /*2*/{9,21,33}, //d
                /*3*/{10,22,34}, //d#
                /*4*/{11,23,35}, //e
                /*5*/{12,24,36}, //f
                /*6*/{1,13,25}, //f#
                /*7*/{2,14,26}, //g
                /*8*/{3,15,27}, //g#
                /*9*/{4,16,28}, //a
                /*10*/{5,17,29}, //a#
                /*11*/{6,18,30}, //b
        }; //12 pitches and their respective choices
    }

    private Boolean octaveless; //whether octaveless

    public static final Random gen = new Random();

    public static final SharedPreferences prefs= MyApplication.getAppContext().getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, Context.MODE_PRIVATE);

    public static final Gson gson = new Gson();

    public static final HashMap<String, int[]> mapPitches = new HashMap<>();

    static{
        //adds the built-in chords
        mapPitches.put("major triad", new int[]{0,4,7});
        mapPitches.put("minor triad", new int[]{0,3,7});
        mapPitches.put("augmented triad", new int[]{0,4,8});
        mapPitches.put("diminished triad", new int[]{0,3,6});
        mapPitches.put("sus 4 triad", new int[]{0,5,7});
        mapPitches.put("sus 2 triad", new int[]{0,2,7});
        mapPitches.put("major seventh", new int[]{0,4,7,11});
        mapPitches.put("minor seventh", new int[]{0,3,7,10});
        mapPitches.put("dominant seventh", new int[]{0,4,7,10});
        mapPitches.put("half diminished seventh", new int[]{0,3,6,10});
        mapPitches.put("diminished seventh", new int[]{0,3,6,9});
        mapPitches.put("aug/maj seventh", new int[]{0,4,8,11});
        mapPitches.put("min/maj seventh", new int[]{0,3,7,11});
        mapPitches.put("major ninth", new int[]{0,4,7,11,14});
        mapPitches.put("minor ninth", new int[]{0,3,7,10,14});
        mapPitches.put("dominant ninth", new int[]{0,4,7,10,14});
        mapPitches.put("dom/min ninth", new int[]{0,4,7,10,13});
        mapPitches.put("altered ♯5♯9", new int[]{0,4,8,10,15});
        mapPitches.put("altered ♯5♭9", new int[]{0,4,8,10,13});
        mapPitches.put("altered ♭5♯9", new int[]{0,4,6,10,15});
        mapPitches.put("altered ♭5♭9", new int[]{0,4,6,10,13});
    }

//    private Gson gson;

    //constructor
    public ChordBuilder() {

        //prefs = MyApplication.getAppContext().getSharedPreferences(ChordSelectActivity.MY_PREFERENCE, Context.MODE_PRIVATE);
        octaveless = prefs.getBoolean("octaveless",false);

//        gson = new Gson();

        loadCustomChords(mapPitches);

    }

    //returns the mapPitches
    public HashMap<String, int[]> getMapPitches(){
        return mapPitches;
    }

    //reads the sharedpreference, and for any custom chords, add its name and the corresponding pithces
    //to the map "mapPitches"
    public static void loadCustomChords(HashMap<String, int[]> mapPitches){

        //retrieve the saved customchords from prefs
        String customChords = prefs.getString("Custom Chords", null);

        if (customChords != null) {
            //unpack the customchords (from string to arraylist of custom chords) with gson
            ArrayList<CustomChord> listChords = gson.fromJson(customChords, new TypeToken<List<CustomChord>>(){}.getType());

            for (CustomChord c1: listChords) { //loop through the list of saved customchords
                ArrayList<Integer> pitches = c1.getPitches(); //gets the ArrayList<Integer> of the pitches of the chord
                //System.out.println(pitches.toString());
                int[] pitchesArray = new int[pitches.size()]; //creates a new int[] of the same size
                for (int i=0; i<pitches.size(); i++) { //copy the elements from the arraylist to array
                    pitchesArray[i] = pitches.get(i);
                }
                mapPitches.put(c1.getChordName(),pitchesArray);
            }
        }
    }


    //given the quality and base note of the chord, return its pitches (divided by 12)
    public ArrayList<Integer> getPitches(int baseNote, String quality) {

        ArrayList<Integer> pitches = new ArrayList<>();

        if (mapPitches.containsKey(quality)) { // if such a quality is present in the map
            int[] pitchesArray = mapPitches.get(quality);
            for (int i : pitchesArray) {
                pitches.add((baseNote + i) % 12);
            }
        } else {
            pitches.add(baseNote);
        }
        return pitches;
    }



    //given the quality and base note of the chord, return its pitches (not divided by 12)
    public static ArrayList<Integer> getPitches2(int baseNote, String quality) {

        ArrayList<Integer> pitches = new ArrayList<>();

        if (mapPitches.containsKey(quality)){
            int[] pitchesArray = mapPitches.get(quality);
            for(int i: pitchesArray) {
                pitches.add(baseNote+i);
            }
        }
        else {
            pitches.add(baseNote);
        }


        return pitches;

    }

    //builds a random chord voicing given the chord quality
    public ArrayList<Integer> buildRandomChord(String quality) {

        ArrayList<Integer> pitchesToPlay = new ArrayList<>(); //contains the pitches to be played

        Random gen = new Random();
        int baseNote = gen.nextInt(12); //randomly pick one (among 0-11) as base note
        System.out.println("base note: " + baseNote); //one among 0-11

        //represents the pitches the chord has, e.g: d, f, a.
        //doesn't specify which d or which f
        ArrayList<Integer> pitchesInChord = getPitches(baseNote, quality);

        ArrayList<Integer> random3 = new ArrayList<>();
        random3.addAll(Arrays.asList(0,1,2)); //a helper list

        for (int i: pitchesInChord) {
            Collections.shuffle(random3);
            int r1 = random3.get(0);
            int r2 = random3.get(1); //r1, r2 are two random distinct numbers in 0-2
            pitchesToPlay.add(pitchChart[i][r1]); //adds a random choice of the first pitch
            if (!octaveless && gen.nextFloat()<0.3)
                pitchesToPlay.add(pitchChart[i][r2]); //if octaveless is not selected, then by 30% chance there will be two, e.g c2 + c3
        }

        return pitchesToPlay;
    }

    //builds a random chord voicing (in root position) given the chord quality
    public ArrayList<Integer> buildRandomChordRoot(String quality) {

        ArrayList<Integer> pitchesToPlay = new ArrayList<>(); //contains the pitches to be played

        Random gen = new Random();
        int baseNote = gen.nextInt(12); //randomly pick one (among 0-12) as base note
        System.out.println("base note: " + baseNote); //one among 0-11

        int base1 = pitchChart[baseNote][0];
        int base2 = pitchChart[baseNote][1];
        int base3 = pitchChart[baseNote][2]; //here are the base note in three different octaves

        //other pitches in the chord, for example, if a chord is c major triad (c,e,g)
        //then "otherPitches" will contain e and g.
        ArrayList<Integer> otherPitches = getPitches(baseNote, quality); //returns the full chord
        otherPitches.remove(Integer.valueOf(baseNote));//remove the base note

        ArrayList<Integer> random3 = new ArrayList<>();
        random3.addAll(Arrays.asList(0, 1, 2)); //a helper list

        for (int i : otherPitches) {
            Collections.shuffle(random3);
            while (pitchChart[i][random3.get(0)] < base1 || pitchChart[i][random3.get(1)] < base1)
                Collections.shuffle(random3); //this is to make sure that I have 2 available options (not lower than the lowest base note)

            int r1 = random3.get(0);
            int r2 = random3.get(1); //r1, r2 are two random distinct numbers in 0-2
            pitchesToPlay.add(pitchChart[i][r1]); //adds a random choice of the first pitch
            if (!octaveless && gen.nextFloat() < 0.3)
                pitchesToPlay.add(pitchChart[i][r2]); //if octaveless is not selected, then by 30% chance there will be two, e.g c2 + c3
        }

        //here i've done selecting all the other notes other than the base note
        //its time to choose our base notes

        int lowest = Collections.min(pitchesToPlay); //the lowest note (other than base note) selected
        Collections.shuffle(random3);
        if (base1 < lowest && lowest < base2) { //this means base1 must be added
            while (pitchChart[baseNote][random3.get(0)] != base1)
                Collections.shuffle(random3); //make sure "pitchChart[baseNote][random3.get(0)]" is base1
        } else if (base2 < lowest && lowest < base3) { //this means one of base1, base2 must be added
            while (pitchChart[baseNote][random3.get(0)] != base1 && pitchChart[baseNote][random3.get(0)] != base2)
                Collections.shuffle(random3); //make sure "pitchChart[baseNote][random3.get(0)]" is either base1 or base2
        }
        //if else, then adding either base1, base2 or base3 would work, so no need to restrict anything

        pitchesToPlay.add(pitchChart[baseNote][random3.get(0)]);
        if (!octaveless && gen.nextFloat() < 0.3)
            pitchesToPlay.add(pitchChart[baseNote][random3.get(1)]);
        if (!octaveless && gen.nextFloat() < 0.3)
            pitchesToPlay.add(pitchChart[baseNote][random3.get(2)]);

        return pitchesToPlay;
    }

    //builds a random chord voicing (in root position) given the chord quality
    public ArrayList<Integer> buildRandomChordRoot(String quality, int baseNote) {

        ArrayList<Integer> pitchesToPlay = new ArrayList<>(); //contains the pitches to be played

        System.out.println("base note: " + baseNote); //one among 0-11

        int base1 = pitchChart[baseNote][0];
        int base2 = pitchChart[baseNote][1];
        int base3 = pitchChart[baseNote][2]; //here are the base note in three different octaves

        //other pitches in the chord, for example, if a chord is c major triad (c,e,g)
        //then "otherPitches" will contain e and g.
        ArrayList<Integer> otherPitches = getPitches(baseNote, quality); //returns the full chord
        otherPitches.remove(Integer.valueOf(baseNote));//remove the base note

        ArrayList<Integer> random3 = new ArrayList<>();
        random3.addAll(Arrays.asList(0, 1, 2)); //a helper list

        for (int i : otherPitches) {
            Collections.shuffle(random3);
            while (pitchChart[i][random3.get(0)] < base1 || pitchChart[i][random3.get(1)] < base1)
                Collections.shuffle(random3); //this is to make sure that I have 2 available options (not lower than the lowest base note)

            int r1 = random3.get(0);
            int r2 = random3.get(1); //r1, r2 are two random distinct numbers in 0-2
            pitchesToPlay.add(pitchChart[i][r1]); //adds 2 random choices of the other pitches
            pitchesToPlay.add(pitchChart[i][r2]);
        }

        //here i've done selecting all the other notes other than the base note
        //its time to choose our base notes

//        int lowest = Collections.min(pitchesToPlay); //the lowest note (other than base note) selected
//        Collections.shuffle(random3);
//        if (base1 < lowest && lowest < base2) { //this means base1 must be added
//            while (pitchChart[baseNote][random3.get(0)] != base1)
//                Collections.shuffle(random3); //make sure "pitchChart[baseNote][random3.get(0)]" is base1
//        } else if (base2 < lowest && lowest < base3) { //this means one of base1, base2 must be added
//            while (pitchChart[baseNote][random3.get(0)] != base1 && pitchChart[baseNote][random3.get(0)] != base2)
//                Collections.shuffle(random3); //make sure "pitchChart[baseNote][random3.get(0)]" is either base1 or base2
//        }
//        //if else, then adding either base1, base2 or base3 would work, so no need to restrict anything

        pitchesToPlay.add(pitchChart[baseNote][random3.get(0)]);
        pitchesToPlay.add(pitchChart[baseNote][random3.get(1)]);
        pitchesToPlay.add(pitchChart[baseNote][random3.get(2)]);

        return pitchesToPlay;
    }

    //build a random simple chord
    public ArrayList<Integer> buildRandomChordSimple(String quality, int base) {
        ArrayList<Integer> pitches = getPitches2(base, quality); //the "simple" pitches of the chord
        int highest = Collections.max(pitches);
        if (highest > 36) { //if the highest note is out of bound (36)
            int gap = highest - 36;
            base = gen.nextInt(base-gap) + 1; //randomly pick a base note where the highest note would not get out of bound
            pitches = getPitches(base, quality);
        }
        System.out.println("base pitch: " + base); //one among 1-36
        // now the pitches in the chord is guaranteed to be within 1-36
        return pitches;
    }

    //build a random simple chord with specified base noter
    public ArrayList<Integer> buildRandomChordSimple(String quality) {

        Random gen = new Random();
        int base = gen.nextInt(24)+1; //a random note in the first 2 octaves

        ArrayList<Integer> pitches = getPitches2(base, quality); //the "simple" pitches of the chord
        int highest = Collections.max(pitches);
        if (highest > 36) { //if the highest note is out of bound (36)
            int gap = highest - 36;
            base = gen.nextInt(base-gap) + 1; //randomly pick a base note where the highest note would not get out of bound
            pitches = getPitches(base, quality);
        }
        System.out.println("base pitch: " + base); //one among 1-36
        // now the pitches in the chord is guaranteed to be within 1-36
        return pitches;
    }

    //given the chord quality and the basenote,
    //returns all the "playable" inversions, aka highest note no higher than 36
    //baseNote is the basenote of the chord (e.g f_3 of f major),
    // containedNote is the note you want it to contain (e.g. a_3)
    public static ArrayList<ArrayList<Integer>> getInversions(String quality, int baseNote, int containedNote) {

        ArrayList<ArrayList<Integer>> inversions = new ArrayList<>();

        ArrayList<Integer> rootPos = getPitches2(baseNote,quality);
        System.out.println("root: " + rootPos.toString());
        if (Collections.min(rootPos)>=1 && Collections.max(rootPos)<=36){
            inversions.add(rootPos);
            System.out.println("current inversions: " + inversions);
        }

        ArrayList<Integer> upCount = new ArrayList<>(); //helper for counting "up" from root position
        upCount.addAll(rootPos);
        System.out.println("Original upCount: " + upCount.toString());
        while(Collections.min(upCount)!=containedNote && Collections.max(upCount)<=36){
            int minValue = Collections.min(upCount);
            int minIndex = upCount.indexOf(minValue);
            upCount.set(minIndex,minValue+12); //raise the lowest note by an octave (goes to the next inversion)
            System.out.println("upCount: " + upCount.toString());
            if(Collections.max(upCount)<=36 && Collections.min(upCount)>=1) {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.addAll(upCount); //copy the current downCount to a new list "temp"
                inversions.add(temp); //add temp to inversions
                System.out.println("current inversions: " + inversions);
            }
        }

        ArrayList<Integer> downCount = new ArrayList<>(); //helper for counting "down" from root position
        downCount.addAll(rootPos);
        System.out.println("Original downCount: " + downCount.toString());
        while(Collections.max(downCount)!=containedNote && Collections.min(downCount)>=1){
            int maxValue = Collections.max(downCount);
            int maxIndex = downCount.indexOf(maxValue);
            downCount.set(maxIndex,maxValue-12); //drop the highest note by an octave (goes to the next inversion)
            System.out.println("downCount: " + downCount.toString());
            if(Collections.min(downCount)>=1 && Collections.max(downCount)<=36) {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.addAll(downCount); //copy the current downCount to a new list "temp"
                inversions.add(temp); //add temp to inversions
                System.out.println("current inversions: " + inversions);
            }
        }

        System.out.println("final inversions: ");
//        for(ArrayList<Integer> inv : inversions) {
//            System.out.println(inv.toString());
//        }
        System.out.println(inversions.toString());

        return inversions;

    }

    //here we are given a pitch
    //we need to find a random chord that contains that pitch
    //so we need to get 1. what chord it is. 2. what inversion
    public static ArrayList<Integer> buildChordContainingPitch(int pitch){

        //picks out a number of less obscure chords, adds them to "qualitiesToUse"
        String[] temp = {"major triad","minor triad","diminished triad","sus 4 triad","sus 2 triad","major seventh"
                ,"minor seventh","dominant seventh","half diminished seventh","diminished seventh","major ninth",
                "minor ninth","dominant ninth"};
        ArrayList<String> qualitiesToUse = new ArrayList<>();
        qualitiesToUse.addAll(Arrays.asList(temp));

        Collections.shuffle(qualitiesToUse);
        String randomQuality = qualitiesToUse.get(0); //returns a random quality that defines our chord
        System.out.println("chord quality: " + randomQuality);

        int[] pitchesInChord = mapPitches.get(randomQuality); //the pitches that the random quality has ([0,x,y,z..])

        int rand = gen.nextInt(pitchesInChord.length);
        int pitchDegree = pitchesInChord[rand];
        int baseNote = pitch-pitchDegree;
        System.out.println("basenote: " + baseNote);

        // explanation: here we have already a quality, e.g a major triad. Then we ask: which major triad?
        // A major triad contains "one", "three" and "five". And given a pitch (let's say A)
        // that's in a major triad chord, there are only 3 possibilities about the chord :
        // 1. A is the one, 2. A is the three, 3. A is the five i.e
        // 1. chord is A major, 2. chord is F major, 3. chord is D major
        // so we can randomly pick one case among the three and determine a chord (which base note) that contains the pitch
        // next we need to assign which inversion it's in.

        ArrayList<ArrayList<Integer>> inversions = getInversions(randomQuality,baseNote,pitch);
        Collections.shuffle(inversions);
        ArrayList<Integer> theInversion = inversions.get(0);
        System.out.println("resulting chord: "+ theInversion.toString());
        return theInversion;

    }

}
