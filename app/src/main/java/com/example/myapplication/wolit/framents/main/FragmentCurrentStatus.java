package com.example.myapplication.wolit.framents.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.widget.PullRefreshLayout;
import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.activities.NewTransferActivity;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.framents.FragmentFunc;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.PocketVers;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.viewmodels.AdapterListViewPocket;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;


public class FragmentCurrentStatus extends Fragment implements CompoundButton.OnClickListener,CompoundButton.OnLongClickListener {
    public static FragmentCurrentStatus instance = null;
    TextView labelYourMoney;
    DateType startDate = new DateType(), endDate = DateType.getToday();
    LineChart lineChart;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    CheckBox checkBoxFilter;
    RelativeLayout layoutFilter;
    EditText filterNote;
    Button btStartDate;
    Button btEndDate;
    FragmentManager fragmentManager;
    public static FragmentCurrentStatus getInstance(boolean newFrame, FragmentManager fragmentManager){
        if (instance == null || newFrame){
            instance = new FragmentCurrentStatus();
            instance.fragmentManager = fragmentManager;
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_status, container, false);

        labelYourMoney = view.findViewById(R.id.yourMoney);
        checkBoxFilter = view.findViewById(R.id.checkBoxFilter);
        layoutFilter = view.findViewById(R.id.filter);
        filterNote = view.findViewById(R.id.filterNote);
        ((Button)view.findViewById(R.id.btPocketSelect)).setText(PocketVers.getCurrentVers().getLabel());


        checkBoxFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    layoutFilter.setVisibility(View.VISIBLE);
                    buttonView.setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE));
                    btEndDate.setText(endDate.getString());
                    btStartDate.setText(startDate.getString());
                }else{
                    layoutFilter.setVisibility(View.GONE);
                    buttonView.setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE_DARK));
                    endDate = DateType.getToday();
                    startDate.reset();
                    filterNote.setText("");
                    refreshDataForChart();
                }
            }
        }); 
        //filterText

        //chart
        lineChart = view.findViewById(R.id.chart);


        //format x axis
        lineChart.getXAxis().setValueFormatter(new MyAxisFormatter());


        //disable grid
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);

        //set textColor axis
        lineChart.getAxisLeft().setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE));
        lineChart.getXAxis().setTextColor(ContextCompat.getColor(getContext(), R.color.WHITE));

        //disable description
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        refreshDataForChart();

        //pullRefreshLayout
        final PullRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDataForChart();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //button get Date
        btStartDate = view.findViewById(R.id.startDate);
        btEndDate = view.findViewById(R.id.endDate);
        btEndDate.setOnClickListener(this);
        btEndDate.setOnLongClickListener(this);
        btStartDate.setOnClickListener(this);
        btStartDate.setOnLongClickListener(this);
        btEndDate.setText(endDate.getString());
        btStartDate.setText(startDate.getString());

        view.findViewById(R.id.btRefresh).setOnClickListener(this);
        view.findViewById(R.id.btPocketSelect).setOnClickListener(this);

        view.findViewById(R.id.floatBtAddNew).setOnClickListener(this);
        return view;
    }
    private void refreshDataForChart(){
        lineChart.setData(getLineData());
        lineChart.invalidate();
        lineChart.animateX(1000, Easing.EaseInSine);


    }
    private LineData getLineData(){
        RealmResults<NonRepeatedDetail> nonRepeatedDetails = RealmApdapter.getAllNonRepeatedList();

        List<Entry> points = new ArrayList<>();
        float total = 0;
        DateType preDate = new DateType();
        long dateCode = 0;
        List<NonRepeatedDetail> listFiltered = new ArrayList<>();
        for(NonRepeatedDetail tmp : nonRepeatedDetails){
            //bound
            if (tmp.getDate().getDateCode() > endDate.getDateCode()) break;
            if (!startDate.isEmptyDate() && startDate.getDateCode() > tmp.getDate().getDateCode()) continue;
            String tmpNote = filterNote.getText().toString();
            if (tmpNote.length() > 0 && tmp.getNote().indexOf(tmpNote) == -1) continue;
            total = total + (float)tmp.getValue();
            if (preDate.isDifferent(tmp.getDate())){
                dateCode =  tmp.getDate().getMilisecondCode();
                preDate.set(tmp.getDate());
            }else{
                dateCode = dateCode + 10000000;
            }
            points.add( new Entry(dateCode, total) );
//          add to list
            listFiltered.add(tmp);
        }


        //set label sum
        labelYourMoney.setText(decimalFormat.format(total));
        //
        LineDataSet dataSet = new LineDataSet(points, "Sum");
        dataSet.setLineWidth(5);
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.YELLOW));
//        dataSet.setDrawCircles(false);
        dataSet.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.YELLOW));
        dataSet.setCircleColor(ContextCompat.getColor(getContext(), R.color.YELLOW));
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.GRAY));

        return new LineData(dataSet);
    }
    private void pickDateFromCalendar(final Button btPicker){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (btPicker.getId() == R.id.startDate) startDate.set(dayOfMonth, month+1, year);
                else endDate.set(dayOfMonth, month+1, year);
                btPicker.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                refreshDataForChart();
                Toast.makeText(getContext(), "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void eraseDate(Button btPicker){
        btPicker.setText("__/__/____");
        if (btPicker.getId() == R.id.startDate) startDate.reset();
        else endDate.reset();
        refreshDataForChart();
    }
    private void showBottomDialogPocketSelect(){
        final BottomSheetDialog bottomSheetPocketSelector = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetPocketSelector.setContentView(R.layout.dialog_pocket_picker);

        final SwipeMenuListView listViewPocket = bottomSheetPocketSelector.findViewById(R.id.listPocket);
        final RealmResults<PocketVers> listPockets = RealmApdapter.getPockets();

        final AdapterListViewPocket adapterListViewPocket = new AdapterListViewPocket(getContext(), listPockets);
        listViewPocket.setAdapter(adapterListViewPocket);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem mergeItem = new SwipeMenuItem(getActivity());
                // set item width
                mergeItem.setWidth((150));
                mergeItem.setBackground(getResources().getDrawable(R.drawable.custom_background_earning));
                // set a icon
                mergeItem.setIcon(R.drawable.icon_merge);
                // add to menu
                menu.addMenuItem(mergeItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item width
                deleteItem.setWidth((150));
                deleteItem.setBackground(getResources().getDrawable(R.drawable.custom_background_paying));
                // set a icon
                deleteItem.setIcon(R.drawable.icon_delete);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };
        listViewPocket.setMenuCreator(creator);
        listViewPocket.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listViewPocket.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        if (position == 0){
                            Toast.makeText(getContext(), "Needn't do it!", Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(true);
                            builder.setTitle("Add all transactions to Main pocket");
                            builder.setMessage("Are you sure?");
                            builder.setPositiveButton("Confirm",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RealmApdapter.mergeAllToMain(listPockets.get(position));
                                            FragmentFunc.loadFragment(FragmentCurrentStatus.getInstance(true, fragmentManager), fragmentManager,  R.id.frameLayout);
                                            Toast.makeText(getContext(), "Added all transactions of " + listPockets.get(position).getLabel() + " to Main pocket", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            builder.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        break;
                    case 1:
                        if (position == 0 || listPockets.get(position).getLabel().equals(PocketVers.getCurrentVers().getLabel())){
                            Toast.makeText(getContext(), "Cannot remove this wallet!", Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(true);
                            builder.setTitle("Delete "+ listPockets.get(position).getLabel());
                            builder.setMessage("Are you sure?");
                            builder.setPositiveButton("Confirm",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RealmApdapter.eraseAll(RealmApdapter.getRealm(listPockets.get(position).getLabel()));
                                            Toast.makeText(getContext(), "Delete " + listPockets.get(position).getLabel() + " successfully", Toast.LENGTH_SHORT).show();
                                            listPockets.get(position).removeFromDataBase();
                                        }
                                    });
                            builder.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        break;
                }
                bottomSheetPocketSelector.cancel();
                return false;
            }
        });
        listViewPocket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterListViewPocket.onSelected(position);
//                adapterListViewPocket.notifyDataSetChanged();
                bottomSheetPocketSelector.cancel();
                FragmentFunc.loadFragment(FragmentCurrentStatus.getInstance(true, fragmentManager), fragmentManager,  R.id.frameLayout);
            }
        });

        bottomSheetPocketSelector.findViewById(R.id.btAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCreatePocket(listPockets);
                bottomSheetPocketSelector.cancel();
            }
        });


        bottomSheetPocketSelector.show();
    }
    private void showDialogCreatePocket(final RealmResults<PocketVers> listPockets){
        final Dialog dialogCreatePocket = new Dialog(getContext());
        dialogCreatePocket.setContentView(R.layout.dialog_pocket_create);
        dialogCreatePocket.setCanceledOnTouchOutside(true);
        dialogCreatePocket.show();

        dialogCreatePocket.findViewById(R.id.button_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePicker = ((EditText)dialogCreatePocket.findViewById(R.id.edit_text_new_pocket)).getText().toString();
                if (!namePicker.equals("")){
                    //check existing
                    for(PocketVers curPocket : listPockets){
                        if (namePicker.equals(curPocket.getLabel())){ // existing!!
                            Toast.makeText(getContext(), "This name is already in used!", Toast.LENGTH_SHORT).show();
                            dialogCreatePocket.cancel();
                            return;
                        }
                    }
                    //add
                    PocketVers.addNewPocket(namePicker);
                    Toast.makeText(getContext(), "Create \"" + namePicker + "\" successfully!", Toast.LENGTH_SHORT).show();
                    FragmentFunc.loadFragment(FragmentCurrentStatus.getInstance(true, fragmentManager), fragmentManager,  R.id.frameLayout);
                }else{
                    Toast.makeText(getContext(), "Try again, name of pocket cannot be empty!!", Toast.LENGTH_SHORT).show();
                }
                dialogCreatePocket.cancel();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                pickDateFromCalendar(((Button)v.findViewById(R.id.startDate)));
                break;
            case R.id.endDate:
                pickDateFromCalendar(((Button)v.findViewById(R.id.endDate)));
                break;
            case R.id.btRefresh:
                refreshDataForChart();
                break;
            case R.id.btPocketSelect:
                showBottomDialogPocketSelect();
                break;
            case R.id.floatBtAddNew:
                startActivity(new Intent(getActivity(), NewTransferActivity.class));
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.startDate:
                eraseDate(((Button)v.findViewById(R.id.startDate)));
                return true;
            case R.id.endDate:
                eraseDate(((Button)v.findViewById(R.id.endDate)));
                return true;
        }
        return false;
    }
}
class MyAxisFormatter extends ValueFormatter {

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String holder = dateFormat.format(new Date( (long)value ));
        String dateMonth = "";
        for(int index = 0; index < holder.length(); index++){
            if (holder.charAt(index) == ',') break;
            dateMonth += holder.charAt(index);
        }
//        Log.d("@@@", dateMonth+ " "+holder);
        return dateMonth;
    }
}