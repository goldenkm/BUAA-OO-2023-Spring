import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Scanner sc = new Scanner(System.in);
    private static final String PATTERN_DATE =
            "([0-9]{1,4})?/[0-9]?[0-9]?/[0-9]?[0-9]?";
    private static final String PATTERN_NAME = "[a-zA-Z0-9]+";
    private static final String PATTERN_JUDGE = "@[a-zA-Z0-9]+ ";
    private static final String ONLY_YEAR = "\\d{1,4}//";
    private static final String ONLY_MONTH = "/([0]?[0-9]|1[0-2])/";
    private static final String ONLY_DAY = "//([0-2]?[0-9]|3[01])";
    private static final String YEAR_AND_MONTH = "\\d{1,4}/(0?[1-9]|1[0-2])/";
    private static final String YEAR_AND_DAY = "\\d{1,4}//([0-2]?[0-9]|3[01])";
    private static final String MONTH_AND_DAY = "/(0?[1-9]|1[0-2])/([0-2]?[0-9]|3[0-1])";
    private static final String YEAR_MONTH_DAY = "\\d{1,4}/[0-9]?[0-9]/[0-9]?[0-9]";
    private static final String MESSAGE_CONTENT = "\".*?\"";
    private static Pattern searchSsq = Pattern.compile("-ssq");
    private static Pattern searchSsr = Pattern.compile("-ssr");
    private static Pattern searchPre = Pattern.compile("-pre");
    private static Pattern searchPos = Pattern.compile("-pos");
    private static Pattern vagueSearch = Pattern.compile("-v");
    private static Pattern clean = Pattern.compile("-c");
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
            String[] records = messages.split(" ");
            ArrayList<String> ans = new ArrayList<>();
            if (records[0].equals("qdate")) {
                ans = queryDate(messages);
            } else if (records[0].equals("qsend")) {
                if (checkQuery(messages)) {
                    ans = querySender(messages);
                } else {
                    System.out.println("Command Error!: Not Vague Query! \""
                            + messages + "\"");
                }
            } else if (records[0].equals("qrecv")) {
                if (checkQuery(messages)) {
                    ans = queryReceiver(messages);
                } else {
                    System.out.println("Command Error!: Not Vague Query! \""
                            + messages + "\"");
                }
            }
            Matcher matcher2 = clean.matcher(messages);
            boolean isClean = matcher2.find();
            if (isClean) {
                String[] records1 = messages.split("-c");
                Pattern patternClean = Pattern.compile(MESSAGE_CONTENT);
                Matcher matcherClean = patternClean.matcher(records1[1]);
                if (matcherClean.find()) {
                    String cleanContent = matcherClean.group();
                    cleanContent = cleanContent.substring(1, cleanContent.length() - 1);
                    for (int i = 0; i < ans.size(); i++) {
                        String answer = cleanKeyword(ans.get(i), cleanContent);
                        System.out.println(answer);
                    }
                }
            } else {
                for (int i = 0; i < ans.size(); i++) {
                    System.out.println(ans.get(i));
                }
            }
        }
    }

    public static ArrayList<String> queryDate(String messages) {
        Pattern pattern = Pattern.compile(PATTERN_DATE);
        Matcher matcher = pattern.matcher(messages);
        ArrayList<String> ans = new ArrayList<>();
        if (matcher.find()) {
            String date = matcher.group();
            if (checkDate(date)) {
                ans = searchDate(matcher.group());
            } else {
                ans.add("Command Error!: Wrong Date Format! \"" +
                        messages + "\"");
            }
        }
        return ans;
    }

    public static ArrayList<String> querySender(String messages) {
        Pattern messageContent = Pattern.compile(MESSAGE_CONTENT);
        Matcher matcherContent = messageContent.matcher(messages);
        String content1 = new String();
        if (matcherContent.find()) {
            String temp = matcherContent.group();
            int start = temp.indexOf("\"") + 1;
            int end = temp.length() - 1;
            content1 = temp.substring(start, end);
        }
        Matcher matcher1 = vagueSearch.matcher(messages);
        Pattern pattern = Pattern.compile(PATTERN_NAME);
        boolean isVagueQuiry = matcher1.find();
        Matcher matcherSsq = searchSsq.matcher(messages);
        Matcher matcherSsr = searchSsr.matcher(messages);
        Matcher matcherPre = searchPre.matcher(messages);
        Matcher matcherPos = searchPos.matcher(messages);
        ArrayList<String> ans = new ArrayList<>();
        if (isVagueQuiry) { //-v 只模糊查询
            if (matcherSsq.find()) { //子序串
                return querySsq(content1, 0);
            } else if (matcherSsr.find()) {
                return querySsr(content1, 0);
            } else if (matcherPre.find()) {
                return queryPre(content1, 0);
            } else if (matcherPos.find()) {
                return queryPos(content1, 0);
            } else {
                return querySsr(content1, 0);
            }
        } else {
            return querySsr(content1, 0);
        }
    }

    public static ArrayList<String> queryReceiver(String messages) {
        Pattern messageContent = Pattern.compile(MESSAGE_CONTENT);
        Matcher matcherContent = messageContent.matcher(messages);
        String content1 = new String();
        if (matcherContent.find()) {
            String temp = matcherContent.group();
            int start = temp.indexOf("\"") + 1;
            int end = temp.length() - 1;
            content1 = temp.substring(start, end);
        }
        Matcher matcher1 = vagueSearch.matcher(messages);
        Pattern pattern = Pattern.compile(PATTERN_NAME);
        boolean isVagueQuiry = matcher1.find();
        Matcher matcherSsq = searchSsq.matcher(messages);
        Matcher matcherSsr = searchSsr.matcher(messages);
        Matcher matcherPre = searchPre.matcher(messages);
        Matcher matcherPos = searchPos.matcher(messages);
        if (isVagueQuiry) { //-v 只模糊查询
            if (matcherSsq.find()) { //子序串
                return querySsq(content1, 1);
            } else if (matcherSsr.find()) {
                return querySsr(content1, 1);
            } else if (matcherPre.find()) {
                return queryPre(content1, 1);
            } else if (matcherPos.find()) {
                return queryPos(content1, 1);
            } else {
                return querySsr(content1, 1);
            }
        } else {
            return querySsr(content1, 1);
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

    public static ArrayList<String> searchDate(String result) {
        ArrayList<String> ans = new ArrayList<>();
        for (Message message : messageArrayList) {
            if (dealDate(message.getDate(), result)) {
                ans.add(message.getMessage());
            }
        }
        return ans;
    }

    public static boolean checkQuery(String messages) {
        Matcher matcherSsq = searchSsq.matcher(messages);
        Matcher matcherSsr = searchSsr.matcher(messages);
        Matcher matcherPre = searchPre.matcher(messages);
        Matcher matcherPos = searchPos.matcher(messages);
        Pattern vague = Pattern.compile("-v");
        Matcher matcherVague = vague.matcher(messages);
        if (matcherVague.find()) { //is vague search
            int start = messages.indexOf("-v");
            if (matcherSsq.find()) {
                if (messages.indexOf("-ssq") < start) {
                    return false;
                }
            } else if (matcherSsr.find()) {
                if (messages.indexOf("-ssr") < start) {
                    return false;
                }
            } else if (matcherPre.find()) {
                if (messages.indexOf("-pre") < start) {
                    return false;
                }
            } else if (matcherPos.find()) {
                if (messages.indexOf("-pos") < start) {
                    return false;
                }
            }
        } else {
            if (matcherSsq.find() || matcherSsr.find()
                    || matcherPre.find() || matcherPos.find()) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> querySsq(String content, Integer flag) {
        //flag=0,querySender;flag=1,queryReceiver
        char[] contents = content.toCharArray();
        String subsequence = new String();
        for (int i = 0; i < contents.length; i++) {
            subsequence += contents[i] + ".*";
        }
        Pattern pattern = Pattern.compile(subsequence);
        ArrayList<String> ans = new ArrayList<>();
        for (Message message : messageArrayList) {
            Matcher matcher = pattern.matcher(message.getSender());
            if (flag == 1) {
                if (message.getReceiver().length() > 0) {
                    matcher = pattern.matcher(message.getReceiver());
                } else {
                    continue;
                }
            }
            if (matcher.find()) {
                ans.add(message.getMessage());
            }
        }
        return ans;
    }

    public static ArrayList<String> querySsr(String result, Integer flag) {
        Pattern subString = Pattern.compile(result);
        ArrayList<String> ans = new ArrayList<>();
        for (Message message : messageArrayList) {
            Matcher matcher = subString.matcher(message.getSender());
            if (flag == 1) {
                if (message.getReceiver().length() > 0) {
                    matcher = subString.matcher(message.getReceiver());
                } else {
                    continue;
                }
            }
            if (matcher.find()) {
                ans.add(message.getMessage());
            }
        }
        return ans;
    }

    public static ArrayList<String> queryPre(String content, Integer flag) {
        ArrayList<String> ans = new ArrayList<>();
        for (Message message : messageArrayList) {
            if (flag == 0) {
                if (message.getSender().startsWith(content)) {
                    ans.add(message.getMessage());
                }
            } else {
                if (message.getReceiver().length() > 0 &&
                        message.getReceiver().startsWith(content)) {
                    ans.add(message.getMessage());
                }
            }
        }
        return ans;
    }

    public static ArrayList<String> queryPos(String content, Integer flag) {
        ArrayList<String> ans = new ArrayList<>();
        for (Message message : messageArrayList) {
            boolean judge = message.getSender().endsWith(content);
            if (flag == 1) {
                if (message.getReceiver().length() > 0) {
                    judge = message.getReceiver().endsWith(content);
                } else {
                    continue;
                }
            }
            if (judge) {
                ans.add(message.getMessage());
            }
        }
        return ans;
    }

    public static String cleanKeyword(String content, String keyword) {
        char[] keywords = keyword.toCharArray();
        String newKeyword = new String();
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i] == '?') { //消除正则表达式中的？
                newKeyword += "\\?";
            } else if (keywords[i] == '.') {
                newKeyword += "\\.";
            } else {
                newKeyword += keywords[i];
            }
        }
        Pattern pattern = Pattern.compile(newKeyword);
        Matcher matcher = pattern.matcher(content);
        String star = new String();
        for (int i = 0; i < keyword.length(); i++) {
            star += "*";
        }
        String[] records2 = content.split(":");
        String newContent = records2[0] + ":" + records2[1].replaceAll(newKeyword, star);
        return newContent;
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

    public static boolean checkDate(String date) {
        String[] records = date.split("/");
        if (date.matches(ONLY_YEAR) || date.matches(ONLY_MONTH)
                || date.matches(ONLY_DAY) || date.matches(YEAR_AND_MONTH)
                || date.matches(YEAR_AND_DAY) || date.matches(MONTH_AND_DAY)) {
            return true;
        } else if (date.matches(YEAR_MONTH_DAY) && yearMonthDay(records)) {
            return true;
        }
        return false;
    }

    public static boolean yearMonthDay(String[] dates) {
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        if (!isLeapYear(year)) {
            if (month == 2 && day > 28) {
                return false;
            }
        }
        if (!(month == 1 || month == 3 || month == 5 ||
                month == 7 || month == 8 || month == 10 || month == 12)) {
            if (day > 30) {
                return false;
            }
        }
        if (month > 12 || day > 31) {
            return false;
        }
        return true;
    }

    public static boolean isLeapYear(Integer year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }
}