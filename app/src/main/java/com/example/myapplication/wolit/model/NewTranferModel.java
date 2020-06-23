package com.example.myapplication.wolit.model;

import com.example.myapplication.wolit.R;

public class NewTranferModel {
    public double value;
    public String note;
    public boolean isRepeated;
    public boolean isEarning;
    public boolean isWeekly;
    public boolean isMonthly;
    public boolean isEveryNDays;
    public String nonRepeatedDate;
    public NewTranferModel(){
        this.isRepeated = false;
        this.isEarning = true;
        this.isWeekly = false;
        this.isMonthly = false;
        this.isEveryNDays = false;
        this.nonRepeatedDate = "__/__/____";
    }

}
