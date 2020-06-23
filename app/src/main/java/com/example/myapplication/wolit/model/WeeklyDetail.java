package com.example.myapplication.wolit.model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.wolit.R;

import java.util.Calendar;

public class WeeklyDetail {
    private final int NUM_OF_DAYS = 7;
    private boolean dayOfWeek[];
    public DateType startDate, endDate;
    private static WeeklyDetail recentWeeklyDetail = null;
    public WeeklyDetail(){      //0 ->Sun
        this.dayOfWeek = new boolean[NUM_OF_DAYS];
        for(int i = 0; i < NUM_OF_DAYS; i++) this.dayOfWeek[i] = false;
        startDate = new DateType();
        endDate = new DateType();
    }
    public static WeeklyDetail getRecentDetail(){
        if (recentWeeklyDetail == null){
            recentWeeklyDetail = new WeeklyDetail();
        }
        return recentWeeklyDetail;
    }
    public void switchDayOfWeek(int day){
        this.dayOfWeek[day] = !this.dayOfWeek[day];
    }
    public void reset(){
        for(int i = 0; i < NUM_OF_DAYS; i++) this.dayOfWeek[i] = false;
        startDate.reset();
        endDate.reset();
    }
    public boolean isNotEnoughInfo(){
        if (startDate.isEmptyDate()) return true;
        int countDays = 0;
        for(int i = 0; i < NUM_OF_DAYS; i++) if (dayOfWeek[i]) countDays++;
        if (countDays == 0) return true;
        return false;
    }
    public void pickDateFromCalendar(final Context context, final Button btPicker){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (btPicker.getId() == R.id.startDate) startDate.set(dayOfMonth, month+1, year);
                else endDate.set(dayOfMonth, month+1, year);
                btPicker.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                Toast.makeText(context, "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void eraseDate(Button btPicker){
        btPicker.setText("__/__/____");
        if (btPicker.getId() == R.id.startDate) startDate.reset();
        else endDate.reset();
    }
}
