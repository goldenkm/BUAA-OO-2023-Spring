import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    private static ArrayList<Adventurer> adventurers = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            int op = sc.nextInt();
            if (op == 1) {
                operation1();
            } else if (op == 2) {
                operation2();
            } else if (op == 3) {
                operation3();
            } else if (op == 4) {
                operation4();
            } else if (op == 5) {
                operation5();
            } else if (op == 6) {
                operation6();
            } else if (op == 7) {
                operation7();
            } else if (op == 8) {
                operation8();
            } else if (op == 9) {
                operation9();
            } else if (op == 10) {
                operation10();
            } else if (op == 11) {
                operation11();
            } else if (op == 12) {
                operation12();
            }
        }
    }

    public static void operation1() {
        //System.out.println("1");
        //Scanner sc=new Scanner(System.in);
        int advId = sc.nextInt();
        String name = sc.next();
        Adventurer adv = new Adventurer();
        ArrayList<Bottle> bottles = new ArrayList<>();
        adv.setId(advId);
        adv.setName(name);
        adv.setBottles(bottles);
        adventurers.add(adv);
    }

    public static void operation2() {
        //System.out.println("2");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        String name = sc.next();
        long price = sc.nextLong();
        double capacity = sc.nextDouble();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                Bottle bottleNew = new Bottle(botId, name, price, capacity, true);
                ArrayList<Bottle> bottleNow = item.getBottles();
                bottleNow.add(bottleNew);
                item.setBottles(bottleNow);
            }
        }
    }

    public static void operation3() {
        //System.out.println("3");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        Bottle bottleRemove = bottlesNow.get(j);
                        bottlesNow.remove(bottleRemove);
                        adventurers.get(i).setBottles(bottlesNow);
                    }
                }
            }
        }
    }

    public static void operation4() {
        //System.out.println("4");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        long price = sc.nextLong();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        Bottle bottleChangePrice = bottlesNow.get(j);
                        bottleChangePrice.setPrice(price);
                        adventurers.get(i).setBottles(bottlesNow);
                    }
                }
            }
        }
    }

    public static void operation5() {
        //System.out.println("5");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        //System.out.println(botId);
        Boolean filled = sc.nextBoolean();
        //System.out.println(filled);
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        Bottle bottleFilled = bottlesNow.get(j);
                        bottleFilled.setFilled(filled);
                        adventurers.get(i).setBottles(bottlesNow);
                    }
                }
            }
        }
    }

    public static void operation6() {
        //System.out.println("6");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            //System.out.println(adventurers.get(i).getId());
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        System.out.println(bottlesNow.get(j).getName());
                    }
                }
            }
        }
    }

    public static void operation7() {
        //System.out.println("7");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        System.out.println(bottlesNow.get(j).getPrice());
                    }
                }
            }
        }
    }

    public static void operation8() {
        //System.out.println("8");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        System.out.println(bottlesNow.get(j).getCapacity());
                    }
                }
            }
        }
    }

    public static void operation9() {
        //System.out.println("9");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        System.out.println(bottlesNow.get(j).isFilled());
                    }
                }
            }
        }
    }

    public static void operation10() {
        //System.out.println("10");
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getId() == botId) {
                        System.out.println("The bottle's id is " + bottlesNow.get(j).getId() +
                                ", name is " + bottlesNow.get(j).getName() +
                                ", capacity is " + bottlesNow.get(j).getCapacity() +
                                ", filled is " + bottlesNow.get(j).isFilled() + ".");
                    }
                }
            }
        }
    }

    public static void operation11() {
        //System.out.println("11");
        int advId = sc.nextInt();
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    BigDecimal num = new BigDecimal(bottlesNow.get(j).getPrice());
                    sum = sum.add(num);
                }
            }
        }
        System.out.println(sum);
    }

    public static void operation12() {
        //System.out.println("12");
        int advId = sc.nextInt();
        long max = 0;
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Bottle> bottlesNow = adventurers.get(i).getBottles();
                for (int j = 0; j < bottlesNow.size(); j++) {
                    if (bottlesNow.get(j).getPrice() > max) {
                        long tmp;
                        tmp = max;
                        max = bottlesNow.get(j).getPrice();
                    }
                }
            }
        }
        System.out.println(max);
    }
}
