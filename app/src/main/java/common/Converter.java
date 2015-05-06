package common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/5/15
 */
public class Converter {

    final static String mDateFormat = "E d-MMM-yy";
    final static String mTimeFormat = "h:mma";

    /**
     *
     * @param timeMilliSeconds
     * @return string date formatted according to mDateFormat
     */
    public static String DateConverter(long timeMilliSeconds) {
        DateFormat mFormat = new SimpleDateFormat(mDateFormat);
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(timeMilliSeconds);
        return mFormat.format(mCal.getTime());
    }

    /**
     *
     * @param timeMilliSeconds
     * @return string time formatted according to mTimeFormat
     */
    public static String TimeConverter(long timeMilliSeconds) {
        DateFormat mFormat = new SimpleDateFormat(mTimeFormat);
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(timeMilliSeconds);
        return mFormat.format(mCal.getTime());
    }
}
