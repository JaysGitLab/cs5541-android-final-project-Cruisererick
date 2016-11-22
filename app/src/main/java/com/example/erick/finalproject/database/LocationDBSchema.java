package com.example.erick.finalproject.database;

/**
 * Created by eric on 10/6/2016.
 */

public class LocationDBSchema {




    public static final class LocationTable {
        public static final String NAME = "locations";


        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DESCRIPTION = "description";
            //public static final String DATE = "date";
            //public static final String SOLVED = "solved";
            public static final String LOCATION = "location";
        }
    }
}
