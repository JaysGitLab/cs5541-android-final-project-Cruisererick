package com.example.erick.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by eric on 9/27/2016.
 */

public class CrimePaperActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_ID =
            "com.example.erick.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext,UUID crimeId){
       Intent intent = new Intent(packageContext,CrimePaperActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }
@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crime_paper);
    UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
    mViewPager = (ViewPager) findViewById(R.id.activity_crime_paper_view_paper);
    mCrimes = CrimeLab.get(this).getCrime();
    FragmentManager fragmentManager = getSupportFragmentManager();
    mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
        @Override
        public Fragment getItem(int position) {
            Crime crime = mCrimes.get(position);
            return CrimeFragment.newInstance(crime.getId());

        }

        @Override
        public int getCount() {
            return mCrimes.size();
        }
    });
    for (int i = 0; i< mCrimes.size();i++){
        if (mCrimes.get(i).getId().equals(crimeId)){
            mViewPager.setCurrentItem(i);
            break;
        }
    }

}

}

