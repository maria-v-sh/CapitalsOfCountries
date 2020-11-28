package com.mariash.countrycapitals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// The class contain RecyclerView and RecyclerView.Adapter
public class CountryCapitalsListFragment extends Fragment {

    public static final int REQUEST_INDEX = 0;
    private static final String KEY_NUM_RIGHT_ANSWER = "NumRightAnswer";
    private static final String DIALOG_RENEW = "DialogRenew";
    private static final int REQUEST_RENEW = 1;

    private RecyclerView mCountryRecyclerView;
    private CountryAdapter mAdapter;
    private Callbacks mCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_country_capital, container, false);
        mCountryRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_countries);
        mCountryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        QuestionsCountryLab listQuestionsCountry = setFromResources();

        mAdapter = new CountryAdapter(listQuestionsCountry.getQuestions());
        mCountryRecyclerView.setAdapter(mAdapter);
        if(savedInstanceState != null) {
            savedInstanceState.getInt(KEY_NUM_RIGHT_ANSWER, 0);
        }
        updateSubtitleRightAnswer();
        return view;
    }

    //Create a list in the QuestionsCountryLab singleton using string resources
    public QuestionsCountryLab setFromResources() {

        String[] arrayCountries = getResources().getStringArray(R.array.countries);
        String[] arrayCapitals = getResources().getStringArray(R.array.capitals);
        String[] arrayStringLatitude = getResources().getStringArray(R.array.latitude);
        String[] arrayStringLongitude = getResources().getStringArray(R.array.longitude);
        double[] arrayDoubleLatitude = new double[arrayStringLatitude.length];
        double[] arrayDoubleLongitude = new double[arrayStringLongitude.length];
        for(int i = 0; i < arrayDoubleLatitude.length; i++) {
            arrayDoubleLatitude[i] = Double.parseDouble(arrayStringLatitude[i]);
            arrayDoubleLongitude[i] = Double.parseDouble(arrayStringLongitude[i]);
        }

        QuestionsCountryLab listQuestionsCountry = QuestionsCountryLab.get(getActivity());
        listQuestionsCountry.setCountriesCapitalsLatitudeLongitude(arrayCountries, arrayCapitals,
                arrayDoubleLatitude, arrayDoubleLongitude);
        listQuestionsCountry.createQuestionCountry();
        return listQuestionsCountry;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(data == null) {
            return;
        }
        if(requestCode == REQUEST_INDEX) {

            //Id is needed to scroll the Recycler to this Page
            //when returning from the CountryCapitalsPagerActivity class
            int index = CountryCapitalsPagerActivity.pagerId(data);
            mCountryRecyclerView.getLayoutManager().scrollToPosition(index);
        }
        if(requestCode == REQUEST_RENEW) {
            if(data.getBooleanExtra(RenewPickerFragment.EXTRA_PUSHED_YES, false)) {
                QuestionsCountryLab.get(getActivity()).renew();
                Score.get(getActivity()).renew(getActivity());
                Hints.get(getActivity()).renew(getActivity());
                updateSubtitleRightAnswer();
            }
        }

        mAdapter.notifyDataSetChanged();
        updateSubtitleRightAnswer();
    }

    //Update the number of correctly answered questions
    public void updateSubtitleRightAnswer() {
        List<QuestionCountry> qc = QuestionsCountryLab.get(getActivity()).getQuestions();
        int totalNumOfQuestions = qc.size();
        int numRightAnswer = Score.get(getActivity()).getNumOfRightAnswer();
        String subtitle = getResources().getString(R.string.score, numRightAnswer, totalNumOfQuestions);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
        if(mAdapter == null) {
            mAdapter = new CountryAdapter(qc);
            mCountryRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCountries(qc);
            mAdapter.notifyDataSetChanged();
        }
    }

    //Create menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_country_capital, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                FragmentManager manager = getFragmentManager();
                RenewPickerFragment dialog = RenewPickerFragment.newInstance();
                dialog.setTargetFragment(this, REQUEST_RENEW);
                dialog.show(manager, DIALOG_RENEW);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mCountryName;
        private CheckBox mIsAnswered;
        private QuestionCountry mQuestionCountry;

        //Position is needed to scroll the Recycler to this position
        //when returning from the CountryCapitalsPagerActivity class
        private int mPosition;

        public CountryViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_country, parent, false));

            itemView.setOnClickListener(this);
            mCountryName = (TextView) itemView.findViewById(R.id.item_country);
            mIsAnswered = (CheckBox) itemView.findViewById(R.id.checkbox);
        }

        //display item
        public void bind(QuestionCountry questionCountry) {
            mCountryName.setText(questionCountry.getCountry());
            mQuestionCountry = questionCountry;
            mIsAnswered.setChecked(mQuestionCountry.isSolved());
        }

        @Override
        public void onClick(View view) {
            if(!mCallbacks.isTwoPanel(mPosition)){
                Intent intent = CountryCapitalsPagerActivity.newIntent(getActivity(), mPosition);
                startActivityForResult(intent, REQUEST_INDEX);
            }
        }

        public void setPosition(int i) {
            mPosition = i;
        }
    }

    //The class CountryAdapter manages ViewHolders
    class CountryAdapter extends RecyclerView.Adapter<CountryViewHolder> {
        private List<QuestionCountry> mCountries;

        public CountryAdapter(List<QuestionCountry> countries) {
            mCountries = countries;
        }

        @Override
        public CountryViewHolder onCreateViewHolder(ViewGroup container, int t) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CountryViewHolder(inflater, container);
        }

        @Override
        public void onBindViewHolder(CountryViewHolder holder, int position) {
            QuestionCountry question = mCountries.get(position);
            holder.bind(question);
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return mCountries.size();
        }

        public void setCountries(List<QuestionCountry> countries) {
            mCountries = countries;
        }
    }

    //find out whether one or two panels are used
    public interface Callbacks {
        boolean isTwoPanel(int position);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
