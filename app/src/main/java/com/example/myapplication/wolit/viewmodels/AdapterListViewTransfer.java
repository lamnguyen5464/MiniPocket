package com.example.myapplication.wolit.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

import io.realm.RealmResults;

public class AdapterListViewTransfer extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    RealmResults<NonRepeatedDetail> arrayTransactions;
    private int firstIndex, lastIndex;
    public AdapterListViewTransfer(Context context, RealmResults<NonRepeatedDetail> arrayTransactions){
        this.context = context;
        this.arrayTransactions = arrayTransactions;
        this.layoutInflater = LayoutInflater.from(context);
        firstIndex = -1;
        lastIndex = -1;

    }
    public int lowerBound(DateType date){
        if (arrayTransactions.size() == 0) return -1;
        int left = 0, right = arrayTransactions.size() - 1;
        while (left < right){           //lower bound
            int mid = (left + right) / 2;
            if (arrayTransactions.get(mid).getDate().getDateCode() >= date.getDateCode())
                right = mid;
            else left = mid + 1;
        }
        if (arrayTransactions.get(left).getDate().getDateCode() != date.getDateCode())
            return -1;
        return left;
    }
    public int upperBound(DateType date){
        if (arrayTransactions.size() == 0) return -1;
        int left = 0, right = arrayTransactions.size() - 1;
        while (left < right){           //upper bound
            int mid = (left + right + 1) / 2;
            if (arrayTransactions.get(mid).getDate().getDateCode() <= date.getDateCode())
                left = mid;
            else right = mid - 1;
        }
        if (arrayTransactions.get(left).getDate().getDateCode() != date.getDateCode())
            return -1;
        return left;
    }
    public void updateBoundOfDate(DateType date){

        this.firstIndex = lowerBound(date);
        this.lastIndex = upperBound(date);

        notifyDataSetChanged();
//        Log.d("@@@", this.firstIndex + " " + this.lastIndex);
    }
    public NonRepeatedDetail get(int pos){
        return arrayTransactions.get(pos);
    }
    @Override
    public int getCount() {
        return (firstIndex == -1) ? 0 : (lastIndex - firstIndex + 1);
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
            convertView = layoutInflater.inflate(R.layout.listview_transfer, null);
            viewHolder = new ViewHolder();
            //define
            viewHolder.labelValue = convertView.findViewById(R.id.labelValue);
            viewHolder.labelNote = convertView.findViewById(R.id.labelNote);
            viewHolder.labelDate = convertView.findViewById(R.id.labelDate);
            viewHolder.labelMonthYear = convertView.findViewById(R.id.labelMonthYear);
//            Typeface typeface = ResourcesCompat.getFont(context, R.font.math_tapping);
//            viewHolder.labelValue.setTypeface(typeface);
//            viewHolder.labelNote.setTypeface(typeface);
//            viewHolder.labelDate.setTypeface(typeface);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        Log.d("@@@", arrayTransactions.get(lastIndex - position).getValue()+" "+arrayTransactions.get(lastIndex - position).getNote());
        double value = arrayTransactions.get(lastIndex - position).getValue();
        if (value < 0){
            viewHolder.labelValue.setTextColor(ContextCompat.getColor(context, R.color.RED));
            viewHolder.labelNote.setTextColor(ContextCompat.getColor(context, R.color.RED));
//            viewHolder.labelDate.setTextColor(ContextCompat.getColor(context, R.color.RED));
        }else{
            viewHolder.labelValue.setTextColor(ContextCompat.getColor(context, R.color.GREEN));
            viewHolder.labelNote.setTextColor(ContextCompat.getColor(context, R.color.GREEN));
        }
        viewHolder.labelValue.setText((value > 0 ? "+" : "")+decimalFormat.format(value));
        viewHolder.labelNote.setText(arrayTransactions.get(lastIndex - position).getNote());
        DateType tmpDate = arrayTransactions.get(lastIndex - position).getDate();
        viewHolder.labelDate.setText((tmpDate.getDate()<10 ? "0" : "") + tmpDate.getDate());
        viewHolder.labelMonthYear.setText("/"+ tmpDate.getMonth()+"/"+tmpDate.getYear());
        //col
        return convertView;
    }
    static class ViewHolder{
        TextView labelValue;
        TextView labelNote;
        TextView labelMonthYear;
        TextView labelDate;
    }
    public int getRealPosition(int pos){
        return lastIndex - pos;
    }
    public void removePos(int pos){
        arrayTransactions.get(lastIndex - pos).removeFromDatabase();
    }
}
