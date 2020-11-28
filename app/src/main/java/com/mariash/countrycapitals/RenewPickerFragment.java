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

//The class contains everything it need to create a dialog with hint
public class RenewPickerFragment extends DialogFragment {

    public static final String EXTRA_PUSHED_YES = "IsPushedYes";

    private TextView mQuestion;
//    private Button mYesButton;
    private boolean mIsPushedYes;

    public static RenewPickerFragment newInstance(){
        Bundle args = new Bundle();
        RenewPickerFragment fragment = new RenewPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_renew,null);
        mQuestion = (TextView) view.findViewById(R.id.renew_text_view);
//        mYesButton = (Button) view.findViewById(R.id.yes_button_renew);
//        mYesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mIsPushedYes = true;
//            }
//        });

        return new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(getTargetFragment() == null){
                            return;
                        }
                        mIsPushedYes = true;
                        Intent intent = new Intent();
                        //If the user push "YES", renew all singletons
                        intent.putExtra(EXTRA_PUSHED_YES, mIsPushedYes);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    }
                })
                .setTitle(R.string.renew_title)
                .setNeutralButton(R.string.no_button, null)
                .setView(view)
                .create();
    }
}
