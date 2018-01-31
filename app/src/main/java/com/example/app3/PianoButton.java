package com.example.app3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.HashMap;

/**
 * Created by dengyitong on 7/28/17.
 */

public class PianoButton extends android.support.v7.widget.AppCompatImageButton implements View.OnClickListener{

    private SoundPool sp;
    private HashMap<Integer, Integer> pitchId;
    private boolean isSelected; //marks whether the pianobutton is selected

    //constructor
    public PianoButton(Context context) {
        super(context);
        isSelected=false;
        setOnClickListener(this);

        sp = MainActivity.msp.getSoundPool();
        pitchId = MainActivity.msp.getPitchId();

    }

    //constructor
    public PianoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        isSelected=false;
        setOnClickListener(this);

        sp = MainActivity.msp.getSoundPool();
        pitchId = MainActivity.msp.getPitchId();
    }

    //constructor
    public PianoButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isSelected=false;
        setOnClickListener(this);

        sp = MainActivity.msp.getSoundPool();
        pitchId = MainActivity.msp.getPitchId();
    }

    //plays the note that the piano button is associated with
    public void playNote() {

        int id = getId(); //the id to indicate which piano button

        switch (id) {
            case R.id.button_c3:
                sp.play(pitchId.get(7),1,1,0,0,1); //plays the note c3
                break;
            case R.id.button_c_3:
                sp.play(pitchId.get(8),1,1,0,0,1);
                break;
            case R.id.button_d3:
                sp.play(pitchId.get(9),1,1,0,0,1);
                break;
            case R.id.button_d_3:
                sp.play(pitchId.get(10),1,1,0,0,1);
                break;
            case R.id.button_e3:
                sp.play(pitchId.get(11),1,1,0,0,1);
                break;
            case R.id.button_f3:
                sp.play(pitchId.get(12),1,1,0,0,1);
                break;
            case R.id.button_f_3:
                sp.play(pitchId.get(13),1,1,0,0,1);
                break;
            case R.id.button_g3:
                sp.play(pitchId.get(14),1,1,0,0,1);
                break;
            case R.id.button_g_3:
                sp.play(pitchId.get(15),1,1,0,0,1);
                break;
            case R.id.button_a3:
                sp.play(pitchId.get(16),1,1,0,0,1);
                break;
            case R.id.button_a_3:
                sp.play(pitchId.get(17),1,1,0,0,1);
                break;
            case R.id.button_b3:
                sp.play(pitchId.get(18),1,1,0,0,1);
                break;
            case R.id.button_c4:
                sp.play(pitchId.get(19),1,1,0,0,1);
                break;
            case R.id.button_c_4:
                sp.play(pitchId.get(20),1,1,0,0,1);
                break;
            case R.id.button_d4:
                sp.play(pitchId.get(21),1,1,0,0,1);
                break;
            case R.id.button_d_4:
                sp.play(pitchId.get(22),1,1,0,0,1);
                break;
            case R.id.button_e4:
                sp.play(pitchId.get(23),1,1,0,0,1);
                break;
            case R.id.button_f4:
                sp.play(pitchId.get(24),1,1,0,0,1);
                break;
            case R.id.button_f_4:
                sp.play(pitchId.get(25),1,1,0,0,1);
                break;
            case R.id.button_g4:
                sp.play(pitchId.get(26),1,1,0,0,1);
                break;
            case R.id.button_g_4:
                sp.play(pitchId.get(27),1,1,0,0,1);
                break;
            case R.id.button_a4:
                sp.play(pitchId.get(28),1,1,0,0,1);
                break;
            case R.id.button_a_4:
                sp.play(pitchId.get(29),1,1,0,0,1);
                break;
            case R.id.button_b4:
                sp.play(pitchId.get(30),1,1,0,0,1);
                break;
        }
    }

    //returns the "index" of a piano button. for example, button_c3 is indexed to 0; button_c_3 is 1 and so on.
    public int getIndex(){

        int index;

        switch (this.getId()) {
            case R.id.button_c3:
                index = 0;
                break;
            case R.id.button_c_3:
                index = 1;
                break;
            case R.id.button_d3:
                index = 2;
                break;
            case R.id.button_d_3:
                index = 3;
                break;
            case R.id.button_e3:
                index = 4;
                break;
            case R.id.button_f3:
                index = 5;
                break;
            case R.id.button_f_3:
                index = 6;
                break;
            case R.id.button_g3:
                index = 7;
                break;
            case R.id.button_g_3:
                index = 8;
                break;
            case R.id.button_a3:
                index = 9;
                break;
            case R.id.button_a_3:
                index = 10;
                break;
            case R.id.button_b3:
                index = 11;
                break;
            case R.id.button_c4:
                index = 12;
                break;
            case R.id.button_c_4:
                index = 13;
                break;
            case R.id.button_d4:
                index = 14;
                break;
            case R.id.button_d_4:
                index = 15;
                break;
            case R.id.button_e4:
                index = 16;
                break;
            case R.id.button_f4:
                index = 17;
                break;
            case R.id.button_f_4:
                index = 18;
                break;
            case R.id.button_g4:
                index = 19;
                break;
            case R.id.button_g_4:
                index = 20;
                break;
            case R.id.button_a4:
                index = 21;
                break;
            case R.id.button_a_4:
                index = 22;
                break;
            case R.id.button_b4:
                index = 23;
                break;
            default:
                index = 0;
        }

        return index;
    }


    //what happens when a pianobutton is clicked
    @Override
    public void onClick(View view) {

        //inverse the "selected" pointer
        isSelected = !isSelected;

        if (isSelected) { //if after the click, the button is selected
            setActivated(true); //tells the drawable selector to display the "down" image
            playNote(); //plays the note
        }
        else //if after the click, the button is unselected
            setActivated(false); //tells the drawable selector to display the "up" image
                                //not play the note
    }

    //getter for isSelected
    public boolean isSelected() {
        return isSelected;
    }

    //setter for isSelected
    public void setSelected(boolean boo) {
        isSelected = boo;
    }




}
