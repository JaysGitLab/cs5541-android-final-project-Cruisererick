package com.example.erick.finalproject;

import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Juan on 11/15/2016.
 */

public class Map extends SupportMapFragment {



    public static Map newInstance(){
        return new Map();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

}
