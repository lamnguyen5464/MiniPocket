package com.example.myapplication.wolit.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.renderscript.Element;
import android.util.Log;
import android.util.MonthDisplayHelper;

import com.example.myapplication.wolit.model.tranferdetail.EveryNDayDetail;
import com.example.myapplication.wolit.model.tranferdetail.MonthlyDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;
import com.example.myapplication.wolit.realm.RealmApdapter;

import io.realm.RealmResults;

import static android.content.Context.MODE_PRIVATE;

public class CurrentStatus {
    public double myMoney;
    public String currencyType = "VND";
    private static CurrentStatus sharedValue;
    private CurrentStatus(){}
    public static CurrentStatus getSharedValue(Context context){
        if (sharedValue == null){
            sharedValue = new CurrentStatus();
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
    public static void updateTransaction(DateType fromDate, DateType toDate){
        WeeklyDetail.updateTransactionFromRealm(fromDate, toDate);
        MonthlyDetail.updateTransactionFromRealm(fromDate, toDate);
        EveryNDayDetail.updateTransactionFromRealm(fromDate, toDate);
    }

}
