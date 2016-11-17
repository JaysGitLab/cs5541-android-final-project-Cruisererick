package com.example.erick.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.erick.criminalintent.Crime;
import com.example.erick.criminalintent.database.CrimeDBSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eric on 10/6/2016.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Crime getCrime(){

        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getInt(getColumnIndex(CrimeTable.Cols.SOLVED)));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolve(isSolved!=0);
        crime.setSuspect(suspect);

        return crime;
    }
}
