package com.example.app3;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by dengyitong on 6/27/17.
 */

public class MySpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] objects;

    public MySpinnerAdapter(@NonNull Context context, @NonNull String[] objects) {
        super(context, R.layout.my_spinner_layout,objects);
        this.context = context;
        this.objects = objects;
        this.setDropDownViewResource(R.layout.my_spinner_layout);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View v = super.getView(position, convertView, parent);
        View v = getCustomView(position, convertView, parent);

        if (position == getCount()) { //this case only occurs in the initial state
            ((TextView)v.findViewById(R.id.textViewSpinner)).setText(getItem(getCount()));//display the last item in the String[]

            //((TextView)v.findViewById(R.id.textViewSpinner)).setHint(getItem(getCount())); //"Hint to be displayed"
        }

        return v;
    }

    @Override
    public int getCount() {
        return super.getCount()-1; // you dont display last item. It is used as hint.
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.my_spinner_layout, parent, false);
        TextView textViewSpinner = view.findViewById(R.id.textViewSpinner);
        textViewSpinner.setText(objects[position]);
        return view;
    }


}
