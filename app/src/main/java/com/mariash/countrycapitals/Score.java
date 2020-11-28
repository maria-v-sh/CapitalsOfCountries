package com.mariash.countrycapitals;

import android.content.Context;

//The class-singleton contains the number of correct answers made by the user
public class Score {
    private int mNumOfRightAnswer;
    private static Score sScore;

    private Score(Context context) {
        mNumOfRightAnswer = QueryPreferences.getStoredQueryScore(context);
    }

    public static Score get(Context context) {
        if (sScore == null) {
            sScore = new Score(context);
        }
        return sScore;
    }

    public int getNumOfRightAnswer() {
        return mNumOfRightAnswer;
    }

    public void increaseNumOfRightAnswer(Context context) {
        mNumOfRightAnswer++;
        setNumOfRightAnswer(context);
    }

    public void renew(Context context) {
        mNumOfRightAnswer = 0;
        setNumOfRightAnswer(context);
    }

    private void setNumOfRightAnswer(Context context) {
        QueryPreferences.setPrefSearchQueryScore(context, mNumOfRightAnswer);
    }
}
