package com.example.app3;

/**
 * Created by dengyitong on 7/25/17.
 */

import java.util.List;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

//recycler adapter for the custom chords repository
public class CustomChordAdapter extends RecyclerView.Adapter<CustomChordAdapter.MyViewHolder> {
    private List<CustomChord> mData; //a list of CustomChords
                                    //should be stored in memory when done
    private Context mContext;
    private LayoutInflater inflater;

    public CustomChordAdapter(Context context, List<CustomChord> data){
        this.mContext=context;
        this.mData=data;
        inflater=LayoutInflater.from(mContext);
    }

    //returns the number of chords in mData
    @Override
    public int getItemCount() {

        return mData.size();
    }

    //Sets what to do with the views in view holder
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.mCheckBox.setText(mData.get(position).getChordName());
        //make the checkbox's checked status the same as the customchord that it represents
        holder.mCheckBox.setChecked(mData.get(position).isSelected());
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mData.get(holder.getAdapterPosition()).setSelected(b); //sets the corresponding CustomChord in mData to be selected or not selected
            }
        });
    }

    //return a MyViewHolder with the view item_checkbox
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_checkbox, parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    //view holder
    public static class MyViewHolder extends ViewHolder{

        private CheckBox mCheckBox; //the checkbox widget in the layout view

        public MyViewHolder(View view) {
            super(view);
            mCheckBox=(CheckBox)view.findViewById(R.id.cb1);
        }

    }
}
