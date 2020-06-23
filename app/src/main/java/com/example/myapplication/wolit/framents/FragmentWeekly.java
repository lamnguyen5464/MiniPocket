package com.example.myapplication.wolit.framents;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.WeeklyDetail;


public class FragmentWeekly extends Fragment implements CompoundButton.OnCheckedChangeListener, CompoundButton.OnClickListener, CompoundButton.OnLongClickListener{
    RelativeLayout background;
    boolean isEarningMode;
    Context context;
    public FragmentWeekly(Context context, boolean isEarningMode){
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
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);
        background = view.findViewById(R.id.background);
        if (isEarningMode){
            background.setBackgroundResource(R.drawable.custom_input_green);
        }else{
            background.setBackgroundResource(R.drawable.custom_input_red);
        }
        //set Event switch toggle button
        ((ToggleButton)view.findViewById(R.id.tgBt1)).setOnCheckedChangeListener(this);
        ((ToggleButton)view.findViewById(R.id.tgBt2)).setOnCheckedChangeListener(this);
        ((ToggleButton)view.findViewById(R.id.tgBt3)).setOnCheckedChangeListener(this);
        ((ToggleButton)view.findViewById(R.id.tgBt4)).setOnCheckedChangeListener(this);
        ((ToggleButton)view.findViewById(R.id.tgBt4)).setOnCheckedChangeListener(this);
        ((ToggleButton)view.findViewById(R.id.tgBt6)).setOnCheckedChangeListener(this);
        ((ToggleButton)view.findViewById(R.id.tgBt7)).setOnCheckedChangeListener(this);

        //reset getRecent
        WeeklyDetail.getRecentDetail().reset();

        //set event select date
        (view.findViewById(R.id.startDate)).setOnClickListener(this);
        (view.findViewById(R.id.endDate)).setOnClickListener(this);

        //set event clear date

        (view.findViewById(R.id.startDate)).setOnLongClickListener(this);
        (view.findViewById(R.id.endDate)).setOnLongClickListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tgBt1:            //Monday
                WeeklyDetail.getRecentDetail().switchDayOfWeek(1);
                break;
            case R.id.tgBt2:
                WeeklyDetail.getRecentDetail().switchDayOfWeek(2);
                break;
            case R.id.tgBt3:
                WeeklyDetail.getRecentDetail().switchDayOfWeek(3);
                break;
            case R.id.tgBt4:
                WeeklyDetail.getRecentDetail().switchDayOfWeek(4);
                break;
            case R.id.tgBt5:
                WeeklyDetail.getRecentDetail().switchDayOfWeek(5);
                break;
            case R.id.tgBt6:
                WeeklyDetail.getRecentDetail().switchDayOfWeek(6);
                break;
            case R.id.tgBt7:
                WeeklyDetail.getRecentDetail().switchDayOfWeek(0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                WeeklyDetail.getRecentDetail().pickDateFromCalendar(context, ((Button)v.findViewById(R.id.startDate)));
                break;
            case R.id.endDate:
                WeeklyDetail.getRecentDetail().pickDateFromCalendar(context, ((Button)v.findViewById(R.id.endDate)));
                break;
        }
    }
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                WeeklyDetail.getRecentDetail().eraseDate(((Button)v.findViewById(R.id.startDate)));
                return true;
            case R.id.endDate:
                WeeklyDetail.getRecentDetail().eraseDate(((Button)v.findViewById(R.id.endDate)));
                return true;
        }
        return false;
    }
}
