package com.example.myapplication.wolit.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.tranferdetail.EveryNDayDetail;
import com.example.myapplication.wolit.model.tranferdetail.MonthlyDetail;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.TransactionDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;
import com.example.myapplication.wolit.realm.RealmApdapter;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.RealmResults;

public class AdapterListViewPending extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<TransactionDetail> listPending;
    public AdapterListViewPending(Context context, List<TransactionDetail> listPending){
        this.context = context;
        this.listPending = listPending;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listPending.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview_pending, null);
            viewHolder = new ViewHolder();
            //define
            viewHolder.labelValue = convertView.findViewById(R.id.labelValue);
            viewHolder.labelNote = convertView.findViewById(R.id.labelNote);
            viewHolder.labelStartDate = convertView.findViewById(R.id.labelStartDate);
            viewHolder.labelEndDate = convertView.findViewById(R.id.labelEndDate);
            viewHolder.labelMain = convertView.findViewById(R.id.labelMain);
//            Typeface typeface = ResourcesCompat.getFont(context, R.font.math_tapping);
//            viewHolder.labelValue.setTypeface(typeface);
//            viewHolder.labelNote.setTypeface(typeface);
//            viewHolder.labelDate.setTypeface(typeface);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        TransactionDetail detail = listPending.get(listPending.size()-1-position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        double value = detail.getValue();
        if (value < 0){
            viewHolder.labelMain.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_button_back_paying));
            viewHolder.labelValue.setTextColor(ContextCompat.getColor(context, R.color.RED));
            viewHolder.labelNote.setTextColor(ContextCompat.getColor(context, R.color.RED));
            viewHolder.labelMain.setTextColor(ContextCompat.getColor(context, R.color.RED));
//            viewHolder.labelDate.setTextColor(ContextCompat.getColor(context, R.color.RED));
        }else{
            viewHolder.labelMain.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_button_back_earning));
            viewHolder.labelValue.setTextColor(ContextCompat.getColor(context, R.color.GREEN));
            viewHolder.labelNote.setTextColor(ContextCompat.getColor(context, R.color.GREEN));
            viewHolder.labelMain.setTextColor(ContextCompat.getColor(context, R.color.GREEN));
        }
        viewHolder.labelValue.setText((value > 0 ? "+" : "")+decimalFormat.format(value));
        viewHolder.labelNote.setText(detail.getNote());

        viewHolder.labelStartDate.setText(detail.getStartDate().getString());
        viewHolder.labelEndDate.setText(detail.getEndDate().getString());

        switch (detail.is()){
            case "WeeklyDetail":
                String textSet = "Weekly on";
                String tmpDateCode = ((WeeklyDetail)detail).getDayOfWeekCode();
                String comma = "";
                for(int i = 0; i < tmpDateCode.length(); i++)
                    if (tmpDateCode.charAt(i) == '1'){
                        textSet += comma + " " + DateType.numToStringDay(i);
                        comma = ",";
                    }
                viewHolder.labelMain.setText(textSet);
                break;

            case "MonthlyDetail":
                int dateOfMonth = ((MonthlyDetail)detail).getDateOfMonth();
                viewHolder.labelMain.setText("Monthly on date: " + ((dateOfMonth < 10) ? "0" : "") + dateOfMonth);
                break;

            case "EveryNDayDetail":
                viewHolder.labelMain.setText("Every " + ((EveryNDayDetail)detail).getNumOfDays() + " day" + ((((EveryNDayDetail)detail).getNumOfDays() > 1) ? "s" : ""));
                break;
        }

        //col
        return convertView;
    }
    static class ViewHolder{
        TextView labelValue;
        TextView labelNote;
        TextView labelStartDate;
        TextView labelEndDate;
        TextView labelMain;
    }
    public void removePos(int pos){
        RealmApdapter.removeFromCombiningList(listPending.size()-1-pos);
        listPending.remove(listPending.size()-1-pos);
    }
}
