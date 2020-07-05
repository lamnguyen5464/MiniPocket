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
import com.example.myapplication.wolit.model.tranferdetail.MonthlyDetail;


public class FragmentMonthly extends Fragment implements CompoundButton.OnClickListener, CompoundButton.OnLongClickListener {
    private RelativeLayout background;
    private boolean isEarningMode;
    private Context context;
    private int colorCode;
    public FragmentMonthly(Context context, boolean isEarningMode){
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
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        ((TextView)view.findViewById(R.id.labelDateOfMonth)).setTextColor(colorCode);
        ((TextView)view.findViewById(R.id.labelDateStart)).setTextColor(colorCode);
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
