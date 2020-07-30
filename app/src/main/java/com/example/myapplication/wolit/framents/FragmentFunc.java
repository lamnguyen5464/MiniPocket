package com.example.myapplication.wolit.framents;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentFunc {
    public static void loadFragment(Fragment fragment, FragmentManager fragmentManager, int id){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public static void remove(FragmentManager fragmentManager, int id){
        if (fragmentManager.findFragmentById(id) != null)
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(id)).commit();
    }
}
