package cn.com.felix.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STANDARD_FORMAT);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public static String getCurrentTime() {
        return simpleDateFormat.format(new Date());
    }

    public static String getDateTime(Date date){
        if(date ==null) {
            return "";
        }
        return dateFormat.format(date);
    }


    public static String getFullDateTime(Date date){
        if(date ==null) {
            return "";
        }
        return simpleDateFormat.format(date);
    }

    /**
     * @title: dateCompare
     * @description: 比较日期大小
     * @param date1 日期1
     * @param date2 日期2
     * @return
     */
    public static int dateCompare(Date date1, Date date2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateFirst = dateFormat.format(date1);
        String dateLast = dateFormat.format(date2);
        int dateFirstIntVal = Integer.parseInt(dateFirst);
        int dateLastIntVal = Integer.parseInt(dateLast);
        if (dateFirstIntVal > dateLastIntVal) {
            return 1;
        } else if (dateFirstIntVal < dateLastIntVal) {
            return -1;
        }
        return 0;
    }
}
