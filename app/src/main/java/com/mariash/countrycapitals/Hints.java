package com.mariash.countrycapitals;

import android.content.Context;

//The class-singleton contains the number of hints that the user can use
public class Hints {
    public static final int NUM_OF_HINTS = 20;

    private int mNumOfHints;
    private static Hints sHints;

    private Hints(Context context) {
        mNumOfHints = QueryPreferences.getStoredQueryHints(context);
    }

    public static Hints get(Context context) {
        if(sHints == null) {
            sHints = new Hints(context);
        }
        return sHints;
    }

    public int getNumOfHints() {
        return mNumOfHints;
    }

    public void numDecrease(Context context) {
        if(mNumOfHints > 0) {
            mNumOfHints--;
            setNumOfHints(context);
        }
    }

    public void renew(Context context) {
        mNumOfHints = NUM_OF_HINTS;
        setNumOfHints(context);
    }

    private void setNumOfHints(Context context) {
        QueryPreferences.setPrefSearchQueryHints(context, mNumOfHints);
    }
}
