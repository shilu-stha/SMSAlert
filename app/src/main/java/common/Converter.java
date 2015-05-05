package common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by shilushrestha on 5/5/15.
 */
public class Converter {

    final static String dateFormat = "E d-MMM-yy";
    final static String timeFormat = "h:mma";

    public static String DateConverter(long timeMilliSeconds){
        DateFormat format = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMilliSeconds);
        return format.format(cal.getTime());
    }

    public static String TimeConverter(long timeMilliSeconds){
        DateFormat format = new SimpleDateFormat(timeFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMilliSeconds);
        return format.format(cal.getTime());
    }
}
