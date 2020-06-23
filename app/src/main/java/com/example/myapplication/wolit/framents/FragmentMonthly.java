package com.example.myapplication.wolit.framents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.MonthlyDetail;


public class FragmentMonthly extends Fragment implements CompoundButton.OnClickListener, CompoundButton.OnLongClickListener {
    private RelativeLayout background;
    private boolean isEarningMode;
    private Context context;
    public FragmentMonthly(Context context, boolean isEarningMode){
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
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);
        background = view.findViewById(R.id.background);
        if (isEarningMode){
            background.setBackgroundResource(R.drawable.custom_input_green);
        }else{
            background.setBackgroundResource(R.drawable.custom_input_red);
        }
        //reset detail
        MonthlyDetail.getRecentDetail().reset();

        //set event
        (view.findViewById(R.id.dateOfMonth)).setOnClickListener(this);
        (view.findViewById(R.id.startDate)).setOnClickListener(this);
        (view.findViewById(R.id.endDate)).setOnClickListener(this);

        (view.findViewById(R.id.dateOfMonth)).setOnLongClickListener(this);
        (view.findViewById(R.id.startDate)).setOnLongClickListener(this);
        (view.findViewById(R.id.endDate)).setOnLongClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dateOfMonth:
                MonthlyDetail.getRecentDetail().pickDateFromCalendar(context, (Button)v.findViewById(R.id.dateOfMonth));
                break;
            case R.id.startDate:
                MonthlyDetail.getRecentDetail().pickDateFromCalendar(context, (Button)v.findViewById(R.id.startDate));
                break;
            case R.id.endDate:
                MonthlyDetail.getRecentDetail().pickDateFromCalendar(context, (Button)v.findViewById(R.id.endDate));
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.dateOfMonth:
                MonthlyDetail.getRecentDetail().eraseDate((Button)v.findViewById(R.id.dateOfMonth));
                return true;
            case R.id.startDate:
                MonthlyDetail.getRecentDetail().eraseDate((Button)v.findViewById(R.id.startDate));
                return true;
            case R.id.endDate:
                MonthlyDetail.getRecentDetail().eraseDate((Button)v.findViewById(R.id.endDate));
                return true;
        }
        return false;
    }
}
