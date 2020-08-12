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
import com.example.myapplication.wolit.framents.newtransfer.FragmentEveryNDay;
import com.example.myapplication.wolit.framents.FragmentFunc;
import com.example.myapplication.wolit.framents.newtransfer.FragmentMonthly;
import com.example.myapplication.wolit.framents.newtransfer.FragmentWeekly;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.tranferdetail.FactoryDetail;
import com.example.myapplication.wolit.model.NewTranferModel;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.TransactionDetail;

import java.text.DecimalFormat;
import java.util.Calendar;

public class NewTransferViewModel extends BaseObservable {
    Context context;
    private NewTranferModel newTranferModel;
    private FragmentManager fragmentManager;
    public String tmpValue = "";

    public NewTransferViewModel(Context context, FragmentManager fragmentManager){
        newTranferModel = new NewTranferModel();
        this.context = context;
        this.fragmentManager = fragmentManager;
        //get end set date
        NonRepeatedDetail.getRecentDetail().getDate().set(DateType.getRecentDate().getDate(), DateType.getRecentDate().getMonth(), DateType.getRecentDate().getYear());
        newTranferModel.nonRepeatedDate = NonRepeatedDetail.getRecentDetail().getDate().getString();
    }
    public NewTranferModel getNewTranferModel(){
        return this.newTranferModel;
    }
    public void onSwitchEarningClicked(){
        this.newTranferModel.isEarning = !this.newTranferModel.isEarning;
        this.newTranferModel.isRepeated = false;
        switchToggleGroup(false, false, false);
        notifyChange();
    }
    public void onRepeatCheckBoxClicked(){
        this.newTranferModel.isRepeated = !this.newTranferModel.isRepeated;
        if (!this.newTranferModel.isRepeated){
            switchToggleGroup(false, false, false);
        }else{
            switchToggleGroup(true, false, false);
            FragmentFunc.loadFragment(new FragmentWeekly(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void afterNoteTextChange(CharSequence text){
        this.newTranferModel.note = text.toString();
    }
    public void afterValueTextChange(CharSequence text){
        if (!this.tmpValue.equals(text.toString())){
            //erase comma
            String tmp = text.toString().replace(",", "");
            if (tmp.equals(".")) return;
            this.newTranferModel.value = (!tmp.isEmpty()) ? Double.parseDouble(tmp) : 0;
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
            this.tmpValue  = (!tmp.isEmpty()) ? decimalFormat.format(this.newTranferModel.value) : "";
            notifyChange();
        }
    }
    public void switchToggleGroup(boolean isWeekly, boolean isMonthly, boolean isEveryNDays){
        if (isWeekly) this.newTranferModel.isWeekly = true;
        else this.newTranferModel.isWeekly = false;
        if (isMonthly) this.newTranferModel.isMonthly = true;
        else this.newTranferModel.isMonthly = false;
        if (isEveryNDays) this.newTranferModel.isEveryNDays = true;
        else this.newTranferModel.isEveryNDays = false;
        FragmentFunc.remove(fragmentManager, R.id.frameDatePicker);
    }
    public void onSelectWeekly(){
        if (!this.newTranferModel.isWeekly) {
            this.newTranferModel.isWeekly = true;
            switchToggleGroup(true, false, false);
            FragmentFunc.loadFragment(new FragmentWeekly(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void onSelectMonthly(){
        if (!this.newTranferModel.isMonthly) {
            this.newTranferModel.isMonthly = true;
            switchToggleGroup(false, true, false);
            FragmentFunc.loadFragment(new FragmentMonthly(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void onSelectEveryNDays(){
        if (!this.newTranferModel.isEveryNDays) {
            this.newTranferModel.isEveryNDays = true;
            switchToggleGroup(false, false, true);
            FragmentFunc.loadFragment(new FragmentEveryNDay(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void onClickGetNonRepeatedDay(){
        DateType recentDate = DateType.getRecentDate();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                NonRepeatedDetail.getRecentDetail().getDate().set(dayOfMonth, month+1, year);
                newTranferModel.nonRepeatedDate = NonRepeatedDetail.getRecentDetail().getDate().getString();
                notifyChange();
                Toast.makeText(context, "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, recentDate.getYear(), recentDate.getMonth()-1, recentDate.getDate());
        datePickerDialog.show();
    }
    public void onClickAddingButton(){
        TransactionDetail finalDetail = FactoryDetail.getRecentInstance(this.newTranferModel);
        finalDetail.setBasicInfo(((this.newTranferModel.isEarning)?1:-1)*this.newTranferModel.value, this.newTranferModel.note);
        if (finalDetail.isNotValid()){
            Toast.makeText(context, "The information is note valid!", Toast.LENGTH_SHORT).show();
        }else{
            finalDetail.saveToDatabase();
            if (this.newTranferModel.isRepeated){
                DateType fromDate = new DateType(finalDetail.getStartDate());
                finalDetail.generateTransaction(fromDate, DateType.getToday());
            }
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}
