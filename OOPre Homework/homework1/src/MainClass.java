import java.math.BigDecimal;
import java.math.BigInteger;
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
            }
        }
    }

    public static void operation1() {
        //System.out.println("1");
        //Scanner sc=new Scanner(System.in);
        int advId = sc.nextInt();
        String name = sc.next();
        Adventurer adv = new Adventurer();
        ArrayList<Equipment> equipments = new ArrayList<>();
        adv.setId(advId);
        adv.setName(name);
        adv.setEquipments(equipments);
        adventurers.add(adv);
    }

    public static void operation2() {
        //System.out.println("2");
        int advId = sc.nextInt();
        int equipmentType = sc.nextInt();
        int id = sc.nextInt();
        String name = sc.next();
        BigInteger price = sc.nextBigInteger();
        double efficiency = 0;
        double expRatio = 0;
        double sharpness = 0;
        double extraExpBonus = 0;
        double evolveRatio = 0;
        double capacity = 0;
        Equipment equipmentNew = new Equipment();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Equipment> equipmentsNow = item.getEquipments();
                switch (equipmentType) {
                    case 1:
                        capacity = sc.nextDouble();
                        equipmentNew = new Bottle(id, name, price, capacity, true);
                        break;
                    case 2:
                        capacity = sc.nextDouble();
                        efficiency = sc.nextDouble();
                        equipmentNew = new HealingPotion(id, name, price,
                                capacity, true, efficiency);
                        break;
                    case 3:
                        capacity = sc.nextDouble();
                        expRatio = sc.nextDouble();
                        equipmentNew = new ExpBottle(id, name, price, capacity, true, expRatio);
                        break;
                    case 4:
                        sharpness = sc.nextDouble();
                        equipmentNew = new Sword(id, name, price, sharpness);
                        break;
                    case 5:
                        sharpness = sc.nextDouble();
                        extraExpBonus = sc.nextDouble();
                        equipmentNew = new RareSword(id, name, price, sharpness, extraExpBonus);
                        break;
                    case 6:
                        sharpness = sc.nextDouble();
                        evolveRatio = sc.nextDouble();
                        equipmentNew = new EpicSword(id, name, price, sharpness, evolveRatio);
                        break;
                    default: break;
                }
                equipmentsNow.add(equipmentNew);
                item.setEquipments(equipmentsNow);
            }
        }
    }

    public static void operation3() {
        //System.out.println("3");
        int advId = sc.nextInt();
        int equipmentId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Equipment> equipmentsNow = adventurers.get(i).getEquipments();
                for (int j = 0; j < equipmentsNow.size(); j++) {
                    if (equipmentsNow.get(j).getId() == equipmentId) {
                        Equipment equipmentRemove = equipmentsNow.get(j);
                        equipmentsNow.remove(equipmentRemove);
                        adventurers.get(i).setEquipments(equipmentsNow);
                    }
                }
            }
        }
    }

    public static void operation4() {
        int advId = sc.nextInt();
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Equipment> equipmentsNow = adventurers.get(i).getEquipments();
                for (int j = 0; j < equipmentsNow.size(); j++) {
                    BigDecimal num = new BigDecimal(equipmentsNow.get(j).getPrice());
                    sum = sum.add(num);
                }
            }
        }
        System.out.println(sum);
    }

    public static void operation5() {
        //System.out.println("5");
        int advId = sc.nextInt();
        BigInteger max = new BigInteger("0");
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Equipment> equipmentsNow = adventurers.get(i).getEquipments();
                for (int j = 0; j < equipmentsNow.size(); j++) {
                    if (equipmentsNow.get(j).getPrice().compareTo(max) == 1) {
                        BigInteger tmp = new BigInteger("0");
                        tmp = max;
                        max = equipmentsNow.get(j).getPrice();
                    }
                }
            }
        }
        System.out.println(max);
    }

    public static void operation6() {
        //System.out.println("6");
        int advId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Equipment> equipmentsNow = adventurers.get(i).getEquipments();
                System.out.println(equipmentsNow.size());
                break;
            }
        }
    }

    public static void operation7() {
        //System.out.println("7");
        int advId = sc.nextInt();
        int equipmentId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Equipment> equipmentsNow = adventurers.get(i).getEquipments();
                for (int j = 0; j < equipmentsNow.size(); j++) {
                    if (equipmentsNow.get(j).getId() == equipmentId) {
                        System.out.println(equipmentsNow.get(j).toString());
                    }
                }
            }
        }
    }

    public static void operation8() {
        //System.out.println("8");
        int advId = sc.nextInt();
        int equipmentId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                ArrayList<Equipment> equipmentsNow = adventurers.get(i).getEquipments();
                for (int j = 0; j < equipmentsNow.size(); j++) {
                    if (equipmentsNow.get(j).getId() == equipmentId) {
                        adventurers.get(i).use(equipmentsNow.get(j));
                    }
                }
            }
        }
    }

    public static void operation9() {
        //System.out.println("9");
        int advId = sc.nextInt();
        for (int i = 0; i < adventurers.size(); i++) {
            if (adventurers.get(i).getId() == advId) {
                System.out.println(adventurers.get(i).toString());
            }
        }
    }
}
