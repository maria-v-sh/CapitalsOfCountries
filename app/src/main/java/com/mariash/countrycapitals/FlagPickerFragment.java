package com.mariash.countrycapitals;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

//The class contains everything it need to create a dialog with flag image
public class FlagPickerFragment extends DialogFragment {

    private static final String KEY_COUNTRY = "country";
    private static final String FLAG_ID = "capital";

    private ImageView mFlagView;

    public static FlagPickerFragment newInstance(int flagId, String country) {
        Bundle args = new Bundle();
        args.putInt(FLAG_ID, flagId);
        args.putString(KEY_COUNTRY, country);

        FlagPickerFragment fragment = new FlagPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int flagId = (int) getArguments().getInt(FLAG_ID, 1);
        final String countryName = (String) getArguments().getString(KEY_COUNTRY, "");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_flag, null);
        mFlagView = (ImageView) v.findViewById(R.id.flag_view);

        Bitmap bitmap = PictureUtils.getScaledBitmap(getResources(), flagId, getActivity());
        mFlagView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNeutralButton(R.string.return_button, null)
                .setTitle(String.format(getString(R.string.flag_dialog), countryName))
                .create();
    }
}
