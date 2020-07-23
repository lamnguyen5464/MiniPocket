package com.example.myapplication.wolit.framents.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.model.CurrentStatus;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;


public class FragmentCurrentStatus extends Fragment implements CompoundButton.OnClickListener,CompoundButton.OnLongClickListener {
    public static FragmentCurrentStatus instance = null;
    TextView labelYourMoney;
    DateType startDate = new DateType(), endDate = DateType.getToday();
    LineChart lineChart;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    CheckBox checkBoxFilter;
    RelativeLayout layoutFilter;
    EditText filterNote;
    Button btStartDate;
    Button btEndDate;
    public static FragmentCurrentStatus getInstance(boolean newFrame){
        if (instance == null || newFrame){
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
        checkBoxFilter = view.findViewById(R.id.checkBoxFilter);
        layoutFilter = view.findViewById(R.id.filter);
        filterNote = view.findViewById(R.id.filterNote);


        checkBoxFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    layoutFilter.setVisibility(View.VISIBLE);
                    buttonView.setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE));
                    btEndDate.setText(endDate.getString());
                    btStartDate.setText(startDate.getString());
                }else{
                    layoutFilter.setVisibility(View.GONE);
                    buttonView.setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE_DARK));
                    endDate = DateType.getToday();
                    startDate.reset();
                    filterNote.setText("");
                    refreshDataForChart();
                }
            }
        });

        //filterText


        //chart
        lineChart = view.findViewById(R.id.chart);


        //format x axis
        lineChart.getXAxis().setValueFormatter(new MyAxisFormatter());


        //disable grid
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);

        //set textColor axis
        lineChart.getAxisLeft().setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE));
        lineChart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE));

        //disable description
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        refreshDataForChart();

        //pullRefreshLayout
        final PullRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDataForChart();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //button get Date
        btStartDate = view.findViewById(R.id.startDate);
        btEndDate = view.findViewById(R.id.endDate);
        btEndDate.setOnClickListener(this);
        btEndDate.setOnLongClickListener(this);
        btStartDate.setOnClickListener(this);
        btStartDate.setOnLongClickListener(this);
        btEndDate.setText(endDate.getString());
        btStartDate.setText(startDate.getString());

        view.findViewById(R.id.btRefresh).setOnClickListener(this);

        return view;
    }
    private void refreshDataForChart(){
        lineChart.setData(getLineData());
        lineChart.invalidate();
        lineChart.animateX(1000, Easing.EaseInSine);


    }
    private LineData getLineData(){
        RealmResults<NonRepeatedDetail> nonRepeatedDetails = RealmApdapter.getAllNonRepeatedList();

        List<Entry> points = new ArrayList<>();
        float total = 0;
        DateType preDate = new DateType();
        long dateCode = 0;
        List<NonRepeatedDetail> listFiltered = new ArrayList<>();
        for(NonRepeatedDetail tmp : nonRepeatedDetails){
            //bound
            if (tmp.getDate().getDateCode() > endDate.getDateCode()) break;
            if (!startDate.isEmptyDate() && startDate.getDateCode() > tmp.getDate().getDateCode()) continue;
            String tmpNote = filterNote.getText().toString();
            if (tmpNote.length() > 0 && tmp.getNote().indexOf(tmpNote) == -1) continue;
            total = total + (float)tmp.getValue();
            if (preDate.isDifferent(tmp.getDate())){
                dateCode =  tmp.getDate().getMilisecondCode();
                preDate.set(tmp.getDate());
            }else{
                dateCode = dateCode + 10000000;
            }
            points.add( new Entry(dateCode, total) );
//          add to list
            listFiltered.add(tmp);
        }


        //set label sum
        labelYourMoney.setText(decimalFormat.format(total));
        //
        LineDataSet dataSet = new LineDataSet(points, "Sum");
        dataSet.setLineWidth(5);
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.YELLOW));
//        dataSet.setDrawCircles(false);
        dataSet.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.YELLOW));
        dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.YELLOW));
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.GRAY));

        return new LineData(dataSet);
    }
    private void pickDateFromCalendar(final Button btPicker){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (btPicker.getId() == R.id.startDate) startDate.set(dayOfMonth, month+1, year);
                else endDate.set(dayOfMonth, month+1, year);
                btPicker.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                refreshDataForChart();
                Toast.makeText(getContext(), "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void eraseDate(Button btPicker){
        btPicker.setText("__/__/____");
        if (btPicker.getId() == R.id.startDate) startDate.reset();
        else endDate.reset();
        refreshDataForChart();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                pickDateFromCalendar(((Button)v.findViewById(R.id.startDate)));
                break;
            case R.id.endDate:
                pickDateFromCalendar(((Button)v.findViewById(R.id.endDate)));
                break;
            case R.id.btRefresh:
                refreshDataForChart();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                eraseDate(((Button)v.findViewById(R.id.startDate)));
                return true;
            case R.id.endDate:
                eraseDate(((Button)v.findViewById(R.id.endDate)));
                return true;
        }
        return false;
    }
}
class MyAxisFormatter extends ValueFormatter {

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String holder = dateFormat.format(new Date( (long)value ));
        String dateMonth = "";
        for(int index = 0; index < holder.length(); index++){
            if (holder.charAt(index) == ',') break;
            dateMonth += holder.charAt(index);
        }
//        Log.d("@@@", dateMonth+ " "+holder);
        return dateMonth;
    }
}