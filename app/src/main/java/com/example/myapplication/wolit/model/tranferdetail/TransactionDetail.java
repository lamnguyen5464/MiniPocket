package com.example.myapplication.wolit.model.tranferdetail;

import com.example.myapplication.wolit.model.DateType;

public interface TransactionDetail {
     double getValue();
     String getNote();
    void reset();
    boolean isNotValid();
    void saveToDatabase();
    void removeFromDatabase();
    void setBasicInfo(double value, String note);
    boolean generateTransaction(DateType fromDate, DateType toDate);
    DateType getStartDate();
    DateType getEndDate();
    String is();
}
