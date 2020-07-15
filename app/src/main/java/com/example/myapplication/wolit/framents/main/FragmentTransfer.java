package com.example.myapplication.wolit.framents.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.myapplication.wolit.model.CurrentStatus;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.DayViewContainer;
import com.example.myapplication.wolit.model.tranferdetail.TransactionDetail;
import com.example.myapplication.wolit.viewmodels.AdapterListViewPending;
import com.example.myapplication.wolit.viewmodels.AdapterListViewTransfer;
import com.example.myapplication.wolit.activities.NewTransferActivity;
import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.realm.RealmApdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.WeekFields;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import io.realm.Sort;

public class FragmentTransfer extends Fragment {
    FloatingActionButton floatingBtAddNew;
    SwipeMenuListView listViewTransfer;
    AdapterListViewTransfer customList;
    AdapterListViewPending customPendingList;
    CalendarView calendarView;
    Switch switchListView;
    TextView labelTitle;
    DateType selectedDate = DateType.getToday();
    DayViewContainer preContainer = null;
//    public static FragmentTransfer instance = null;
//    public static FragmentTransfer getInstance(){
//        if (instance == null){
//            instance = new FragmentTransfer();
//        }
//        return instance;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void selectDate(DayViewContainer container, DateType pickDate){
        if (preContainer != container){
//            Log.d("@@@", selectedDate.getString());
            selectedDate.set(pickDate);

            container.selectedBackground.setVisibility(View.VISIBLE);
            if (preContainer != null)
                preContainer.selectedBackground.setVisibility(View.INVISIBLE);
            preContainer = container;

            customList.updateBoundOfDate(selectedDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        //
        switchListView = view.findViewById(R.id.switchListView);
        labelTitle = view.findViewById(R.id.title);

        //calendar
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }
            @Override
            public void bind(@NonNull final DayViewContainer container, @NonNull CalendarDay day) {
                final DateType dateType = new DateType(day.getDate().getDayOfMonth(), day.getDate().getMonthValue(), day.getDate().getYear());
                container.labelDate.setText((day.getDate().getDayOfMonth()) + "-" + (day.getDate().getMonthValue()));
                container.dayOfWeek.setText(DateType.numToStringDay(dateType.getDayOfWeek()));
                //pick today as default
                if (preContainer == null && !dateType.isDifferent(selectedDate)){
                    selectDate(container, dateType);
                }
                if (customList.lowerBound(dateType) != -1)
                    container.markText.setVisibility(View.VISIBLE);
                else
                    container.markText.setVisibility(View.INVISIBLE);
//                Log.d("@@@", String.valueOf(day.getDate().getDayOfMonth()) + " "+String.valueOf(day.getDate().getMonthValue()) + " "+String.valueOf(day.getDate().getYear()));
                container.btDayCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDate(container, dateType);
                        if (customList.lowerBound(dateType) != -1)
                            container.markText.setVisibility(View.VISIBLE);
                        else
                            container.markText.setVisibility(View.INVISIBLE);
//                        customList.notifyDataSetChanged();
                    }
                });
            }
        });

        YearMonth currentMonth = YearMonth.now();
        calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(10), WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
        calendarView.scrollToMonth(YearMonth.now());
        calendarView.scrollToDate(LocalDate.now());
        calendarView.setDayHeight(150);

        //set up listView
        listViewTransfer = view.findViewById(R.id.listViewTransfer);
        customList = new AdapterListViewTransfer(
                getActivity(),
                RealmApdapter.getInstance().where(NonRepeatedDetail.class).sort("date.dateCode").findAll()
        );
        customPendingList = new AdapterListViewPending(
                getActivity(),
                RealmApdapter.getPendingList()
        );
//        Log.d("@@@", "_______________________");
//        final List<TransactionDetail> list = RealmApdapter.getPendingList();
//        for(TransactionDetail tmp : list){
//            Log.d("@@@", tmp.is() + " " + tmp.getValue() + " " + tmp.getStartDate().getString() + " " +tmp.getEndDate().getString());
//        }

        listViewTransfer.setAdapter(customList);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item width
                deleteItem.setWidth((170));
                deleteItem.setBackground(getResources().getDrawable(R.drawable.custom_background_paying));
                // set a icon
                deleteItem.setIcon(R.drawable.icon_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
                //

//                SwipeMenuItem editItem = new SwipeMenuItem(getActivity());
//                // set item width
//                editItem.setWidth((170));
//                editItem.setBackground(getResources().getDrawable(R.drawable.custom_background_earning));
//                // set a icon
//                editItem.setIcon(R.drawable.icon_delete);
//                // add to menu
//                menu.addMenuItem(editItem);
            }
        };
        listViewTransfer.setMenuCreator(creator);
        listViewTransfer.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listViewTransfer.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                Log.d("@@@","position: "+position+" - indexL: "+index);
                if (index == 0){
                    if (!switchListView.isChecked()){
                        customList.removePos(position);
                        customList.updateBoundOfDate(selectedDate);
                        calendarView.notifyCalendarChanged();
                    }else{
                        customPendingList.removePos(position);
                        customPendingList.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        //
        switchListView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    calendarView.setVisibility(View.VISIBLE);
                    listViewTransfer.setAdapter(customList);
                    labelTitle.setText("Transaction");
                }else{
                    calendarView.setVisibility(View.INVISIBLE);
                    listViewTransfer.setAdapter(customPendingList);
                    labelTitle.setText("Pending");
                }
            }
        });

        //
        floatingBtAddNew = view.findViewById(R.id.floatBtAddNew);
        floatingBtAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewTransferActivity.class));
            }
        });
        return view;
    }
}
