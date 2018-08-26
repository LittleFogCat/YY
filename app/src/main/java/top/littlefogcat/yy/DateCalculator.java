package top.littlefogcat.yy;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LittleFogCat on 2018/8/26.
 */
public class DateCalculator {
    private Calendar orig;

    public DateCalculator(long orig) {
        this.orig = Calendar.getInstance();
        this.orig.setTimeInMillis(orig);
    }

    public int cal() {
        Calendar curr = Calendar.getInstance();
        int origYear = orig.get(Calendar.YEAR);
        int currYear = curr.get(Calendar.YEAR);
        int origDate = orig.get(Calendar.DAY_OF_YEAR);
        int currDate = curr.get(Calendar.DAY_OF_YEAR);
        int yearDelta = currYear - origYear;
        int dayDelta = currDate - origDate;

        Log.d(TAG, "cal: " + origYear + ", " + currYear);
        Log.d(TAG, "cal: " + origDate + ", " + currDate);
        return (int) (dayDelta + 365.25 * yearDelta);
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

//    public static int daysBetween(Calendar early, Calendar late) {
//        int earlyYear = early.get(Calendar.YEAR);
//        int earlyMonth = early.get(Calendar.MONTH);
//        int earlyDayOfMonth = early.get(Calendar.DAY_OF_MONTH);
//        int lateYear = late.get(Calendar.YEAR);
//
//        if (earlyYear == lateYear){
//            // 同年
//
//        }
//    }

    private static final String TAG = "DateCalculator";

    /**
     * 计算两个日期相差多少天
     *
     * @param c1 早些的日期
     * @param c2 晚些的日期
     * @return 天数
     */
    public static int dayBetween(Calendar c1, Calendar c2) {
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        long diffMillis = c2.getTimeInMillis() - c1.getTimeInMillis();
        return Math.round(diffMillis / 86400000);
    }

    public static int dayBetween(String day1, String day2, String format) throws ParseException {
        Log.d(TAG, "dayBetween: " + day1 + " and " + day2);
        return dayBetween(str2Calendar(day1, format), str2Calendar(day2, format));
    }

    @SuppressLint("SimpleDateFormat")
    private static Calendar str2Calendar(String s, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(s);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }
}
