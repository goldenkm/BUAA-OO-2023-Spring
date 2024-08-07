import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Scanner sc = new Scanner(System.in);
    private static final String DOWNLOAD_TIME = "\"download_datetime\": *\".*?\"";
    private static final String CREATED_AT = "\"created_at\": *\".*?\"";
    private static final String ID = "\"id\": *[0-9]*";
    private static final String FULL_TEXT = "\"full_text\": *((\".*?\")|(null))";
    private static final String USER_ID = "\"user_id\": *[0-9]*";
    private static final String RETWEET_COUNT = "\"retweet_count\": *[0-9]*";
    private static final String FAVORITE_COUNT = "\"favorite_count\": *[0-9]*";
    private static final String REPLY_COUNT = "\"reply_count\": *[0-9]*";
    private static final String POSSIBLY_SENSITIVE_EDITABLE =
            "\"possibly_sensitive_editable\": *((true)|(false))";
    private static final String LANG = "\"lang\": *\".*?\"";
    private static final String EMOJIS_NAME = "\"name\": *\".*?\"";
    private static final String EMOJI_ID = "\"emoji_id\": *[0-9]*";
    private static final String EMOJI_COUNT = "\"count\": *[0-9]*";
    private static Pattern pattern1 = Pattern.compile(CREATED_AT);
    private static Pattern pattern2 = Pattern.compile(ID);
    private static Pattern pattern3 = Pattern.compile(FULL_TEXT);
    private static Pattern pattern4 = Pattern.compile(USER_ID);
    private static Pattern pattern5 = Pattern.compile(RETWEET_COUNT);
    private static Pattern pattern6 = Pattern.compile(FAVORITE_COUNT);
    private static Pattern pattern7 = Pattern.compile(REPLY_COUNT);
    private static Pattern pattern8 = Pattern.compile(POSSIBLY_SENSITIVE_EDITABLE);
    private static Pattern pattern9 = Pattern.compile(LANG);
    private static Pattern pattern10 = Pattern.compile(EMOJIS_NAME);
    private static Pattern pattern11 = Pattern.compile(EMOJI_ID);
    private static Pattern pattern12 = Pattern.compile(EMOJI_COUNT);
    private static Pattern pattern0 = Pattern.compile(DOWNLOAD_TIME);
    private static ArrayList<Json> jsons = new ArrayList<>();

    public static void main(String[] args) {
        String jsonLine = "";
        while (!(jsonLine = sc.nextLine()).equals("END_OF_MESSAGE")) {
            Json json = new Json();
            final Matcher matcher0 = pattern0.matcher(jsonLine);
            final Matcher matcher1 = pattern1.matcher(jsonLine);
            final Matcher matcher2 = pattern2.matcher(jsonLine);
            final Matcher matcher3 = pattern3.matcher(jsonLine);
            final Matcher matcher4 = pattern4.matcher(jsonLine);
            final Matcher matcher5 = pattern5.matcher(jsonLine);
            final Matcher matcher6 = pattern6.matcher(jsonLine);
            final Matcher matcher7 = pattern7.matcher(jsonLine);
            final Matcher matcher8 = pattern8.matcher(jsonLine);
            final Matcher matcher9 = pattern9.matcher(jsonLine);
            final Matcher matcher10 = pattern10.matcher(jsonLine);
            final Matcher matcher11 = pattern11.matcher(jsonLine);
            final Matcher matcher12 = pattern12.matcher(jsonLine);
            if (matcher0.find()) { //downloadTime
                buildDownloadTime(matcher0, json);
            }
            if (matcher1.find()) {
                buildCreatedAt(matcher1, json);
            }
            if (matcher2.find()) {
                buildId(matcher2, json);
            }
            if (matcher3.find()) {
                buildFullText(matcher3, json);
            }
            if (matcher4.find()) {
                buildUserId(matcher4, json);
            }
            if (matcher5.find()) {
                buildRetweetCount(matcher5, json);
            }
            if (matcher6.find()) {
                buildFavoriteCount(matcher6, json);
            }
            if (matcher7.find()) {
                buildReplyCount(matcher7, json);
            }
            if (matcher8.find()) {
                buildPossiblySensitiveEditable(matcher8, json);
            }
            if (matcher9.find()) {
                buildLang(matcher9, json);
            }
            ArrayList<Emoji> emojis = new ArrayList<>();
            emojis = buildEmojis(emojis, matcher10, matcher11, matcher12, json);
            json.setEmojis(emojis);
            jsons.add(json);
        }
        String messages = new String();
        while (sc.hasNext()) {
            messages = sc.nextLine();
            query(messages);
        }
    }

    public static void buildDownloadTime(Matcher matcher0, Json json) {
        String[] records = matcher0.group().split(":", 2);
        json.setDownloadTime(records[1].substring(1, records[1].length() - 1));
    }

    public static void buildCreatedAt(Matcher matcher1, Json json) {
        String[] records = matcher1.group().split(":", 2);
        json.setCreatedAt(records[1].substring(1, records[1].length() - 1));
    }

    public static void buildId(Matcher matcher2, Json json) {
        String[] records = matcher2.group().split(":");
        json.setId(records[1].substring(0, records[1].length()));
    }

    public static void buildFullText(Matcher matcher3, Json json) {
        String[] records = matcher3.group().split(":", 2);
        if (records[1].equals("null")) {
            json.setFullText("null");
        } else {
            json.setFullText(records[1].substring(1, records[1].length() - 1));
        }
    }

    public static void buildUserId(Matcher matcher4, Json json) {
        String[] records = matcher4.group().split(":");
        json.setUserId(records[1].substring(0, records[1].length()));
    }

    public static void buildRetweetCount(Matcher matcher5, Json json) {
        String[] records = matcher5.group().split(":");
        int count = Integer.parseInt(records[1].substring(0, records[1].length()));
        json.setRetweetCount(count);
    }

    public static void buildFavoriteCount(Matcher matcher6, Json json) {
        String[] records = matcher6.group().split(":");
        int count = Integer.parseInt(records[1].substring(0, records[1].length()));
        json.setFavoriteCount(count);
    }

    public static void buildReplyCount(Matcher matcher7, Json json) {
        String[] records = matcher7.group().split(":");
        int count = Integer.parseInt(records[1].substring(0, records[1].length()));
        json.setReplyCount(count);
    }

    public static void buildPossiblySensitiveEditable(Matcher matcher8, Json json) {
        String[] records = matcher8.group().split(":");
        if (records[1].substring(0, records[1].length()).equals("true")) {
            json.setPossiblySensitiveEditable(true);
        } else {
            json.setPossiblySensitiveEditable(false);
        }
    }

    public static void buildLang(Matcher matcher9, Json json) {
        String[] records = matcher9.group().split(":");
        json.setLang(records[1].substring(1, records[1].length() - 1));
    }

    public static ArrayList<Emoji> buildEmojis(ArrayList<Emoji> emojis, Matcher matcher10,
                                               Matcher matcher11, Matcher matcher12, Json json) {
        while (matcher10.find()) {
            Emoji emoji = new Emoji();
            String[] records = matcher10.group().split(":", 2);
            emoji.setName(records[1].substring(1, records[1].length() - 1));
            if (matcher11.find()) {
                String[] records1 = matcher11.group().split(":");
                emoji.setEmojiId(records1[1].substring(0, records1[1].length()));
            }
            if (matcher12.find()) {
                String[] records2 = matcher12.group().split(":");
                int count = Integer.parseInt(records2[1].substring(0, records2[1].length()));
                emoji.setCount(count);
            }
            emojis.add(emoji);
        }
        return emojis;
    }

    public static void query(String messages) {
        String[] strings = messages.split(" ");
        if (strings[0].equals("Qdate")) {
            queryDate(strings[1], strings[2]);
        } else if (strings[0].equals("Qemoji")) {
            queryEmoji(strings[1]);
        } else if (strings[0].equals("Qcount")) {
            queryCount(strings[1]);
        } else if (strings[0].equals("Qtext")) {
            queryText(strings[1]);
        } else if (strings[0].equals("Qsensitive")) {
            querySensitive(strings[1]);
        } else if (strings[0].equals("Qlang")) {
            queryLang(strings[1]);
        }
    }

    private static void queryDate(String userId, String dateRange) {
        String[] strings = dateRange.split("~");
        String startDate = strings[0].replaceAll("-", "");
        String endDate = strings[1].replaceAll("-", "");
        int start = Integer.parseInt(startDate);
        int end = Integer.parseInt(endDate);
        int totalCount = 0;
        int retweetCount = 0;
        int favoriteCount = 0;
        int replyCount = 0;
        for (Json json : jsons) {
            if (json.getUserId().equals(userId)) {
                int date = toDate(json.getCreatedAt());
                if (date >= start && date <= end) {
                    totalCount += 1;
                    retweetCount += json.getRetweetCount();
                    favoriteCount += json.getFavoriteCount();
                    replyCount += json.getReplyCount();
                }
            }
        }
        System.out.println(totalCount + " " + retweetCount
                + " " + favoriteCount + " " + replyCount);
    }

    private static int toDate(String createdAt) {
        String year = "";
        String month = "";
        String day = "";
        String[] strings = createdAt.split(" ");
        if (createdAt.indexOf("Jan") > 0) {
            month = "01";
        } else if (createdAt.indexOf("Feb") > 0) {
            month = "02";
        } else if (createdAt.indexOf("Mar") > 0) {
            month = "03";
        } else if (createdAt.indexOf("Apr") > 0) {
            month = "04";
        } else if (createdAt.indexOf("May") > 0) {
            month = "05";
        } else if (createdAt.indexOf("Jun") > 0) {
            month = "06";
        } else if (createdAt.indexOf("Jul") > 0) {
            month = "07";
        } else if (createdAt.indexOf("Aug") > 0) {
            month = "08";
        } else if (createdAt.indexOf("Sep") > 0) {
            month = "09";
        } else if (createdAt.indexOf("Oct") > 0) {
            month = "10";
        } else if (createdAt.indexOf("Nov") > 0) {
            month = "11";
        } else if (createdAt.indexOf("Dec") > 0) {
            month = "12";
        }
        day = strings[2];
        year = strings[4];
        String date = year + month + day;
        int newDate = Integer.parseInt(date);
        return newDate;
    }

    private static void queryEmoji(String id) {
        for (Json json : jsons) {
            if (json.getId().equals(id)) {
                if (json.getEmojis().size() == 0) {
                    System.out.println("None");
                    return;
                }
                Collections.sort(json.getEmojis());
                for (Emoji emoji : json.getEmojis()) {
                    System.out.print(emoji.getName() + " ");
                }
                System.out.print("\n");
            }
        }
    }

    private static void queryCount(String dateRange) {
        String[] strings = dateRange.split("~");
        String startDate = strings[0].replaceAll("-", "");
        String endDate = strings[1].replaceAll("-", "");
        int start = Integer.parseInt(startDate);
        int end = Integer.parseInt(endDate);
        int count = 0;
        for (Json json : jsons) {
            String[] strings1 = json.getDownloadTime().split(" ");
            int newDate = Integer.parseInt(strings1[0].replaceAll("-", ""));
            if (newDate >= start && newDate <= end) {
                count += 1;
            }
        }
        System.out.println(count);
    }

    public static void queryText(String id) {
        for (Json json : jsons) {
            if (json.getId().equals(id)) {
                if (json.getFullText().equals("null")) {
                    System.out.println("None");
                    return;
                }
                System.out.println(json.getFullText());
            }
        }
    }

    public static void querySensitive(String userId) {
        int count = 0;
        for (Json json : jsons) {
            if (json.getUserId().equals(userId)) {
                if (json.isPossiblySensitiveEditable()) {
                    count += 1;
                }
            }
        }
        System.out.println(count);
    }

    public static void queryLang(String id) {
        for (Json json : jsons) {
            if (json.getId().equals(id)) {
                System.out.println(json.getLang());
            }
        }
    }
}