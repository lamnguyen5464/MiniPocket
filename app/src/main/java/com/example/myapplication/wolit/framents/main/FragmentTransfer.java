package com.example.myapplication.wolit.framents.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.myapplication.wolit.databinding.DialogEditNonrepeatedTransferBinding;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.DayViewContainer;
import com.example.myapplication.wolit.viewmodels.AdapterListViewPending;
import com.example.myapplication.wolit.viewmodels.AdapterListViewTransfer;
import com.example.myapplication.wolit.activities.NewTransferActivity;
import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.viewmodels.EditTransferViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

public class FragmentTransfer extends Fragment {
    FloatingActionButton floatingBtAddNew;
    static SwipeMenuListView listViewTransfer;
    static AdapterListViewTransfer customList;
    AdapterListViewPending customPendingList;
    static CalendarView calendarView;
    Switch switchListView;
    TextView labelTitle;
    static TextView labelNoTransaction;
    TextView labelNoRepeatedTransaction;
    DateType selectedDate = DateType.getRecentDate();
    DayViewContainer preContainer = null;
    static Dialog dialogEdit;
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
    void showPopUpView(NonRepeatedDetail detail){
        DialogEditNonrepeatedTransferBinding binding
                = DataBindingUtil.inflate(
                        LayoutInflater.from(getContext()),
                        R.layout. dialog_edit_nonrepeated_transfer,
                        null,
                        false);

        dialogEdit = new Dialog(getContext());
        dialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEdit.setCanceledOnTouchOutside(true);
        dialogEdit.setContentView(binding.getRoot());
        dialogEdit.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit.cancel();
            }
        });
        ((Switch)dialogEdit.findViewById(R.id.switch_select)).setTypeface(ResourcesCompat.getFont(getContext(), R.font.chalkboard));
        binding.setCurrentTransfer(new EditTransferViewModel(getContext(), detail));
        dialogEdit.show();
    }
    @BindingAdapter("onClickSaving")
    public static void setOnClick(Button button, final EditTransferViewModel currentTransfer){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmApdapter.getInstance().beginTransaction();
                currentTransfer.getRootDetail().setValue((currentTransfer.isEarning()) ? currentTransfer.getCurrentDetail().getValue() : -currentTransfer.getCurrentDetail().getValue());
                currentTransfer.getRootDetail().setNote(currentTransfer.getCurrentDetail().getNote());
                currentTransfer.getRootDetail().getDate().set(currentTransfer.getCurrentDetail().getDate());
                RealmApdapter.getInstance().commitTransaction();
                //change UI
                customList.updateBoundOfDate(DateType.getRecentDate());
                calendarView.notifyCalendarChanged();
                labelNoTransaction.setVisibility((customList.getCount() == 0) ? View.VISIBLE : View.INVISIBLE);
                dialogEdit.cancel();
            }
        });
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

        YearMonth currentMonth = YearMonth.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        calendarView.setup(currentMonth.minusMonths(12), currentMonth.plusMonths(12), WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
        calendarView.scrollToDate(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate()));
        calendarView.setDayHeight(((int) getContext().getResources().getDisplayMetrics().density * 80));
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
                final int firstIndex = customList.lowerBound(dateType);
                final int lastIndex = customList.upperBound(dateType);
                if (!dateType.isDifferent(selectedDate)){
                    if (preContainer == null) selectDate(container, dateType, firstIndex != -1);
                }
                boolean haveEarning, havePaying;
                haveEarning = havePaying = false;
                if (firstIndex != -1){
                    for(int index = firstIndex; index <= lastIndex; index++){
                        if (customList.get(index).getValue() >= 0)
                            haveEarning = true;
                        else
                            havePaying = true;
                    }
                }
                container.markEarning.setVisibility((haveEarning) ? View.VISIBLE : View.INVISIBLE);
                container.markPaying.setVisibility((havePaying) ? View.VISIBLE : View.INVISIBLE);
//                Log.d("@@@", String.valueOf(day.getDate().getDayOfMonth()) + " "+String.valueOf(day.getDate().getMonthValue()) + " "+String.valueOf(day.getDate().getYear()));
                container.btDayCell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDate(container, dateType, firstIndex != -1);
//                        container.markEarning.setVisibility((customList.havingEarning()) ? View.VISIBLE : View.INVISIBLE);
//                        container.markPaying.setVisibility((customList.havingPaying()) ? View.VISIBLE : View.INVISIBLE);
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
                SwipeMenuItem editItem = new SwipeMenuItem(getActivity());
                // set item width
                editItem.setWidth((170));
                editItem.setBackground(getResources().getDrawable(R.drawable.custom_background_earning));
                // set a icon
                editItem.setIcon(R.drawable.icon_edit);
                // add to menu
                menu.addMenuItem(editItem);


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
        listViewTransfer.setMenuCreator(creator);
        listViewTransfer.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listViewTransfer.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                Log.d("@@@","position: "+position+" - indexL: "+index);
                switch (index){
                    case 0:         //edit
                        if (switchListView.isChecked()){
                            Toast.makeText(getContext(), "This tab cannot be changed", Toast.LENGTH_SHORT).show();
                        }else{
                            showPopUpView(customList.get(customList.getRealPosition(position)));
                        }
                        break;
                    case 1:        //delete
                        if (switchListView.isChecked()){
                            customPendingList.removePos(position);
                            customPendingList.notifyDataSetChanged();
                            labelNoRepeatedTransaction.setVisibility((customPendingList.getCount() == 0) ? View.VISIBLE : View.INVISIBLE);
                        }else{
                            customList.removePos(position);
                            customList.updateBoundOfDate(selectedDate);
                            calendarView.notifyCalendarChanged();
                            labelNoTransaction.setVisibility((customList.getCount() == 0) ? View.VISIBLE : View.INVISIBLE);
                        }
                        break;
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
                    calendarView.setVisibility(View.GONE);
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
