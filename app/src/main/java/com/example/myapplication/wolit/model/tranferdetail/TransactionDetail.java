package com.example.myapplication.wolit.model.tranferdetail;

import com.example.myapplication.wolit.model.DateType;

public interface TransactionDetail {
    void reset();
    boolean isNotValid();
    void saveToDatabase();
    void removeFromDatabase();
    void setBasicInfo(double value, String note);
    boolean generateTransaction(DateType fromDate, DateType toDate);
    DateType getStartDate();
}
