package com.example.myapplication.wolit.viewmodels;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.activities.MainActivity;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.framents.FragmentFunc;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.NewTranferModel;
import com.example.myapplication.wolit.model.tranferdetail.FactoryDetail;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.TransactionDetail;

import java.text.DecimalFormat;

public class EditTransferViewModel extends BaseObservable {
    Context context;
    private NonRepeatedDetail currentDetail;
    private NonRepeatedDetail rootDetail;
    private boolean isEarning;
    public String tmpValue = "";


    public EditTransferViewModel(Context context, NonRepeatedDetail currentDetail){
        this.rootDetail = currentDetail;
        this.currentDetail = RealmApdapter.getInstance().copyFromRealm(currentDetail);
        this.currentDetail.setValue( (this.currentDetail.getValue() > 0) ? this.currentDetail.getValue() : -this.currentDetail.getValue() );
        this.context = context;
        this.isEarning = currentDetail.getValue() > 0;
        afterNoteTextChange(this.currentDetail.getNote());
        afterValueTextChange(String.valueOf(this.currentDetail.getValue()));
    }
    public NonRepeatedDetail getCurrentDetail(){
        return this.currentDetail;
    }
    public void onSwitchEarningClicked(){
        this.isEarning = !this.isEarning;
        notifyChange();
    }

    public void afterNoteTextChange(CharSequence text){
        this.currentDetail.setNote(text.toString());
    }
    public void afterValueTextChange(CharSequence text){
        if (!this.tmpValue.equals(text.toString())){
            //erase comma
            String tmp = text.toString().replace(",", "");
            if (tmp.equals(".")) return;
            this.currentDetail.setValue((!tmp.isEmpty()) ? Double.parseDouble(tmp) : 0);
            DecimalFormat decimalFormat;
            if (tmp.indexOf(".") > 0){
                if (tmp.indexOf(".") == tmp.length()-1){
                    decimalFormat = new DecimalFormat("###,###,###.");
                }else{
                    decimalFormat = new DecimalFormat("###,###,###.0");
                }
            }else{
                decimalFormat = new DecimalFormat("###,###,###");
            }
            this.tmpValue  = (!tmp.isEmpty()) ? decimalFormat.format(this.currentDetail.getValue()) : "";
            notifyChange();
        }
    }

    public void onClickGetNonRepeatedDay(){
        DateType recentDate = DateType.getRecentDate();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentDetail.getDate().set(dayOfMonth, month+1, year);
                notifyChange();
            }
        }, recentDate.getYear(), recentDate.getMonth()-1, recentDate.getDate());
        datePickerDialog.show();
    }

    public boolean isEarning() {
        return isEarning;
    }

    public void setCurrentDetail(NonRepeatedDetail currentDetail) {
        this.currentDetail = currentDetail;
    }

    public NonRepeatedDetail getRootDetail() {
        return rootDetail;
    }

    public void setRootDetail(NonRepeatedDetail rootDetail) {
        this.rootDetail = rootDetail;
    }

    public void setEarning(boolean earning) {
        isEarning = earning;
    }
}
