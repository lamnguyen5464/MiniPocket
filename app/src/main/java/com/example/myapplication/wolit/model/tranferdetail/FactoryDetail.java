package com.example.myapplication.wolit.model.tranferdetail;

import com.example.myapplication.wolit.model.NewTranferModel;

public class FactoryDetail {
    public static TransactionDetail get(NewTranferModel model){
        if (model.isWeekly) return new WeeklyDetail();
        if (model.isMonthly) return new MonthlyDetail();
        if (model.isEveryNDays) return  new EveryNDayDetail();
        return new NonRepeatedDetail();
    }

    public static TransactionDetail getRecentInstance(NewTranferModel model){
        if (model.isWeekly) return  WeeklyDetail.getRecentDetail();
        if (model.isMonthly) return  MonthlyDetail.getRecentDetail();
        if (model.isEveryNDays) return  EveryNDayDetail.getRecentDetail();
        return NonRepeatedDetail.getRecentDetail();
    }
}
