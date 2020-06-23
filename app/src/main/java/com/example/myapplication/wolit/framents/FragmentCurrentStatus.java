package com.example.myapplication.wolit.framents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.wolit.R;


public class FragmentCurrentStatus extends Fragment {
    public static FragmentCurrentStatus instance = null;

    public static FragmentCurrentStatus getInstance(){
        if (instance == null){
            instance = new FragmentCurrentStatus();
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
        return inflater.inflate(R.layout.fragment_current_status, container, false);
    }
}
