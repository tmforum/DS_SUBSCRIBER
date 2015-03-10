package org.tmf.dsmapi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private static SimpleDateFormat sdf;

    private static SimpleDateFormat getDateFormat() {
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
            TimeZone tz = TimeZone.getTimeZone("UTC");
            sdf.setTimeZone(tz);
        }
        return sdf;
    }

    public static String getDateAsString() {
        Date date = new Date();
        return getDateFormat().format(date);
    }

}
