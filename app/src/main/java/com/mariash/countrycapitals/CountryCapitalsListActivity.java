package com.mariash.countrycapitals;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

public class CountryCapitalsListActivity extends AppCompatActivity implements CountryCapitalsListFragment.Callbacks, CountryCapitalFragment.Callbacks {

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if(f == null) {
            f = new CountryCapitalsListFragment();
            fm.beginTransaction().add(R.id.fragment_container, f).commit();
        }
    }

    //find out whether one(phone) or two panels(tablet) are used
    @Override
    public boolean isTwoPanel(int position) {
        if(findViewById(R.id.second_panel) == null) {
            return false;
        } else {
            Fragment newDetail = CountryCapitalFragment.newInstance(position, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.second_panel, newDetail)
                    .commit();
        }
        return true;
    }

    @Override
    public void onCountryCapitalUpdated(QuestionCountry questionCountry) {
        CountryCapitalsListFragment listFragment = (CountryCapitalsListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateSubtitleRightAnswer();
    }
}