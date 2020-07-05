package com.example.myapplication.wolit.framents;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.widget.PullRefreshLayout;
import com.example.myapplication.wolit.activities.AdapterListViewTransfer;
import com.example.myapplication.wolit.activities.NewTransferActivity;
import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.realm.RealmApdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class FragmentTransfer extends Fragment {
    FloatingActionButton floatingBtAddNew;
    SwipeMenuListView listViewTransfer;
    AdapterListViewTransfer customList;
    public static FragmentTransfer instance = null;
    public static FragmentTransfer getInstance(){
        if (instance == null){
            instance = new FragmentTransfer();
        }
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        //set up listView
        listViewTransfer = view.findViewById(R.id.listViewTransfer);
        customList = new AdapterListViewTransfer(
                getActivity(),
                RealmApdapter.getInstance().where(NonRepeatedDetail.class).sort("date.dateCode").findAll()
        );
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
            }
        };
        listViewTransfer.setMenuCreator(creator);
        listViewTransfer.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listViewTransfer.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.d("@@@","position: "+position+" - indexL: "+index);
                if (index == 0){
                    customList.removePos(position);
                    customList.notifyDataSetChanged();
                }
                return false;
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
