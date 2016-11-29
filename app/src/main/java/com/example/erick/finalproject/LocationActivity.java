package com.example.erick.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.UUID;

public class LocationActivity extends SingleFragmentActivity {

    private static final String EXTRA_LOCATION_ID = "com.example.erick.finalproject.location_id";
    private static final int REQUESt_ERROR = 0;
    public static Intent newIntent(Context packageContext, UUID locationId){
        Intent intent = new Intent(packageContext,LocationActivity.class);
        intent.putExtra(EXTRA_LOCATION_ID,locationId);
        return intent;
    }
    @Override
    protected Fragment createFragment(){
        //return new LocationFragment();
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_LOCATION_ID);
        return LocationFragment.newInstance(crimeId);
    }

    @Override
    protected void onResume(){
        super.onResume();
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS){
            Dialog errorDialog = GooglePlayServicesUtil
                    .getErrorDialog(errorCode,this,REQUESt_ERROR,
                            new DialogInterface.OnCancelListener()
                            {
                              @Override
                                public void onCancel(DialogInterface dialog){
                                  finish();
                              }
                            });
            errorDialog.show();
        }
    }
}
