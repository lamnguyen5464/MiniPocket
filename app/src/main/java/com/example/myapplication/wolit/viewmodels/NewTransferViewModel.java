package com.example.myapplication.wolit.viewmodels;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.framents.FragmentEveryNDay;
import com.example.myapplication.wolit.framents.FragmentFunc;
import com.example.myapplication.wolit.framents.FragmentMonthly;
import com.example.myapplication.wolit.framents.FragmentWeekly;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.NewTranferModel;

import java.util.Calendar;

public class NewTransferViewModel extends BaseObservable {
    Context context;
    private NewTranferModel newTranferModel;
    private FragmentManager fragmentManager;
    private FragmentFunc fragmentFunc;

    public NewTransferViewModel(Context context, FragmentManager fragmentManager){
        newTranferModel = new NewTranferModel();
        fragmentFunc = new FragmentFunc();
        this.context = context;
        this.fragmentManager = fragmentManager;
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
            fragmentFunc.loadFragment(new FragmentWeekly(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void switchToggleGroup(boolean isWeekly, boolean isMonthly, boolean isEveryNDays){
        if (isWeekly) this.newTranferModel.isWeekly = true;
        else this.newTranferModel.isWeekly = false;
        if (isMonthly) this.newTranferModel.isMonthly = true;
        else this.newTranferModel.isMonthly = false;
        if (isEveryNDays) this.newTranferModel.isEveryNDays = true;
        else this.newTranferModel.isEveryNDays = false;
        fragmentFunc.remove(fragmentManager, R.id.frameDatePicker);
    }
    public void onSelectWeekly(){
        if (!this.newTranferModel.isWeekly) {
            this.newTranferModel.isWeekly = true;
            switchToggleGroup(true, false, false);
            fragmentFunc.loadFragment(new FragmentWeekly(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void onSelectMonthly(){
        if (!this.newTranferModel.isMonthly) {
            this.newTranferModel.isMonthly = true;
            switchToggleGroup(false, true, false);
            fragmentFunc.loadFragment(new FragmentMonthly(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void onSelectEveryNDays(){
        if (!this.newTranferModel.isEveryNDays) {
            this.newTranferModel.isEveryNDays = true;
            switchToggleGroup(false, false, true);
            fragmentFunc.loadFragment(new FragmentEveryNDay(context, this.newTranferModel.isEarning), fragmentManager, R.id.frameDatePicker);
        }
        notifyChange();
    }
    public void onClickGetNonRepeatedDay(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DateType.getRecentDate().set(dayOfMonth, month, year);
                newTranferModel.nonRepeatedDate = DateType.getRecentDate().getString();
                notifyChange();
                Toast.makeText(context, "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
