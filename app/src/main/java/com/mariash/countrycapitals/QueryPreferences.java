package com.mariash.countrycapitals;

import android.content.Context;
import android.preference.PreferenceManager;

//The class stores score number and number of hints
public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY_SCORE = "searchQueryScore";
    private static final String PREF_SEARCH_QUERY_HINTS = "searchQueryHints";

    public static int getStoredQueryScore(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_SEARCH_QUERY_SCORE, 0);
    }

    public static int getStoredQueryHints(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_SEARCH_QUERY_HINTS, Hints.NUM_OF_HINTS);
    }

    public static void setPrefSearchQueryScore(Context context, int query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_SEARCH_QUERY_SCORE, query)
                .apply();
    }

    public static void setPrefSearchQueryHints(Context context, int query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_SEARCH_QUERY_HINTS, query)
                .apply();
    }
}
