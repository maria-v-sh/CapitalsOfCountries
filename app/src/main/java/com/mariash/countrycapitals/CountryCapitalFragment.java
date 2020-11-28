package com.mariash.countrycapitals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import java.util.Random;

public class CountryCapitalFragment extends Fragment {

    private static final String KEY_QUESTION = "questionCountry";
    private static final String KEY_CAPITALS_ON_BUTTONS = "capitalsOnButtons";
    private static final String EXTRA_TWO_PANEL = "twoPanel";

    private static final String DIALOG_FLAG = "DialogFlag";
    private static final String DIALOG_HINT = "DialogHint";

    private static final int REQUEST_SHOW_HINT = 0;
    private final int NUM_OF_BUTTONS = 8;

    private QuestionCountry mQuestionCountry;
    private TextView mQuestionText;
    private ImageButton mFlagButton;
    private Button mMapButton;
    private TextView mOptionsText;
    private Button[] mButtons = new Button[NUM_OF_BUTTONS];
    private String[] mCapitalsOnButton = new String[NUM_OF_BUTTONS];
    private int mFlagId;
    private Callbacks mCallbacks;
    private ImageView mSwipeRight;
    private ImageView mSwipeLeft;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = (int) getArguments().getInt(KEY_QUESTION, 0);
        mQuestionCountry = QuestionsCountryLab.get(getActivity()).getQuestions().get(id);
        setHasOptionsMenu(true);
        updateSubtitleHint();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_country_capital, container, false);
        mQuestionText = (TextView) view.findViewById(R.id.question_view);
        mQuestionText.setText(String.format(getString(R.string.question_text), mQuestionCountry.getCountry()));
        mFlagButton = (ImageButton) view.findViewById(R.id.flag_button);
        mMapButton = (Button) view.findViewById(R.id.map_button);

        //Launch map page
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapsActivity.newIntent(getActivity(), mQuestionCountry.getLatitude(),
                        mQuestionCountry.getLongitude(), mQuestionCountry.getCapital());
                startActivity(intent);
            }
        });
        mMapButton.setClickable(false);
        mMapButton.setEnabled(false);
        mOptionsText = (TextView) view.findViewById(R.id.choice);

        mButtons[0] = (Button) view.findViewById(R.id.capital_button1);
        mButtons[1] = (Button) view.findViewById(R.id.capital_button2);
        mButtons[2] = (Button) view.findViewById(R.id.capital_button3);
        mButtons[3] = (Button) view.findViewById(R.id.capital_button4);
        mButtons[4] = (Button) view.findViewById(R.id.capital_button5);
        mButtons[5] = (Button) view.findViewById(R.id.capital_button6);
        mButtons[6] = (Button) view.findViewById(R.id.capital_button7);
        mButtons[7] = (Button) view.findViewById(R.id.capital_button8);

        mSwipeRight = (ImageView) view.findViewById(R.id.swipe_right);
        mSwipeLeft = (ImageView) view.findViewById(R.id.swipe_left);

        int position = (int) getArguments().getInt(KEY_QUESTION);

        //Arrows for swipe only appear if using one panel(phone)
        boolean twoPanel = (boolean) getArguments().getBoolean(EXTRA_TWO_PANEL, false);
        if(twoPanel) {
            mSwipeLeft.setVisibility(View.INVISIBLE);
            mSwipeRight.setVisibility(View.INVISIBLE);
        } else {
            if (position == 0) {
                mSwipeLeft.setVisibility(View.INVISIBLE);
            } else if (position == QuestionsCountryLab.get(getActivity()).getQuestions().size() - 1) {
                mSwipeRight.setVisibility(View.INVISIBLE);
            }
        }

        //If button names have been saved, set them again
        // else set new names
        if(savedInstanceState != null) {
            mCapitalsOnButton = savedInstanceState.getStringArray(KEY_CAPITALS_ON_BUTTONS);
            for(int i = 0; i < NUM_OF_BUTTONS; i++) {
                mButtons[i].setText(mCapitalsOnButton[i]);
            }
        } else {
            setButtonsNewCapitals();
        }

        mButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(0);
            }
        });
        mButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(1);
            }
        });
        mButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(2);
            }
        });
        mButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(3);
            }
        });
        mButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(4);
            }
        });
        mButtons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(5);
            }
        });
        mButtons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(6);
            }
        });
        mButtons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserChoiceCountry(7);
            }
        });

        if(mQuestionCountry.isSolved()) {
            setTrueAnswerIsChosen();
        }

        //Find the flag in resources(drawable) and updat it
        mFlagId = getResources().getIdentifier("id" + mQuestionCountry.getId(), "drawable", "com.mariash.countrycapitals");
        updateFlag();

        mFlagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FlagPickerFragment dialog = FlagPickerFragment.newInstance(mFlagId, mQuestionCountry.getCountry());
                dialog.show(manager, DIALOG_FLAG);
            }
        });
        updateSubtitleHint();
        return view;
    }

    public static CountryCapitalFragment newInstance(int indexQuestionCountry, boolean twoPanel) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_QUESTION, indexQuestionCountry);
        bundle.putBoolean(EXTRA_TWO_PANEL, twoPanel);
        CountryCapitalFragment fragment = new CountryCapitalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    //Update the number of hints that the user can use
    private void updateSubtitleHint() {
        int numOfHints = Hints.get(getActivity()).getNumOfHints();
        String subtitle = getResources().getString(R.string.num_of_hints, numOfHints);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //Set buttons names
    private void setButtonsNewCapitals() {

        Random generatorButton = new Random();
        Random generatorCapitals = new Random();

        //Select the button for the correct answer and set the answer
        int trueButton = generatorButton.nextInt(NUM_OF_BUTTONS);
        String trueCapital = mQuestionCountry.getCapital();
        mButtons[trueButton].setText(trueCapital);
        mCapitalsOnButton[trueButton] = trueCapital;
        String[] capitals = getResources().getStringArray(R.array.capitals);
        boolean[] isUsed = new boolean[capitals.length];

        //set incorrect answers to other buttons
        for(int i = 0; i < NUM_OF_BUTTONS; i++) {
            if (i != trueButton) {
                int indexOfCapital = generatorCapitals.nextInt(capitals.length);
                while (capitals[indexOfCapital].equals(trueCapital) || isUsed[indexOfCapital]) {
                    indexOfCapital = generatorCapitals.nextInt(capitals.length);
                }
                mButtons[i].setText(capitals[indexOfCapital]);
                mCapitalsOnButton[i] = capitals[indexOfCapital];
                isUsed[indexOfCapital] = true;
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_country_capital, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hints:
                FragmentManager manager = getFragmentManager();
                int numOfHints = Hints.get(getActivity()).getNumOfHints();
                HintPickerFragment dialog = HintPickerFragment
                        .newInstance(mQuestionCountry.getCapital(), numOfHints);
                dialog.setTargetFragment(this, REQUEST_SHOW_HINT);
                dialog.show(manager, DIALOG_HINT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Save button names
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(KEY_CAPITALS_ON_BUTTONS, mCapitalsOnButton);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(data == null) {
            return;
        }

        //If the hint was used, decrease number of hints
        if(requestCode == REQUEST_SHOW_HINT) {
            if(data.getBooleanExtra(HintPickerFragment.EXTRA_ANSWER_SHOWN, false)) {
                Hints hints = Hints.get(getActivity());
                hints.numDecrease(getActivity());
                updateSubtitleHint();
            }
        }
    }

    public void updateFlag() {
        Bitmap bitmap = PictureUtils.getScaledBitmap(getResources(), mFlagId, getActivity());
        mFlagButton.setImageBitmap(bitmap);
    }

    //Determine if the answer was correct
    private void checkUserChoiceCountry(int buttonNumber) {

        if(mCapitalsOnButton[buttonNumber].equals(mQuestionCountry.getCapital())) {
            String str = getString(R.string.correct_answer) + String.format(getString(R.string.hint), mQuestionCountry.getCapital());
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            Score.get(getActivity()).increaseNumOfRightAnswer(getActivity());
            setTrueAnswerIsChosen();

            //update dataBase if correct answer was given
//            QuestionsCountryLab.get(getActivity()).updateQuestionCountry(mQuestionCountry);
            updateCountryCapital();
        } else {
            Toast.makeText(getActivity(), R.string.incorrect_answer, Toast.LENGTH_SHORT).show();
        }
    }

    //if the answer was correct
    private void setTrueAnswerIsChosen() {
        mQuestionCountry.setSolved(true);
        mMapButton.setEnabled(true);
        mMapButton.setClickable(true);
        for(int i = 0; i < NUM_OF_BUTTONS; i++) {
            mButtons[i].setClickable(false);
            mButtons[i].setEnabled(false);
            if(mCapitalsOnButton[i].equals(mQuestionCountry.getCapital())) {
                mButtons[i].setBackground(getResources().getDrawable(R.drawable.button_true, null));
            }
        }
    }

    //find out whether one(phone) or two panels(tablet) are used
    public interface Callbacks {
        void onCountryCapitalUpdated(QuestionCountry questionCountry);
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

    //updated if correct answer was given
    private void updateCountryCapital() {
        QuestionsCountryLab.get(getActivity()).updateQuestionCountry(mQuestionCountry);
        mCallbacks.onCountryCapitalUpdated(mQuestionCountry);
    }
}
