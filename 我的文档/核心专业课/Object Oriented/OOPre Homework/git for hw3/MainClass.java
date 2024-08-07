import java.math.BigInteger;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

public class MainClass {
    private static ArrayList<Adventurer> adventurers = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);
    private static HashMap<String, ArrayList<Adventurer>>
            version = new HashMap<>();

    public static void main(String[] args) {
        int n = sc.nextInt();
        String branch = "1";
        version.put("1", adventurers);
        for (int i = 0; i < n; i++) {
            int op = sc.nextInt();
            //System.out.println("!"+op);
            if (op == 1) {
                operation1(branch);
            } else if (op == 2) {
                operation2(branch);
            } else if (op == 3) {
                operation3(branch);
            } else if (op == 4) {
                operation4(branch);
            } else if (op == 5) {
                operation5(branch);
            } else if (op == 6) {
                operation6(branch);
            } else if (op == 7) {
                operation7(branch);
            } else if (op == 8) {
                operation8(branch);
            } else if (op == 9) {
                operation9(branch);
            } else if (op == 10) {
                operation10(branch);
            } else if (op == 11) {
                String oldbranch = branch;
                String branchName = sc.next();
                branch = branchName;
                operation11(branch, oldbranch);
            } else if (op == 12) {
                String branchName = sc.next();
                branch = branchName;
            }
        }
    }

    public static void operation1(String branch) {
        //System.out.println("1");
        int advId = sc.nextInt();
        String name = sc.next();
        Adventurer adv = new Adventurer();
        ArrayList<Commodity> commodities = new ArrayList<>();
        adv.setId(advId);
        adv.setName(name);
        adv.setCommodities(commodities);
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        adventurers1.add(adv);
    }

    public static void operation2(String branch) {
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
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                final ArrayList<Commodity> commoditiesNow = adventurer.getCommodities();
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
                    default:
                        break;
                }
                equipmentNew.setCommodity(price);
                equipmentNew.setBranch(branch);
                commoditiesNow.add(equipmentNew);
            }
        }
    }

    public static void operation3(String branch) {
        //System.out.println("3");
        int advId = sc.nextInt();
        int equipmentId = sc.nextInt();
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                ArrayList<Commodity> commoditiesNow = adventurer.getCommodities();
                for (int j = 0; j < commoditiesNow.size(); j++) {
                    if (commoditiesNow.get(j).getId() == equipmentId) {
                        Commodity commodityRemove = commoditiesNow.get(j);
                        commoditiesNow.remove(commodityRemove);
                    }
                }
            }
        }
    }

    public static void operation4(String branch) {
        int advId = sc.nextInt();
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                System.out.println(adventurer.getCommodity());
            }
        }
    }

    public static void operation5(String branch) {
        //System.out.println("5");
        int advId = sc.nextInt();
        BigInteger max = new BigInteger("0");
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                ArrayList<Commodity> commoditiesNow = adventurer.getCommodities();
                for (int j = 0; j < commoditiesNow.size(); j++) {
                    if (commoditiesNow.get(j).getCommodity().compareTo(max) == 1) {
                        BigInteger tmp = new BigInteger("0");
                        tmp = max;
                        max = commoditiesNow.get(j).getCommodity();
                    }
                }
            }
        }
        System.out.println(max);
    }

    public static void operation6(String branch) {
        //System.out.println("6");
        int advId = sc.nextInt();
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                ArrayList<Commodity> commoditiesNow = adventurer.getCommodities();
                System.out.println(commoditiesNow.size());
                break;
            }
        }
    }

    public static void operation7(String branch) {
        //System.out.println("7");
        int advId = sc.nextInt();
        int equipmentId = sc.nextInt();
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                ArrayList<Commodity> commoditiesNow = adventurer.getCommodities();
                for (int j = 0; j < commoditiesNow.size(); j++) {
                    if (commoditiesNow.get(j).getId() == equipmentId) {
                        System.out.println(commoditiesNow.get(j).toString());
                    }
                }
            }
        }
    }

    public static void operation8(String branch) {
        //System.out.println("!"+branch);
        int advId = sc.nextInt();
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                ArrayList<Commodity> commoditiesNow = adventurer.getCommodities();
                Collections.sort(commoditiesNow);
                for (Commodity commodity1 : commoditiesNow) {
                    adventurer.use(commodity1);
                }
            }
        }
    }

    public static void operation9(String branch) {
        //System.out.println("9");
        int advId = sc.nextInt();
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer : adventurers1) {
            if (adventurer.getId() == advId) {
                System.out.println(adventurer.toString());
            }
        }
    }

    public static void operation10(String branch) {
        int advId1 = sc.nextInt();
        int advId2 = sc.nextInt();
        ArrayList<Adventurer> adventurers1 = findVersion(branch);
        for (Adventurer adventurer1 : adventurers1) {
            if (adventurer1.getId() == advId1) {
                for (Adventurer adventurer2 : adventurers1) {
                    if (adventurer2.getId() == advId2) {
                        ArrayList<Commodity> commoditiesNow = adventurer1.getCommodities();
                        Commodity commodityNew = adventurer2;
                        commoditiesNow.add(commodityNew);
                    }
                }
            }
        }
    }

    public static void operation11(String branch, String oldBranch) {
        ArrayList<Adventurer> adventurers1 = findVersion(oldBranch);
        ArrayList<Adventurer> adventurersNew = new ArrayList<>();
        HashMap<Integer, Adventurer> idIsCloned = new HashMap<>();
        for (Adventurer adventurer : adventurers1) {
            Adventurer adventurerNew = new Adventurer();
            adventurerNew = cloneAdventurer(adventurer, branch, idIsCloned);
            adventurersNew.add(adventurerNew);
        }
        version.put(branch, adventurersNew);    //创立新版本
    }

    public static ArrayList<Adventurer> findVersion(String branch) {
        Set<String> keys = version.keySet();
        String target = "0";
        for (String key : keys) {
            if (key.equals(branch)) {
                target = key;
                break;
            }
        }
        return version.get(target);
    }

    public static Bottle cloneBottle(Bottle commodity) {
        Bottle bottleClone = new Bottle(commodity.getId(),
                commodity.getName(),
                commodity.getPrice(),
                commodity.getCapacity(),
                commodity.isFilled());
        return bottleClone;
    }

    public static ExpBottle cloneExpBottle(ExpBottle commodity) {
        ExpBottle expBottleClone = new ExpBottle(commodity.getId(),
                commodity.getName(),
                commodity.getPrice(),
                commodity.getCapacity(),
                commodity.isFilled(),
                commodity.getExpRatio());
        return expBottleClone;
    }

    public static HealingPotion cloneHealingPotion(HealingPotion commodity) {
        HealingPotion healingPotionClone = new HealingPotion(commodity.getId(),
                commodity.getName(),
                commodity.getPrice(),
                commodity.getCapacity(),
                commodity.isFilled(),
                commodity.getEfficiency());
        return healingPotionClone;
    }

    public static Sword cloneSword(Sword commodity) {
        Sword swordClone = new Sword(commodity.getId(),
                commodity.getName(),
                commodity.getPrice(),
                commodity.getSharpness());
        return swordClone;
    }

    public static RareSword cloneRareSword(RareSword commodity) {
        RareSword rareSwordClone = new RareSword(commodity.getId(),
                commodity.getName(),
                commodity.getPrice(),
                commodity.getSharpness(),
                commodity.getExtraExpBonus());
        return rareSwordClone;
    }

    public static EpicSword cloneEpicSword(EpicSword commodity) {
        EpicSword epicSwordClone = new EpicSword(commodity.getId(),
                commodity.getName(),
                commodity.getPrice(),
                commodity.getSharpness(),
                commodity.getEvolveRatio());
        return epicSwordClone;
    }

    public static Adventurer cloneAdventurer(Adventurer adventurer, String branch,
                                             HashMap<Integer, Adventurer> idIsCloned) {
        for (HashMap.Entry<Integer, Adventurer> entry : idIsCloned.entrySet()) {
            if (entry.getValue().getId() == adventurer.getId()) {
                return entry.getValue();
            }
        }
        ArrayList<Commodity> commoditiesClone = new ArrayList<>();
        ArrayList<Commodity> commoditiesNow = adventurer.getCommodities();
        for (Commodity commodity : commoditiesNow) {
            if (commodity instanceof ExpBottle) {
                ExpBottle commodityClone = new ExpBottle();
                commodityClone = cloneExpBottle((ExpBottle) commodity);
                commoditiesClone.add(commodityClone);
            } else if (commodity instanceof HealingPotion) {
                HealingPotion commodityClone = new HealingPotion();
                commodityClone = cloneHealingPotion((HealingPotion) commodity);
                commoditiesClone.add(commodityClone);
            } else if (commodity instanceof Bottle) {
                Bottle commodityClone = new Bottle();
                commodityClone = cloneBottle((Bottle) commodity);
                commoditiesClone.add(commodityClone);
            } else if (commodity instanceof RareSword) {
                RareSword commodityClone = new RareSword();
                commodityClone = cloneRareSword((RareSword) commodity);
                commoditiesClone.add(commodityClone);
            } else if (commodity instanceof EpicSword) {
                EpicSword commodityClone = new EpicSword();
                commodityClone = cloneEpicSword((EpicSword) commodity);
                commoditiesClone.add(commodityClone);
            } else if (commodity instanceof Sword) {
                Sword commodityClone = new Sword();
                commodityClone = cloneSword((Sword) commodity);
                commoditiesClone.add(commodityClone);
            } else if (commodity instanceof Adventurer) {
                Adventurer commodityClone = new Adventurer();
                commodityClone = cloneAdventurer((Adventurer) commodity, branch, idIsCloned);
                commoditiesClone.add(commodityClone);
            }
        }
        Adventurer adventurerClone = new Adventurer(adventurer.getId(),
                adventurer.getName(),
                adventurer.getCommodity(),
                commoditiesClone,
                adventurer.getHealth(),
                adventurer.getExp(),
                adventurer.getMoney());
        idIsCloned.put(adventurerClone.getId(), adventurerClone);
        return adventurerClone;
    }
}