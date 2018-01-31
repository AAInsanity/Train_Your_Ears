package com.example.app3;

/**
 * Created by dengyitong on 2018/1/28.
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

//recycler adapter for the default progression repository
public class DefaultProgressionAdapter extends RecyclerView.Adapter<DefaultProgressionAdapter.MyViewHolder> {
    private List<String> allProgressions; //a list of all default progression names
    private List<String> selectedProgressions;
    //should be stored in memory when done
    private Context mContext;
    private LayoutInflater inflater;
    private int style;

    public DefaultProgressionAdapter(Context context, List<String> all, List<String> selected, int s){
        this.mContext=context;
        this.allProgressions=all;
        this.selectedProgressions=selected;
        inflater=LayoutInflater.from(mContext);
        style = s;
    }

    //returns the number of progressions in allProgressions
    @Override
    public int getItemCount() {

        return allProgressions.size();
    }

    //Sets what to do with the views in view holder
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Boolean isSelected = selectedProgressions.contains(allProgressions.get(position));
        holder.mCheckBox.setText(allProgressions.get(position));
        //make the checkbox's checked status the same as the progression that it represents
        holder.mCheckBox.setChecked(isSelected);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && !selectedProgressions.contains(allProgressions.get(holder.getAdapterPosition())))
                    selectedProgressions.add(allProgressions.get(holder.getAdapterPosition()));
                else
                    selectedProgressions.remove(allProgressions.get(holder.getAdapterPosition()));
            }
        });
    }

    //return a MyViewHolder with the view item_checkbox
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (style == 1) view = inflater.inflate(R.layout.item_checkbox, parent, false);
        else view = inflater.inflate(R.layout.item_checkbox2, parent, false);
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
