package com.mariash.countrycapitals.database;

//The class contains schema of database
public class QuestionCountryDBSchema {
    public static final class QuestionTable {
        public static final String NAME = "questionCountry";

        public static final class Cols {
            public static final String ID = "id";
            public static final String COUNTRY = "country";
            public static final String CAPITAL = "capital";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String SOLVED = "solved";
        }
    }
}
