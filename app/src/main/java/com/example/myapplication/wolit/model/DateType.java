package com.example.myapplication.wolit.model;

import android.util.Log;
import android.util.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;

public class DateType extends RealmObject {
    private int date;
    private int month;
    private int year;
    private int dateCode;
    private static DateType recentDate = null;
    public static DateType getRecentDate(){
        if (recentDate == null){
            recentDate = getToday();
        }
        return recentDate;
    }
    public static DateType getToday(){
//        return new DateType(9, 8, 2020);
        Calendar calendar = Calendar.getInstance();
        return new DateType(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
    }
    public DateType(int date, int month, int year){
        this.date = date;
        this.month = month;
        this.year = year;
        this.dateCode = getDateCode();
    }
    public DateType(DateType desDate){
        this.date = desDate.getDate();
        this.month = desDate.getMonth();
        this.year = desDate.getYear();
        this.dateCode = getDateCode();
    }
    public DateType(){
        reset();
    }
    public void reset(){
        this.date = 0;
        this.month = 0;
        this.year = 0;
        this.dateCode = getDateCode();
    }
    public void set(int date, int month, int year){
        this.date = date;
        this.month = month;
        this.year = year;
        this.dateCode = getDateCode();
    }
    public void set(DateType date){
        this.date = date.getDate();
        this.month = date.getMonth();
        this.year = date.getYear();
        this.dateCode = getDateCode();
    }
    public boolean isDifferent(DateType date){
        return (this.date != date.date || this.month != date.month || this.year != date.year);
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
    public int getDateCode(){
        if (isEmptyDate()) return 99999999;
        return (((this.year*100) + this.month)*100 + this.date);
    }

    public void setDateCode(int dateCode) {
        this.dateCode = dateCode;
    }
    public int getDayOfWeek(){             //0->Sun
        int deltaMonth[] = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        year -= (month < 3) ? 1 : 0;
        return (year + year/4 - year/100 + year/400 + deltaMonth[month-1] + date) % 7;
    }
    public int getDaysOfMonth(){
        int days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (this.isLeapYear()) days[1] = 29;
        return days[month-1];
    }
    public boolean isLeapYear(){
        return  ((this.year%4==0 && this.year%100!=0) || this.year%400==0);
    }
    public void goToTheNextMonth(){
        this.month++;
        if (this.month > 12){
            this.month = 1;
            this.year++;
        }
        this.dateCode = getDateCode();
    }
    public void goToTheNextDay(){
        this.date++;
        if (this.date > getDaysOfMonth()){
            this.date = 1;
            goToTheNextMonth();
        }
        this.dateCode = getDateCode();
    }
    public int countDeltaDays(DateType toDate){
        SimpleDateFormat dft = new SimpleDateFormat("dd MM yyyy");
        try {
            Date date1 = dft.parse(this.date+" "+this.month+" "+this.year);
            Date date2 = dft.parse(toDate.date+" "+toDate.month+" "+this.year);
            return (int) TimeUnit.DAYS.convert((date2.getTime() - date1.getTime()), TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public long getMilisecondCode(){
        SimpleDateFormat dft = new SimpleDateFormat("dd MM yyyy");
        try {
            Date date1 = dft.parse(this.date+" "+this.month+" "+this.year);
            return TimeUnit.MILLISECONDS.convert(date1.getTime(), TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String numToStringDay(int num){
        switch (num){
            case 1: return "Mon";
            case 2: return "Tue";
            case 3: return "Wed";
            case 4: return "Thu";
            case 5: return "Fri";
            case 6: return "Sat";
        }
        return "Sun";
    }
    public void setDate(int date) {
        this.date = date;
        this.dateCode = getDateCode();
    }

    public void setMonth(int month) {
        this.month = month;
        this.dateCode = getDateCode();
    }

    public void setYear(int year) {
        this.year = year;
        this.dateCode = getDateCode();
    }

}
