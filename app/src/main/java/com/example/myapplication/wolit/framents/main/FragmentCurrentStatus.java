package com.example.myapplication.wolit.framents.main;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.model.CurrentStatus;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;


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

        //chart
        final LineChart lineChart = view.findViewById(R.id.chart);
        RealmResults<NonRepeatedDetail> nonRepeatedDetails = RealmApdapter.getAllNonRepeatedList();
        List<Entry> points = new ArrayList<>();

        float total = 0;
        DateType preDate = new DateType();
        long dateCode = 0;
        for(NonRepeatedDetail tmp : nonRepeatedDetails){
            total = total + (float)tmp.getValue();
            if (preDate.isDifferent(tmp.getDate())){
                dateCode =  tmp.getDate().getMilisecondCode();
                preDate.set(tmp.getDate());
            }else{
                dateCode = dateCode + 10000000;
            }
            points.add( new Entry(dateCode, total) );

            DateFormat dateFormat = DateFormat.getDateTimeInstance();
//            Log.d("@@@", tmp.getDate().getString()+ " - "+ preDate.getString()+" - " + (long)dateCode + " _ "+ " - " + dateFormat.format(new Date(tmp.getDate().getMilisecondCode())) +  " " +tmp.getValue());
        }

        LineDataSet dataSet = new LineDataSet(points, "Sum");
        dataSet.setLineWidth(5);
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.GREEN));
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(getResources().getColor(R.color.GRAY));

        LineData lineData = new LineData(dataSet);
        //add data
        lineChart.setData(lineData);

        //format x axis
        lineChart.getXAxis().setValueFormatter(new MyAxisFormatter());

        //animate
        lineChart.animateX(1000);

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

        lineChart.invalidate();

        //pullRefreshLayout
        final PullRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lineChart.animateX(1000);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
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