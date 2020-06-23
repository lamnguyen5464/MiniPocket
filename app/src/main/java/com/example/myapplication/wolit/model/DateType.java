package com.example.myapplication.wolit.model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.wolit.R;

import java.util.Calendar;

public class DateType {
    private int date;
    private int month;
    private int year;
    private static DateType recentDate = null;
    public static DateType getRecentDate(){
        if (recentDate == null){
            recentDate = new DateType();
        }
        return recentDate;
    }
    public DateType(int date, int month, int year){
        this.date = date;
        this.month = month;
        this.year = year;
    }
    public DateType(){
        reset();
    }
    public void reset(){
        this.date = 0;
        this.month = 0;
        this.year = 0;
    }
    public void set(int date, int month, int year){
        this.date = date;
        this.month = month;
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public boolean isEmptyDate(){
        return (this.date == 0);
    }

    public String getString(){
        if (isEmptyDate()) return "__/__/____";
        return this.date + "/" + this.month + "/" +this.year;
    }
}
