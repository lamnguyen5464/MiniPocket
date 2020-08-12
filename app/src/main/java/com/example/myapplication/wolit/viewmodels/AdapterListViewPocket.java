package com.example.myapplication.wolit.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.model.PocketVers;

import io.realm.RealmResults;

public class AdapterListViewPocket extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    RealmResults<PocketVers> listPocket;
    public AdapterListViewPocket(Context context, RealmResults<PocketVers> listPocket){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listPocket = listPocket;
    }
    @Override
    public int getCount() {
        return listPocket.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void onSelected(int position){
        RealmApdapter.switchInstanceToVers(listPocket.get(position).getLabel());
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview_pocket, null);
            viewHolder = new ViewHolder();
            //define
            viewHolder.labelName = convertView.findViewById(R.id.labelName);
            viewHolder.mark = convertView.findViewById(R.id.mark);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.labelName.setText(listPocket.get(position).getLabel());
        if (listPocket.get(position).getLabel().equals(PocketVers.getCurrentVers().getLabel())){
            viewHolder.mark.setImageResource(R.drawable.icon_mark);
        }else{
            viewHolder.mark.setImageResource(R.drawable.icon_not_mark);
        }
        //col
        return convertView;
    }
    static class ViewHolder{
        TextView labelName;
        ImageView mark;
    }
}
