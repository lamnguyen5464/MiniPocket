package com.example.myapplication.wolit.model;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.myapplication.wolit.R;
import com.kizitonwose.calendarview.ui.ViewContainer;

public class DayViewContainer extends ViewContainer {
    public Button btDayCell;
    public View markEarning, markPaying, selectedBackground;
    public TextView dayOfWeek, labelDate;

    public DayViewContainer(@NonNull View view) {
        super(view);
        markEarning = view.findViewById(R.id.markEarning);
        markPaying = view.findViewById(R.id.markPaying);
        selectedBackground = view.findViewById(R.id.selected);
        dayOfWeek = view.findViewById(R.id.dayOfWeek);
        labelDate = view.findViewById(R.id.labelDate);
        btDayCell = view.findViewById(R.id.btDayCell);
    }
}