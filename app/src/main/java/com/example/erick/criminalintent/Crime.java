package com.example.erick.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Erick on 9/15/2016.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolve;
    private String mSuspect;



    public Crime(){
        this(UUID.randomUUID());
        //mId = UUID.randomUUID();
        //mDate = new Date();
    }
    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolve() {
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
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public String getPhotoFilename(){
        return "IMG_"+ getId().toString()+".jpg";
    }
}
