package com.example.myapplication.wolit.framents.main;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.CurrentStatus;
import com.example.myapplication.wolit.model.DayViewContainer;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.WeekFields;

import java.text.DecimalFormat;
import java.util.Locale;


public class FragmentCurrentStatus extends Fragment {
    public static FragmentCurrentStatus instance = null;
    TextView labelYourMoney;

    public static FragmentCurrentStatus getInstance(){
        if (instance == null){
            instance = new FragmentCurrentStatus();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_status, container, false);

        labelYourMoney = view.findViewById(R.id.yourMoney);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        labelYourMoney.setText(decimalFormat.format(CurrentStatus.getSharedValue().getMyMoney()));


        return view;
    }
}