package com.mariash.countrycapitals.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mariash.countrycapitals.database.QuestionCountryDBSchema.QuestionTable;

//Create and update database
public class QuestionCountryBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "questionCountryBase.db";

    public QuestionCountryBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + QuestionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                QuestionTable.Cols.ID + ", " +
                QuestionTable.Cols.COUNTRY + ", " +
                QuestionTable.Cols.CAPITAL + ", " +
                QuestionTable.Cols.LATITUDE + ", " +
                QuestionTable.Cols.LONGITUDE + ", " +
                QuestionTable.Cols.SOLVED +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
