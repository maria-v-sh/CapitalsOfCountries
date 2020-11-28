package com.mariash.countrycapitals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

//The class contains everything it need to create a dialog with hint
public class HintPickerFragment extends DialogFragment {

    private static final String KEY_ANSWER_IS_SHOWN = "answerIsShown";
    private static final String KEY_CAPITAL = "capital";
    private static final String KEY_NUM_HINTS = "numOfHints";

    public static final String EXTRA_ANSWER_SHOWN = "AnswerShown";

    private TextView mQuestion;
    private Button mYesButton;
    private TextView mAnswer;
    private boolean mAnswerShown = false;


    public static HintPickerFragment newInstance(String capital, int numOfHints){
        Bundle args = new Bundle();
        args.putString(KEY_CAPITAL, capital);
        args.putInt(KEY_NUM_HINTS, numOfHints);

        HintPickerFragment fragment = new HintPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        final String capital = (String) getArguments().getString(KEY_CAPITAL, "ERRCapital");
        int numOfHints = (int) getArguments().getInt(KEY_NUM_HINTS, 20);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_hint,null);
        mQuestion = (TextView) view.findViewById(R.id.hint_text_view);
        mYesButton = (Button) view.findViewById(R.id.yes_button);
        mAnswer = (TextView) view.findViewById(R.id.answer);
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnswer.setText(String.format(getString(R.string.hint),capital));
                mAnswerShown = true;
            }
        });

        mYesButton.setEnabled(numOfHints > 0);
        mYesButton.setClickable(numOfHints > 0);
        if(savedInstanceState != null) {
            mAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_IS_SHOWN);
        }
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.return_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(getTargetFragment() == null){
                            return;
                        }
                        Intent intent = new Intent();
                        //If the user used the hint, decreas the number of hints by one in the CountryCapitalFragment class.
                        intent.putExtra(EXTRA_ANSWER_SHOWN, mAnswerShown);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setView(view)
                .setTitle(R.string.help)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ANSWER_IS_SHOWN, mAnswerShown);
    }
}
