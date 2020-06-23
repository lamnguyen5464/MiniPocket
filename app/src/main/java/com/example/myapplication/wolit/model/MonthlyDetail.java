package com.example.myapplication.wolit.model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.wolit.R;

import java.util.Calendar;

public class MonthlyDetail {
    public DateType dateOfMonth, startDate, endDate;
    private static MonthlyDetail recentWeeklyDetail = null;
    public MonthlyDetail(){
        dateOfMonth = new DateType();
        startDate = new DateType();
        endDate = new DateType();
    }
    public static MonthlyDetail getRecentDetail(){
        if (recentWeeklyDetail == null){
            recentWeeklyDetail = new MonthlyDetail();
        }
        return recentWeeklyDetail;
    }
    public void reset(){
        dateOfMonth.reset();
        startDate.reset();
        endDate.reset();
    }
    public boolean isNotEnoughInfo(){
        if (dateOfMonth.isEmptyDate()) return true;
        if (startDate.isEmptyDate()) return true;
        return false;
    }
    public void pickDateFromCalendar(final Context context, final Button btPicker){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                switch (btPicker.getId()){
                    case R.id.dateOfMonth:
                        dateOfMonth.set(dayOfMonth, month+1, year);
                        break;
                    case R.id.startDate:
                        startDate.set(dayOfMonth, month+1, year);
                        break;
                    case R.id.endDate:
                        endDate.set(dayOfMonth, month+1, year);
                        break;
                }
                btPicker.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                Toast.makeText(context, "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void eraseDate(Button bt){
        bt.setText("__/__/____");
        switch (bt.getId()){
            case R.id.dateOfMonth:
                dateOfMonth.reset();
                break;
            case R.id.startDate:
                startDate.reset();
                break;
            case R.id.endDate:
                endDate.reset();
                break;
        }
    }
}
