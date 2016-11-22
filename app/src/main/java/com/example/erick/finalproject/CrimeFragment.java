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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.UUID;

/**
 * Created by Erick on 9/15/2016.
 */
public class CrimeFragment extends android.support.v4.app.Fragment {

    private static final String ARG_LOCATION_ID = "location_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    private Location mLocation;
    private EditText mTitlefield;
    private Button mDateButton;
    private CheckBox mSolvedcheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;

    private ImageView mImageView;
    private GoogleMap googleMap;
    private  GoogleApiClient mClient;
    private static final String TAG = "CrimeFragment";
    private android.location.Location mCurrentLocation;


    public static CrimeFragment newInstance(UUID LocationId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION_ID,LocationId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedIntenceState){
        super.onCreate(savedIntenceState);
        //mLocation = new Location();
        //UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra((CrimeActivity.EXTRA_CRIME_ID));
        UUID LocationId = (UUID) getArguments().getSerializable(ARG_LOCATION_ID);
        mLocation = LocationLab.get(getActivity()).getLocation(LocationId);
        mPhotoFile = LocationLab.get(getActivity()).getPhotoFile(mLocation);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks(){

                    @Override
                    public void onConnected(Bundle bundle){
                        if (mLocation.getLocation()==null)
                        getloca();

                    }

                    @Override
                    public void onConnectionSuspended(int i){

                    }

            })
                .build();
    }

    @Override
    public void onStart(){
        super.onStart();
        //getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onPause(){
        super.onPause();
        LocationLab.get(getActivity()).updateLocation(mLocation);
        mClient.disconnect();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
    ,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime,container,false);


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
        //mDateButton = (Button)v.findViewById(R.id.crime_date);
        //updateDate();
        //mDateButton.setEnabled(false);
        /*mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                //DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInnstance(mLocation.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });*/
        //mSolvedcheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        //mSolvedcheckBox.setChecked(mLocation.isSolve());
       /* mSolvedcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
              // Set the crime solved propoety
              mLocation.setSolve(isChecked);
            }

        });*/
        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_suspect));
                i = Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);

            }
        });

        final Intent pickContact = new Intent (Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        /*mSuspectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });*/
        /*if(mLocation.getSuspect()!= null){
            mSuspectButton.setText(mLocation.getSuspect());
        }*/

        PackageManager packageManager = getActivity().getPackageManager();
        /*if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }*/
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

    /*private void updateDate() {
        mDateButton.setText(mLocation.getDate().toString());
    }*/

    private String getCrimeReport(){
        String solvedString = null;

        /*if(mLocation.isSolve()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE,MMM dd";
        String dateString = DateFormat.format(dateFormat,mLocation.getDate()).toString();

        String suspect = mLocation.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect,suspect);
        }
        */
        String report = getString(R.string.crime_report, mLocation.getDescription(),null,solvedString,null);
        return report;
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

        if (googleMap == null){
            return;
        }
        if(mCurrentLocation == null){
            return;
        }else
        {
            location = mCurrentLocation;
        }

        if (mLocation.getLocation()!= null){
            //mCurrentLocation = (Location) mLocation.getLocation();
        }

        LatLng mypoint = new LatLng(location.getLatitude(),location.getLongitude());
        LatLng mypoint1 = new LatLng(location.getLatitude()+0.001,location.getLongitude()+0.001);
        LatLng mypoint2 = new LatLng(location.getLatitude()-0.001,location.getLongitude()-0.001);

        MarkerOptions myMarker = new MarkerOptions().position(mypoint);
        googleMap.clear();
        googleMap.addMarker(myMarker);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(mypoint)
                .include(mypoint1)
                .include(mypoint2)
                .build();
        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,margin);
        googleMap.animateCamera(update);


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
            mLocation.setLocation(mCurrentLocation.toString());
            zoomin(mCurrentLocation);
        }
    }

}
