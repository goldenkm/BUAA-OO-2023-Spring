import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Scanner sc = new Scanner(System.in);
    private static final String PATTERN_DATE =
            "([0-9]{1,4})?/[01]?[0-9]?/[0-3]?[0-9]?";
    private static final String PATTERN_NAME = "[a-zA-Z0-9]+";
    private static final String PATTERN_JUDGE = "@[a-zA-Z0-9]+ ";
    private static final String ONLY_YEAR = "\\d{1,4}//";
    private static final String ONLY_MONTH = "/[01]?[0-9]/";
    private static final String ONLY_DAY = "//[0-3]?[0-9]";
    private static final String YEAR_AND_MONTH = "\\d{1,4}/[01]?[0-9]/";
    private static final String YEAR_AND_DAY = "\\d{1,4}//[0-3]?[0-9]";
    private static final String MONTH_AND_DAY = "/[0-1]?[0-9]/[0-3]?[0-9]";
    private static ArrayList<Message> messageArrayList = new ArrayList<>();

    public static void main(String[] args) {
        String messages = new String();
        while (!(messages = sc.nextLine()).equals("END_OF_MESSAGE")) {
            String[] records = messages.split(";");         //分割消息
            for (String record : records) {
                if (record.indexOf("-") > 0) {
                    Message message = new Message();
                    message.setMessage(record.trim() + ";");
                    message.setDate(returnDate(record));
                    message.setSender(returnSender(record));
                    if (haveReceiver(record)) { //有接受者
                        message.setReceiver(returnReceiver(record));
                    }
                    messageArrayList.add(message);
                }
            }
        }
        while (sc.hasNext()) {
            messages = sc.nextLine();
            String searchResult = new String();
            String[] records = messages.split(" ");
            if (records[0].equals("qdate")) {
                Pattern pattern = Pattern.compile(PATTERN_DATE);
                Matcher matcher = pattern.matcher(records[1]);
                if (matcher.find()) {
                    searchDate(matcher.group());
                }
            } else if (records[0].equals("qsend")) {
                Pattern pattern = Pattern.compile(PATTERN_NAME);
                if (!records[1].equals("-v")) {
                    Matcher matcher = pattern.matcher(records[1]);
                    if (matcher.find()) {
                        searchSender(matcher.group());
                    }
                } else {
                    Matcher matcher = pattern.matcher(records[2]);
                    if (matcher.find()) {
                        searchSender(matcher.group());
                    }
                }
            } else if (records[0].equals("qrecv")) {
                Pattern pattern = Pattern.compile(PATTERN_NAME);
                if (!records[1].equals("-v")) {
                    Matcher matcher = pattern.matcher(records[1]);
                    if (matcher.find()) {
                        searchReceiver(matcher.group());
                    }
                } else {
                    Matcher matcher = pattern.matcher(records[2]);
                    if (matcher.find()) {
                        searchReceiver(matcher.group());
                    }
                }
            }
        }
    }

    public static String returnDate(String message) {
        int end = message.indexOf("-");
        int start = 0;
        Pattern pattern = Pattern.compile(PATTERN_DATE);
        Matcher matcher = pattern.matcher(message.substring(start, end));
        if (!matcher.find()) {
            throw new RuntimeException("Invalid Input");
        } else {
            return matcher.group();
        }
    }

    public static String returnSender(String message) {
        int end = message.indexOf(":");
        int start = message.indexOf("-");
        Pattern pattern = Pattern.compile(PATTERN_NAME);
        Matcher matcher = pattern.matcher(message.substring(start, end));
        if (!matcher.find()) {
            throw new RuntimeException("Invalid Input");
        } else {
            return matcher.group();
        }
    }

    public static String returnReceiver(String message) {
        int end = message.length() - 1;
        int start = message.indexOf("@");
        Pattern pattern = Pattern.compile(PATTERN_NAME);
        Matcher matcher = pattern.matcher(message.substring(start, end));
        if (!matcher.find()) {
            throw new RuntimeException("Invalid Input");
        } else {
            return matcher.group();
        }
    }

    public static boolean haveReceiver(String message) {
        Pattern pattern = Pattern.compile(PATTERN_JUDGE);
        Matcher matcher = pattern.matcher(message);
        return matcher.find();
    }

    public static void searchDate(String result) {
        //System.out.println(result);
        for (Message message : messageArrayList) {
            //System.out.println(dealDate(message.getDate(), result));
            if (dealDate(message.getDate(), result)) {
                System.out.println(message.getMessage());
            }
        }
    }

    public static void searchSender(String result) {
        Pattern subString = Pattern.compile(result);
        for (Message message : messageArrayList) {
            Matcher matcher = subString.matcher(message.getSender());
            if (matcher.find()) {
                System.out.println(message.getMessage());
            }
        }
    }

    public static void searchReceiver(String result) {
        Pattern subString = Pattern.compile(result);
        for (Message message : messageArrayList) {
            if (message.getReceiver().length() > 0) {
                Matcher matcher = subString.matcher(message.getReceiver());
                if (matcher.find()) {
                    System.out.println(message.getMessage());
                }
            }
        }
    }

    public static boolean dealDate(String date1, String date2) {
        String[] dates1 = date1.split("/");
        int year1 = 0;
        int month1 = 0;
        int day1 = 0;
        year1 = Integer.parseInt(dates1[0]);
        month1 = Integer.parseInt(dates1[1]);
        day1 = Integer.parseInt(dates1[2]);
        String[] dates2 = date2.split("/");
        int year2 = 0;
        int month2 = 0;
        int day2 = 0;
        if (date2.matches(ONLY_YEAR)) {
            year2 = Integer.parseInt(dates2[0]);
        } else if (date2.matches(ONLY_MONTH)) {
            month2 = Integer.parseInt(dates2[1]);
        } else if (date2.matches(ONLY_DAY)) {
            day2 = Integer.parseInt(dates2[2]);
        } else if (date2.matches(YEAR_AND_MONTH)) {
            year2 = Integer.parseInt(dates2[0]);
            month2 = Integer.parseInt(dates2[1]);
        } else if (date2.matches(YEAR_AND_DAY)) {
            year2 = Integer.parseInt(dates2[0]);
            day2 = Integer.parseInt(dates2[2]);
        } else if (date2.matches(MONTH_AND_DAY)) {
            month2 = Integer.parseInt(dates2[1]);
            day2 = Integer.parseInt(dates2[2]);
        } else {
            year2 = Integer.parseInt(dates2[0]);
            month2 = Integer.parseInt(dates2[1]);
            day2 = Integer.parseInt(dates2[2]);
        }
        if ((year1 == year2 || year2 == 0)
                && (month1 == month2 || month2 == 0)
                && (day1 == day2 || day2 == 0)) {
            return true;
        }
        return false;
    }
}
