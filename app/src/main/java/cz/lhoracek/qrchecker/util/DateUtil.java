package cz.lhoracek.qrchecker.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String formatDate(Date date){
        return date == null ? "" : SimpleDateFormat.getDateTimeInstance().format(date);
    }
}
