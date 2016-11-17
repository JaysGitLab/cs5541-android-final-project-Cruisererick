package com.example.erick.finalproject;

import android.support.v4.app.Fragment;

/**
 * Created by Erick on 9/20/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
