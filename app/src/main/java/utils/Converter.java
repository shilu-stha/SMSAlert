package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import components.Constants;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/5/15
 */
public class Converter {

    /**
     *
     * @param timeMilliSeconds
     * @return : string date formatted as <code><i>E d-MMM-yy</i></code>
     */
    public static String DateConverter(long timeMilliSeconds) {
        DateFormat mFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(timeMilliSeconds);

        return mFormat.format(mCal.getTime());
    }

    /**
     *
     * @param timeMilliSeconds
     * @return : string time formatted as <code><i>h:mma</i></code>
     */
    public static String TimeConverter(long timeMilliSeconds) {
        DateFormat mFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(timeMilliSeconds);

        return mFormat.format(mCal.getTime());
    }
}
