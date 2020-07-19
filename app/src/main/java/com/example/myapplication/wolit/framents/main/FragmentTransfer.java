package com.example.myapplication.wolit.framents.main;

import android.content.Intent;
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
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.DayViewContainer;
import com.example.myapplication.wolit.viewmodels.AdapterListViewPending;
import com.example.myapplication.wolit.viewmodels.AdapterListViewTransfer;
import com.example.myapplication.wolit.activities.NewTransferActivity;
import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

public class FragmentTransfer extends Fragment {
    FloatingActionButton floatingBtAddNew;
    SwipeMenuListView listViewTransfer;
    AdapterListViewTransfer customList;
    AdapterListViewPending customPendingList;
    CalendarView calendarView;
    Switch switchListView;
    TextView labelTitle, labelNoTransaction, labelNoRepeatedTransaction;
    DateType selectedDate = DateType.getRecentDate();
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

    private void selectDate(DayViewContainer container, DateType pickDate, boolean haveTransactions){
        DateType.getRecentDate().set(pickDate);
        labelNoTransaction.setVisibility((haveTransactions == false) ? View.VISIBLE : View.INVISIBLE);
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
        labelNoTransaction = view.findViewById(R.id.labelNoTransaction);
        labelNoRepeatedTransaction = view.findViewById(R.id.labelNoRepeatedTransaction);


        //calendar
        calendarView = view.findViewById(R.id.calendarView);

        YearMonth currentMonth = YearMonth.now();
        calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
        calendarView.scrollToDate(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate()));
        calendarView.setDayHeight(150);
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
                final int numOfTrans = customList.lowerBound(dateType);
                if (!dateType.isDifferent(selectedDate)){
                    if (preContainer == null) selectDate(container, dateType, numOfTrans != -1);
                }
                container.markText.setVisibility((numOfTrans == -1) ? View.INVISIBLE : View.VISIBLE);
//                Log.d("@@@", String.valueOf(day.getDate().getDayOfMonth()) + " "+String.valueOf(day.getDate().getMonthValue()) + " "+String.valueOf(day.getDate().getYear()));
                container.btDayCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDate(container, dateType, numOfTrans != -1);
                        container.markText.setVisibility((numOfTrans != -1) ? View.VISIBLE : View.INVISIBLE);
                    }
                });
            }
        });


        //set up listView
        listViewTransfer = view.findViewById(R.id.listViewTransfer);
        customList = new AdapterListViewTransfer(
                getActivity(),
                RealmApdapter.getAllNonRepeatedList()
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
                deleteItem.setWidth((150));
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
                        labelNoRepeatedTransaction.setVisibility((customPendingList.getCount() == 0) ? View.VISIBLE : View.INVISIBLE);
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
                    selectDate(preContainer, selectedDate, customList.lowerBound(selectedDate) != -1);
                    labelNoRepeatedTransaction.setVisibility(View.INVISIBLE);
                }else{
                    labelNoTransaction.setVisibility(View.INVISIBLE);
                    labelNoRepeatedTransaction.setVisibility((customPendingList.getCount() == 0) ? View.VISIBLE : View.INVISIBLE);
                    Log.d("@@@", customPendingList.getCount()+"");
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
