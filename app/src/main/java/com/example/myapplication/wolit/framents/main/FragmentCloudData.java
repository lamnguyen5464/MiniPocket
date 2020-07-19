package com.example.myapplication.wolit.framents.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.DayViewContainer;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;


public class FragmentCloudData extends Fragment {
    public static FragmentCloudData instance = null;
    TextView monthLabel;

    public static FragmentCloudData getInstance(){
        if (instance == null){
            instance = new FragmentCloudData();
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
        View view = inflater.inflate(R.layout.fragment_cloud_data, container, false);
        return view;
    }
}