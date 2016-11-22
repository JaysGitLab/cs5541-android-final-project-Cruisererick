package com.example.erick.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.erick.finalproject.database.LocationBaseHelper;
import com.example.erick.finalproject.database.CrimeCursorWrapper;
import com.example.erick.finalproject.database.LocationDBSchema.LocationTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Erick on 9/20/2016.
 */
public class LocationLab {

    private static LocationLab sLocationLab;
    //private List<Location> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static LocationLab get(Context contex){
        if(sLocationLab == null){
            sLocationLab = new LocationLab(contex);
        }
        return sLocationLab;
    }

    private LocationLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new LocationBaseHelper(mContext).getWritableDatabase();
        //mCrimes = new ArrayList<>();

    }

    public void addLocation(Location c){
        //mCrimes.add(c);
        ContentValues values = getContentValues(c);
        mDatabase.insert(LocationTable.NAME,null,values);

    }
    public List<Location> getCrime(){
        //return mCrimes;
        List<Location> locations = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                locations.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally{
            cursor.close();
        }
        return locations;
    }
    public Location getLocation(UUID id){
        //for (Location crime:mCrimes){
            //if(crime.getId().equals(id)){
              //  return crime;
            //}
        //}
        CrimeCursorWrapper cursor = queryCrimes(
                LocationTable.Cols.UUID + "=?",
                new String[]{id.toString()}
        );
        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public File getPhotoFile (Location location){
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null){
            return null;
        }
        return new File(externalFilesDir, location.getPhotoFilename());
    }

    public void updateLocation(Location location){
        String uuidString = location.getId().toString();
        ContentValues values = getContentValues(location);
        mDatabase.update(LocationTable.NAME,values,LocationTable.Cols.UUID+"=?",
                new String[] {uuidString});}

    private static ContentValues getContentValues(Location location){
        ContentValues values = new ContentValues();
        values.put(LocationTable.Cols.UUID, location.getId().toString());
        values.put(LocationTable.Cols.DESCRIPTION, location.getDescription());
        //values.put(LocationTable.Cols.DATE,location.getDate().getTime());
        //values.put(LocationTable.Cols.SOLVED,location.isSolve()?1:0);
        values.put(LocationTable.Cols.LOCATION, location.getLocation());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                LocationTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
}
