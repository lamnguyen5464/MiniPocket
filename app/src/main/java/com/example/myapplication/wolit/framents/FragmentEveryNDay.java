package com.example.myapplication.wolit.framents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.myapplication.wolit.R;


public class FragmentEveryNDay extends Fragment implements CompoundButton.OnCheckedChangeListener, CompoundButton.OnClickListener, CompoundButton.OnLongClickListener {
    RelativeLayout background;
    boolean isEarningMode;
    Context context;
    public FragmentEveryNDay(Context context, boolean isEarningMode){
        this.isEarningMode = isEarningMode;
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_every_n_day, container, false);
        background = view.findViewById(R.id.background);
        if (isEarningMode){
            background.setBackgroundResource(R.drawable.custom_input_green);
        }else{
            background.setBackgroundResource(R.drawable.custom_input_red);
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
