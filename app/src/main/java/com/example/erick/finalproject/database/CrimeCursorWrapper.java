package com.example.erick.finalproject.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.erick.finalproject.Location;
import com.example.erick.finalproject.database.LocationDBSchema.LocationTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eric on 10/6/2016.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Location getCrime(){

        String uuidString = getString(getColumnIndex(LocationTable.Cols.UUID));
        String description = getString(getColumnIndex(LocationTable.Cols.DESCRIPTION));
        //long date = getLong(getColumnIndex(LocationTable.Cols.DATE));
        //int isSolved = getInt(getInt(getColumnIndex(LocationTable.Cols.SOLVED)));
        String currentlocation = getString(getColumnIndex(LocationTable.Cols.LOCATION));

        Location location = new Location(UUID.fromString(uuidString));
        location.setDescription(description);
        //location.setDate(new Date(date));
        //location.setSolve(isSolved!=0);
        location.setLocation(currentlocation);

        return location;
    }
}
