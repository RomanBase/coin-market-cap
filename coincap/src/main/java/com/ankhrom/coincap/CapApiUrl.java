package com.ankhrom.coincap;

/**
 * Created by R' on 2/1/2018.
 */

public class CapApiUrl {

    public static final String BASE_URL = " http://coincap.io/";
    public static final String HISTORY = BASE_URL + "history/";

    public static final String HISTORY_1DAY = HISTORY + "1day/";
    public static final String HISTORY_7DAYS = HISTORY + "7day/";
    public static final String HISTORY_30DAYS = HISTORY + "30day/";
    public static final String HISTORY_90DAYS = HISTORY + "90day/";
    public static final String HISTORY_180DAYS = HISTORY + "180day/";
    public static final String HISTORY_1YEAR = HISTORY + "365day/";

    public static String getHistoryUrl(CapHistoryTimeFrame timeFrame) {

        switch (timeFrame) {

            case D_1:
                return HISTORY_1DAY;
            case D_7:
                return HISTORY_7DAYS;
            case D_30:
                return HISTORY_30DAYS;
            case D_90:
                return HISTORY_90DAYS;
            case D_180:
                return HISTORY_180DAYS;
            case Y_1:
                return HISTORY_1YEAR;
        }

        return HISTORY_1DAY;
    }
}
