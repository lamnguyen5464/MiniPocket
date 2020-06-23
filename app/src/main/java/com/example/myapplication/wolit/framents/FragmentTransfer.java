package com.example.myapplication.wolit.framents;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.wolit.activities.NewTransferActivity;
import com.example.myapplication.wolit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FragmentTransfer extends Fragment {
    FloatingActionButton floatingBtAddNew;
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
