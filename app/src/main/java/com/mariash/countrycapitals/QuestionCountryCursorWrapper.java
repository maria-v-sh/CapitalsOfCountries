package com.mariash.countrycapitals;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mariash.countrycapitals.database.QuestionCountryDBSchema;

import static com.mariash.countrycapitals.database.QuestionCountryDBSchema.*;

public class QuestionCountryCursorWrapper extends CursorWrapper {

    public QuestionCountryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public QuestionCountry getQuestionCountry() {
        String idString = getString(getColumnIndex(QuestionTable.Cols.ID));
        String country = getString(getColumnIndex(QuestionTable.Cols.COUNTRY));
        String capital = getString(getColumnIndex(QuestionTable.Cols.CAPITAL));
        double latitude = Double.valueOf(getString(getColumnIndex(QuestionTable.Cols.LATITUDE)));
        double longitude = Double.valueOf(getString(getColumnIndex(QuestionTable.Cols.LONGITUDE)));
        int solved = getInt(getColumnIndex(QuestionTable.Cols.SOLVED));

        QuestionCountry qc = new QuestionCountry(country, capital, Integer.valueOf(idString), latitude, longitude);
        qc.setSolved(solved == 1);
        return qc;
    }
}
