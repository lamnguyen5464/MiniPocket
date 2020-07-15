package com.example.myapplication.wolit.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.myapplication.wolit.model.tranferdetail.EveryNDayDetail;
import com.example.myapplication.wolit.model.tranferdetail.MonthlyDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;

import static android.content.Context.MODE_PRIVATE;

public class CurrentStatus {
    private DateType upToDate;
    private double myMoney;
    private String currencyType = "VND";
    private static CurrentStatus sharedValue;
    private Context context;
    private static final String VERS = "1";
    private static final String STORAGE_TAG = "STORAGE_TAG" + VERS;
    private static final String MONEY_TAG = "MONEY_TAG" + VERS;
    private static final String CURRENCY_TAG = "CURRENCY_TAG" + VERS;
    private static final String CURRENT_DATE_TAG = "CURRENT_DATE" + VERS;
    private static final String CURRENT_MONTH_TAG = "CURRENT_MONTH" + VERS;
    private static final String CURRENT_YEAR_TAG = "CURRENT_YEAR" + VERS;

    private CurrentStatus(){}
    public static CurrentStatus initSharedValue(Context context){ //must be called when start app
        if (sharedValue == null){
            sharedValue = new CurrentStatus();
            sharedValue.upToDate = new DateType();

            SharedPreferences sharedPreferences = context.getSharedPreferences(STORAGE_TAG, MODE_PRIVATE);
            sharedValue.myMoney = Double.parseDouble(sharedPreferences.getString(MONEY_TAG, "0"));
            sharedValue.currencyType = sharedPreferences.getString(CURRENCY_TAG,"VND");

            sharedValue.upToDate.setDate(Integer.parseInt(sharedPreferences.getString(CURRENT_DATE_TAG, "0")));
            sharedValue.upToDate.setMonth(Integer.parseInt(sharedPreferences.getString(CURRENT_MONTH_TAG, "0")));
            sharedValue.upToDate.setYear(Integer.parseInt(sharedPreferences.getString(CURRENT_YEAR_TAG, "0")));

            sharedValue.setContext(context);
        }
        return sharedValue;
    }
    public static CurrentStatus getSharedValue(){
        return sharedValue;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void updateMoney(double newMoney, String newCurrencyType){
        this.setMyMoney(newMoney);
        this.setCurrencyType(newCurrencyType);
        SharedPreferences sharedPreferences = context.getSharedPreferences(STORAGE_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MONEY_TAG, Double.toString(this.myMoney));
        editor.putString(CURRENCY_TAG, this.currencyType);
        editor.commit();
    }
    public void updateUpToDate(DateType date){
        this.upToDate.set(date);
        SharedPreferences sharedPreferences = context.getSharedPreferences(STORAGE_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_DATE_TAG, Integer.toString(this.upToDate.getDate()));
        editor.putString(CURRENT_MONTH_TAG, Integer.toString(this.upToDate.getMonth()));
        editor.putString(CURRENT_YEAR_TAG, Integer.toString(this.upToDate.getYear()));
        editor.commit();
    }

    public static void updateTransaction(DateType fromDate, DateType toDate){
        WeeklyDetail.updateTransactionFromRealm(fromDate, toDate);
        MonthlyDetail.updateTransactionFromRealm(fromDate, toDate);
        EveryNDayDetail.updateTransactionFromRealm(fromDate, toDate);
    }
    public static void update(){
        DateType toDay = DateType.getToday();
        updateTransaction(CurrentStatus.getSharedValue().getUpToDate(), toDay);
        toDay.goToTheNextDay();
        CurrentStatus.getSharedValue().updateUpToDate(toDay);
    }
    public DateType getUpToDate() {
        return upToDate;
    }

    public void setUpToDate(DateType upToDate) {
        this.upToDate = upToDate;
    }

    public double getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(double myMoney) {
        this.myMoney = myMoney;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
