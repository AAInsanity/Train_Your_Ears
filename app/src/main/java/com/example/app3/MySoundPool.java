package com.example.app3;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.HashMap;

/**
 * Created by dengyitong on 6/24/17.
 */

public class MySoundPool {

    private SoundPool sp;
    private HashMap<Integer, Integer> pitchId;

    public MySoundPool() {
        //create the soundpool
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder().setMaxStreams(10).build();
        }
        else {
            sp = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
        }

        pitchId = new HashMap<Integer, Integer>();

        loadSoundPool();
    }

    public void loadSoundPool() {

        //load the pitches into the soundpool
        pitchId.put(1,sp.load(MyApplication.getAppContext(), R.raw.f_2,1));
        pitchId.put(2,sp.load(MyApplication.getAppContext(), R.raw.g2,1));
        pitchId.put(3,sp.load(MyApplication.getAppContext(), R.raw.g_2,1));
        pitchId.put(4,sp.load(MyApplication.getAppContext(), R.raw.a2,1));
        pitchId.put(5,sp.load(MyApplication.getAppContext(), R.raw.a_2,1));
        pitchId.put(6,sp.load(MyApplication.getAppContext(), R.raw.b2,1));
        pitchId.put(7,sp.load(MyApplication.getAppContext(), R.raw.c3,1));
        pitchId.put(8,sp.load(MyApplication.getAppContext(), R.raw.c_3,1));
        pitchId.put(9,sp.load(MyApplication.getAppContext(), R.raw.d3,1));
        pitchId.put(10,sp.load(MyApplication.getAppContext(), R.raw.d_3,1));
        pitchId.put(11,sp.load(MyApplication.getAppContext(), R.raw.e3,1));
        pitchId.put(12,sp.load(MyApplication.getAppContext(), R.raw.f3,1));
        pitchId.put(13,sp.load(MyApplication.getAppContext(), R.raw.f_3,1));
        pitchId.put(14,sp.load(MyApplication.getAppContext(), R.raw.g3,1));
        pitchId.put(15,sp.load(MyApplication.getAppContext(), R.raw.g_3,1));
        pitchId.put(16,sp.load(MyApplication.getAppContext(), R.raw.a3,1));
        pitchId.put(17,sp.load(MyApplication.getAppContext(), R.raw.a_3,1));
        pitchId.put(18,sp.load(MyApplication.getAppContext(), R.raw.b3,1));
        pitchId.put(19,sp.load(MyApplication.getAppContext(), R.raw.c4,1));
        pitchId.put(20,sp.load(MyApplication.getAppContext(), R.raw.c_4,1));
        pitchId.put(21,sp.load(MyApplication.getAppContext(), R.raw.d4,1));
        pitchId.put(22,sp.load(MyApplication.getAppContext(), R.raw.d_4,1));
        pitchId.put(23,sp.load(MyApplication.getAppContext(), R.raw.e4,1));
        pitchId.put(24,sp.load(MyApplication.getAppContext(), R.raw.f4,1));
        pitchId.put(25,sp.load(MyApplication.getAppContext(), R.raw.f_4,1));
        pitchId.put(26,sp.load(MyApplication.getAppContext(), R.raw.g4,1));
        pitchId.put(27,sp.load(MyApplication.getAppContext(), R.raw.g_4,1));
        pitchId.put(28,sp.load(MyApplication.getAppContext(), R.raw.a4,1));
        pitchId.put(29,sp.load(MyApplication.getAppContext(), R.raw.a_4,1));
        pitchId.put(30,sp.load(MyApplication.getAppContext(), R.raw.b4,1));
        pitchId.put(31,sp.load(MyApplication.getAppContext(), R.raw.c5,1));
        pitchId.put(32,sp.load(MyApplication.getAppContext(), R.raw.c_5,1));
        pitchId.put(33,sp.load(MyApplication.getAppContext(), R.raw.d5,1));
        pitchId.put(34,sp.load(MyApplication.getAppContext(), R.raw.d_5,1));
        pitchId.put(35,sp.load(MyApplication.getAppContext(), R.raw.e5,1));
        pitchId.put(36,sp.load(MyApplication.getAppContext(), R.raw.f5,1));
    }


//    public static String getPitchName(int i) {
//        String[] pitches = {"c3","c#3","d3","d#3","e3","f3","f#3","g3","g#3","a3","a#3",
//                "b3","c4","c#4","d4","d#4","e4","f4","f#4","g4","g#4","a4","a#4","b4"};
//        String str = pitches[i-1];
//        return str;
//    }

    public HashMap<Integer, Integer> getPitchId() {
        return pitchId;
    }

    public SoundPool getSoundPool() {
        return sp;
    }


}
