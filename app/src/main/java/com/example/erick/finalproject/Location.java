package com.example.erick.finalproject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Erick on 9/15/2016.
 */
public class Location {
    private UUID mId;
    private String mDescription;
    //private Date mDate;
    //private boolean mSolve;
    private String mLocation;



    public Location(){
        this(UUID.randomUUID());
        //mId = UUID.randomUUID();
        //mDate = new Date();
    }
    public Location(UUID id){
        mId = id;
        //mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String descrip) {
        mDescription = descrip;
    }

    /*public boolean isSolve() {
        return mSolve;
    }

    public void setSolve(boolean mSolve) {
        this.mSolve = mSolve;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }*/

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getPhotoFilename(){
        return "IMG_"+ getId().toString()+".jpg";
    }
}
