package com.mariash.countrycapitals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mariash.countrycapitals.database.QuestionCountryBaseHelper;
import com.mariash.countrycapitals.database.QuestionCountryDBSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mariash.countrycapitals.database.QuestionCountryDBSchema.*;

// The class-singleton contains list of QuestionCountries
public class QuestionsCountryLab {

    //Data required to create QuestionCountry
    private String[] mArrayCountries;
    private String[] mArrayCapitals;
    private double[] mArrayLatitude;
    private double[] mArrayLongitude;

    private static QuestionsCountryLab sCountryLab;

    //Data required to DataBase
    private SQLiteDatabase mDatabase;
    //helps avoid duplication

    private QuestionsCountryLab(Context context) {
        mDatabase = new QuestionCountryBaseHelper(context).getWritableDatabase();
    }

    public static QuestionsCountryLab get(Context context) {
        if(sCountryLab == null) {
            sCountryLab = new QuestionsCountryLab(context);
        }
        return sCountryLab;
    }

    //Create list of QuestionCountries and save it in database
    public void createQuestionCountry() {
        List<QuestionCountry> questionsAux;
        questionsAux = getQuestions();
        //if DB is empty
        if(questionsAux.size() == 0) {

            List<QuestionCountry> questions;
            questions = getEmptyQuestionCountryList();

            //Sort the list by country name
            Collections.sort(questions, new Comparator<QuestionCountry>() {
                @Override
                public int compare(QuestionCountry countryFirst, QuestionCountry countrySecond) {
                    return countryFirst.getCountry().compareTo(countrySecond.getCountry());
                }
            });

            for (int i = 0; i < mArrayCapitals.length; i++) {
                ContentValues cv = getContentValues(questions.get(i));
                mDatabase.insert(QuestionTable.NAME, null, cv);
            }
        }
    }

    public List<QuestionCountry> getQuestions() {
        List<QuestionCountry> questionCountries = new ArrayList<>();
        QuestionCountryCursorWrapper cursorWrapper = queryQuestionCountry(null, null);

        //extraction data from a database
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                questionCountries.add(cursorWrapper.getQuestionCountry());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return questionCountries;
    }

    //Set data required to create QuestionCountry
    public void setCountriesCapitalsLatitudeLongitude(String[] arrayCountries, String[] arrayCapitals, double[] arrayLatitude, double[] arrayLongitude) {
        mArrayCountries = arrayCountries;
        mArrayCapitals = arrayCapitals;
        mArrayLatitude = arrayLatitude;
        mArrayLongitude = arrayLongitude;
    }

    //Converts a QuestionCountry object to an ContentValues object
    private static ContentValues getContentValues(QuestionCountry questionCountry) {
        ContentValues values = new ContentValues();
        values.put(QuestionTable.Cols.ID, String.valueOf(questionCountry.getId()));
        values.put(QuestionTable.Cols.COUNTRY, questionCountry.getCountry());
        values.put(QuestionTable.Cols.CAPITAL, questionCountry.getCapital());
        values.put(QuestionTable.Cols.LATITUDE, String.valueOf(questionCountry.getLatitude()));
        values.put(QuestionTable.Cols.LONGITUDE, String.valueOf(questionCountry.getLongitude()));
        values.put(QuestionTable.Cols.SOLVED, questionCountry.isSolved() ? 1 : 0);
        return values;
    }

    //update if correct answer was given
    public void updateQuestionCountry(QuestionCountry questionCountry){
        String idString = String.valueOf(questionCountry.getId());
        ContentValues values = getContentValues(questionCountry);

        mDatabase.update(QuestionTable.NAME, values,
                QuestionTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    //aux for read data from dataBase
    private QuestionCountryCursorWrapper queryQuestionCountry(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                QuestionTable.NAME,
                null, //all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new QuestionCountryCursorWrapper(cursor);
    }

    public void renew() {
        List<QuestionCountry> questions = getEmptyQuestionCountryList();
        for(int i = 0; i < questions.size(); i++) {
            updateQuestionCountry(questions.get(i));
        }
    }

    private List<QuestionCountry> getEmptyQuestionCountryList() {
        List<QuestionCountry> questions = new ArrayList<>();
        for (int i = 0; i < mArrayCapitals.length; i++) {
            QuestionCountry qc = new QuestionCountry(mArrayCountries[i], mArrayCapitals[i], i+1, mArrayLatitude[i], mArrayLongitude[i]);
            questions.add(qc);
        }
        return questions;
    }
}
