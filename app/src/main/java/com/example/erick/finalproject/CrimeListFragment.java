package com.example.erick.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Erick on 9/20/2016.
 */
public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
     View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(
                R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }

    }
    private void updateUI(){
        LocationLab locationLab = LocationLab.get(getActivity());
        List<Location> locations = locationLab.getCrime();
        if(mAdapter==null) {
            mAdapter = new CrimeAdapter(locations);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setCrimes(locations);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Location mLocation;

            public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);

        }
        @Override
        public void onClick(View v){
            //Toast.makeText(getActivity(),mLocation.getTitle()+"Cliked!",Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getActivity(),CrimeActivity.class);
            //Intent intent = CrimeActivity.newIntent(getActivity(),mLocation.getId());
            Intent intent = CrimePaperActivity.newIntent(getActivity(), mLocation.getId());
            startActivity(intent);}

        public void bindLocation(Location location){
            mLocation = location;
            mTitleTextView.setText(mLocation.getDescription());
            //mDateTextView.setText(mLocation.getDate().toString());
           // mSolvedCheckBox.setChecked(mLocation.isSolve());


        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Location> mLocations;

        public CrimeAdapter(List<Location> locations){
            mLocations = locations;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position){
            Location location = mLocations.get(position);
            holder.bindLocation(location);

        }

        @Override
        public int getItemCount(){

            return mLocations.size();
        }

        public void setCrimes(List<Location> locations){
            mLocations = locations;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Location location = new Location();
                LocationLab.get(getActivity()).addLocation(location);
                Intent intent = CrimePaperActivity.newIntent(getActivity(), location.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateSubtitle(){
        LocationLab locationLab = LocationLab.get(getActivity());
        int crimeCount = locationLab.getCrime().size();
        String subtitle = getString(R.string.subtitle_format, Integer.toString(crimeCount));

        if(!mSubtitleVisible){
            subtitle= null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
