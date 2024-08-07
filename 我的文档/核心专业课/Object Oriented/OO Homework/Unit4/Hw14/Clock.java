import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Clock {
    private static int interval = 0;
    private static Calendar calendar;
    private static SimpleDateFormat sdf;

    public static String getDate() {
        return sdf.format(calendar.getTime());
    }

    public static void initialClock() {
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin;
        try {
            begin = sdf.parse("2023-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.setTime(begin);
    }

    public static void addByDay() {
        calendar.add(Calendar.DATE, 1);
        interval++;
    }

    public static boolean needToArrange() {
        return interval >= 3;
    }

    public static void clearInterval() {
        interval = 0;
    }
}
