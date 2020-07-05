package com.example.myapplication.wolit.framents.newtransfer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.tranferdetail.EveryNDayDetail;


public class FragmentEveryNDay extends Fragment implements  CompoundButton.OnClickListener, CompoundButton.OnLongClickListener {
    RelativeLayout background;
    boolean isEarningMode;
    Context context;
    TextView pickerNumOfDays;
    private int colorCode;
    public FragmentEveryNDay(Context context, boolean isEarningMode){
        this.isEarningMode = isEarningMode;
        this.context = context;
        this.colorCode = (this.isEarningMode) ? ContextCompat.getColor(context, R.color.GREEN) : ContextCompat.getColor(context, R.color.RED);

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
        //set ui
        ((TextView)view.findViewById(R.id.labelNumOfDays)).setTextColor(colorCode);
        ((TextView)view.findViewById(R.id.labelDateStart)).setTextColor(colorCode);
        //reset
        EveryNDayDetail.getRecentDetail().reset();

        //set event
        (view.findViewById(R.id.startDate)).setOnClickListener(this);
        (view.findViewById(R.id.endDate)).setOnClickListener(this);
        (view.findViewById(R.id.incBt)).setOnClickListener(this);
        (view.findViewById(R.id.decBt)).setOnClickListener(this);

        (view.findViewById(R.id.startDate)).setOnLongClickListener(this);
        (view.findViewById(R.id.endDate)).setOnLongClickListener(this);

        pickerNumOfDays = view.findViewById(R.id.pickerNumOfDays);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                EveryNDayDetail.getRecentDetail().pickDateFromCalendar(context, (Button)v.findViewById(R.id.startDate));
                break;
            case R.id.endDate:
                EveryNDayDetail.getRecentDetail().pickDateFromCalendar(context, (Button)v.findViewById(R.id.endDate));
                break;
            case R.id.incBt:
                EveryNDayDetail.getRecentDetail().onClickPickDays(pickerNumOfDays, 1);
                break;
            case R.id.decBt:
                EveryNDayDetail.getRecentDetail().onClickPickDays(pickerNumOfDays, -1);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                EveryNDayDetail.getRecentDetail().eraseDate((Button)v.findViewById(R.id.startDate));
                return true;
            case R.id.endDate:
                EveryNDayDetail.getRecentDetail().eraseDate((Button)v.findViewById(R.id.endDate));
                return true;
        }
        return false;
    }

}
