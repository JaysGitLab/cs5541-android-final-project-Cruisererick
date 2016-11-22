package com.example.erick.finalproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.erick.finalproject.database.LocationDBSchema.LocationTable;

/**
 * Created by eric on 10/6/2016.
 */

public class LocationBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public LocationBaseHelper(Context context){
        super(context, DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + LocationTable.NAME +"("+
        "_id integer primary key autoincrement, "+
                LocationTable.Cols.UUID + ","+
                LocationTable.Cols.DESCRIPTION+","+
       // CrimeTable.Cols.DATE+","+
        //CrimeTable.Cols.SOLVED+ "," +
                LocationTable.Cols.LOCATION +")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion
    , int newVersion){

    }

}
