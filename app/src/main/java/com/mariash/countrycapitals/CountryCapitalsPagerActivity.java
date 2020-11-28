package com.mariash.countrycapitals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

//The Pager class contain list QuestionCountry and ViewPager
public class CountryCapitalsPagerActivity extends AppCompatActivity implements CountryCapitalFragment.Callbacks {

    private static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;
    private static final String EXTRA_QUESTION_COUNTRY_ID = "com.mariash.countrycapitals.question_country_id";
    private static final String EXTRA_PAGE_ID = "com.mariash.countrycapitals.page_id";


    private ViewPager mViewPager;
    private List<QuestionCountry> mQuestionCountries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_country_capitals);

        mViewPager = (ViewPager) findViewById(R.id.pager_country_capitals);
        mQuestionCountries = QuestionsCountryLab.get(this).getQuestions();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return CountryCapitalFragment.newInstance(position, false);
            }

            @Override
            public int getCount() {
                return mQuestionCountries.size();
            }
        });

        //Launch CountryCapitalFragment with this id
        int id = (int) getIntent().getIntExtra(EXTRA_QUESTION_COUNTRY_ID, 0);
        mViewPager.setCurrentItem(id);
    }

    //Pass "index" to Scroll RecyclerView to "index" item
    private void setToWhichPageToScrollTo(int index) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PAGE_ID, index);
        setResult(Activity.RESULT_OK, intent);
    }

    //Id is needed to scroll the Recycler to this Page
    //when returning from the CountryCapitalsPagerActivity class
    public static int pagerId(Intent intent) {
        return intent.getIntExtra(EXTRA_PAGE_ID, 0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int index = mViewPager.getCurrentItem();
                setToWhichPageToScrollTo(index);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //if press BackButton also set which page to scroll to
    @Override
    public void onBackPressed() {
        int index = mViewPager.getCurrentItem();
        setToWhichPageToScrollTo(index);
        finish();
    }

    public static Intent newIntent(Context packageContext, int index) {
        Intent intent = new Intent(packageContext, CountryCapitalsPagerActivity.class);
        intent.putExtra(EXTRA_QUESTION_COUNTRY_ID, index);
        return intent;
    }

    @Override
    public void onCountryCapitalUpdated(QuestionCountry questionCountry) {
    }
}
