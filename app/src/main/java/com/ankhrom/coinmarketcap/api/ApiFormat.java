package com.ankhrom.coinmarketcap.api;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by R' on 12/30/2017.
 */

public class ApiFormat {

    public static final double ONE = 1;
    public static final double TEN = 10;

    public static final double THOUSAND = 1000;
    public static final double HUNDRED_THOUSAND = 100000;
    public static final double MILLION = HUNDRED_THOUSAND * 100;
    public static final double BILLION = MILLION * 100;

    public static final String THOUSAND_SIGN = " K";
    public static final String MILLION_SIGN = " M";
    public static final String BILLION_SIGN = " B";

    private static final NumberFormat shortFormat = new DecimalFormat("#0.00");
    private static final NumberFormat extendedFormat = new DecimalFormat("#0.000");
    private static final NumberFormat longFormat = new DecimalFormat("#0.0000");

    public static String toPriceFormat(String number) {

        double value = Double.parseDouble(number);

        if (value > THOUSAND) {
            number = NumberFormat.getInstance().format((int) Math.round(value)) + "." + Math.round((value - Math.floor(value)) * 100.0);
        } else if (value > TEN) {
            number = toShortFormatString(value);
        } else if (value > ONE) {
            number = toExtendedFormatString(value);
        } else {
            number = toLongFormatString(value);
        }

        return number;
    }

    public static String toShortFormat(String number) {

        double value = Double.parseDouble(number);

        if (value > ApiFormat.BILLION) {
            value /= ApiFormat.BILLION;
            number = toShortFormatString(value) + BILLION_SIGN;
        } else if (value > ApiFormat.MILLION) {
            value /= ApiFormat.MILLION;
            number = toShortFormatString(value) + MILLION_SIGN;
        } else {
            value /= ApiFormat.HUNDRED_THOUSAND;
            number = toShortFormatString(value) + THOUSAND_SIGN;
        }

        return number;
    }

    public static String toShortFormatString(double number) {

        return shortFormat.format(number);
    }

    public static String toExtendedFormatString(double number) {

        return extendedFormat.format(number);
    }

    public static String toLongFormatString(double number) {

        return longFormat.format(number);
    }

}
