package com.example.erick.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.UUID;

/**
 * Created by Erick on 9/15/2016.
 */
public class LocationFragment extends android.support.v4.app.Fragment {

    private static final String ARG_LOCATION_ID = "location_id";
    private static final int REQUEST_PHOTO = 2;
    private Location mLocation;
    private EditText mTitlefield;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;


    private GoogleMap googleMap;
    private  GoogleApiClient mClient;
    private static final String TAG = "LocationFragment";
    private android.location.Location mCurrentLocation;
    boolean isImageFitToScreen;
    private Bitmap bitmap;



    public static LocationFragment newInstance(UUID LocationId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID,LocationId);

        LocationFragment fragment = new LocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        setHasOptionsMenu(true);
        UUID LocationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationLab.get(getActivity()).getLocation(LocationId);
        mPhotoFile = LocationLab.get(getActivity()).getPhotoFile(mLocation);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks(){

                    @Override
                    public void onConnected(Bundle bundle){
                        if(mCurrentLocation==null)
                        getloca();


                    }

                    @Override
                    public void onConnectionSuspended(int i){

                    }

            })
                .build();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.frasgment_location_delete,menu);
    }

    @Override
    public void onStart(){
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onPause(){
        if(bitmap!=null){
        bitmap.recycle();
        bitmap = null;}
        LocationLab.get(getActivity()).updateLocation(mLocation);
        mClient.disconnect();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
    ,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_location,container,false);


        mTitlefield = (EditText)v.findViewById(R.id.crime_title);
        mTitlefield.setText(mLocation.getDescription());
        mTitlefield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            mLocation.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        mReportButton = (Button) v.findViewById(R.id.location_report);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/jpg");
                String[] auxlo = mLocation.getLocation().split(",");
                double longitude = Double.parseDouble(auxlo[0]);
                double latitude = Double.parseDouble(auxlo[1]);
                String uri = mLocation.getDescription()+"  "+"http://maps.google.com/maps?saddr=" +latitude+","+longitude;
                StringBuffer smsBody = new StringBuffer();
                smsBody.append(Uri.parse(uri));
                i.putExtra(Intent.EXTRA_TEXT, smsBody.toString());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.location_report_suspect));
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse(mPhotoFile.getPath()));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);

            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camara);
        final Intent captureImage = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFitToScreen && mCurrentLocation!=null) {
                    isImageFitToScreen=false;
                    mPhotoView.setLayoutParams(new LinearLayout.LayoutParams(270,270));
                    mPhotoView.setAdjustViewBounds(true);
                }else{
                    if(mCurrentLocation!=null){
                    isImageFitToScreen=true;
                    mPhotoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    mPhotoView.setAdjustViewBounds(true);}
            }
            }
        });

        updatePhotoView();

        googleMap =((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        return v;
    }

    private void getloca(){
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(android.location.Location location) {
                        Log.i(TAG,"Got a fix: "+ location);
                        new SearchTask().execute(location);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }

    private void updatePhotoView(){
      if (mPhotoFile == null || !mPhotoFile.exists()){
        mPhotoView.setImageDrawable(null);
      }else{
          Bitmap bitmap = PictureUtils.getScaleBitmap(mPhotoFile.getPath(),getActivity());
          mPhotoView.setImageBitmap(bitmap);
      }

    }

    private void zoomin(android.location.Location location){

        LatLng mypoint  = null;
        LatLng mypoint1 = null ;
        LatLng mypoint2 = null ;
        LatLng mypoint3 = null;
        if (googleMap == null){
            return;
        }
        if(mCurrentLocation == null){
            return;
        }else
        {
            location = mCurrentLocation;
            if(location!=null){
                 mypoint = new LatLng(location.getLatitude(),location.getLongitude());
                 mypoint1 = new LatLng(location.getLatitude()+0.001,location.getLongitude()+0.001);
                 mypoint2 = new LatLng(location.getLatitude()-0.001,location.getLongitude()-0.001);}
        }



        if (mLocation.getLocation()!= null){
            String[] auxlo =  mLocation.getLocation().split(",");
            double longitude = Double.parseDouble(auxlo[0]);
            double latitude = Double.parseDouble(auxlo[1]);
            LatLng auxlocation = new LatLng(latitude, longitude);;
            mypoint3 = new LatLng(auxlocation.latitude,auxlocation.longitude);

        }


if (mypoint3.toString().compareTo(mypoint.toString())==0) {
    MarkerOptions myMarker = new MarkerOptions().position(mypoint);
    googleMap.clear();
    googleMap.addMarker(new MarkerOptions()
            .position(mypoint)
            .title("Car")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    LatLngBounds bounds = new LatLngBounds.Builder()
            .include(mypoint)
            .include(mypoint1)
            .include(mypoint2)
            .build();
    int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
    CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
    googleMap.animateCamera(update);
}else{
    MarkerOptions myMarker = new MarkerOptions().position(mypoint);
    MarkerOptions secondMarker = new MarkerOptions().position(mypoint3);
    googleMap.clear();
    googleMap.addMarker(new MarkerOptions()
            .position(mypoint)
            .title("User location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    googleMap.addMarker(new MarkerOptions()
            .position(mypoint3)
            .title("Car")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    LatLngBounds bounds = new LatLngBounds.Builder()
            .include(mypoint)
            .include(mypoint3)
            .build();
    int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
    CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
    googleMap.animateCamera(update);
}

    }

    private class SearchTask extends AsyncTask<android.location.Location,Void,Void>{

        private android.location.Location mauxLocation;
        @Override
        protected Void doInBackground(android.location.Location... params){

            mauxLocation = params[0];
          return null;
        }

        @Override
        protected void onPostExecute(Void result){
            mCurrentLocation = mauxLocation;
            if(mLocation.getLocation()==null)
            mLocation.setLocation(mCurrentLocation.getLongitude()+","+ mCurrentLocation.getLatitude());
            zoomin(mCurrentLocation);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_delete_location:
                LocationLab.get(getActivity()).deleteLocation(mLocation);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
