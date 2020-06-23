package com.example.myapplication.wolit.model;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class MyMoney {
    public double myMoney;
    public String currencyType = "VND";
    private static MyMoney sharedValue;
    private MyMoney(){}
    public static MyMoney getSharedValue(Context context){
        if (sharedValue == null){
            sharedValue = new MyMoney();
            SharedPreferences sharedPreferences = context.getSharedPreferences("STORAGE_TAG",MODE_PRIVATE);
            sharedValue.myMoney = Double.parseDouble(sharedPreferences.getString("MONEY_TAG", "0"));
            sharedValue.currencyType = sharedPreferences.getString("CURRENCY_TAG","VND");
        }
        return sharedValue;
    }
    public boolean updateMoney(double newMoney, String newCurrencyType, Context context){      //must init before calling this func -> sharedValue != null
        if (sharedValue == null){
            return false;
        }
        sharedValue.myMoney = newMoney;
        sharedValue.currencyType = newCurrencyType;
        SharedPreferences sharedPreferences = context.getSharedPreferences("STORAGE_TAG", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MONEY_TAG", Double.toString(sharedValue.myMoney));
        editor.putString("CURRENCY_TAG", sharedValue.currencyType);
        editor.commit();
        return true;
    }
}
