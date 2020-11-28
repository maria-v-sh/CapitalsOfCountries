package com.mariash.countrycapitals;

// The class contains the data required for a question
public class QuestionCountry {
    private int mId;
    private String mCountry;
    private String mCapital;
    private boolean mIsSolved;

    // Data for searching on the Google map
    private double mLatitude;
    private double mLongitude;


    public QuestionCountry(String country, String capital, int index, double latitude, double longitude) {
        mId = index;
        mCountry = country;
        mCapital = capital;
        mLatitude = latitude;
        mLongitude = longitude;
        mIsSolved = false;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getCapital() {
        return mCapital;
    }

    public int getId() {
        return mId;
    }

    public boolean isSolved() {
        return mIsSolved;
    }

    public void setSolved(boolean solved) {
        mIsSolved = solved;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
