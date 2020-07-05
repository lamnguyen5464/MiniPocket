package com.example.myapplication.wolit.framents;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.wolit.R;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;


public class FragmentMain extends Fragment {
    public static FragmentMain instance = null;

    public static FragmentMain getInstance(){
        if (instance == null){
            instance = new FragmentMain();
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
        CalendarView calendarView = view.findViewById(R.id.calendarView);


        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                Log.d("@@@", "create()");
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay day) {
                Log.d("@@@", "bind()");
                container.textView.setText(String.valueOf(day.getDate().getDayOfMonth()));
            }
        });

        YearMonth currentMonth = YearMonth.now();
        calendarView.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
        calendarView.scrollToMonth(YearMonth.now());
        return view;
    }
}
class DayViewContainer extends ViewContainer {
    TextView textView;

    DayViewContainer(@NonNull View view) {
        super(view);
        Log.d("@@@", "constructor dayView()");
        textView = view.findViewById(R.id.calendarDayText1);
    }
}