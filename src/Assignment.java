import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Assignment {
    long daysLeftToAssign;
    LinkedHashMap<Day, HashSet<Person>> dayToDomain = new LinkedHashMap<>();
    HashMap<Integer, Day> dateToDayMap = new HashMap<>();
    Color color;
    List<Person> relevantPeople;

    Assignment(List<Day> days, Color color, List<Person> people) {
        relevantPeople = people;
        this.color = color;

        // does reduction of domain already

        System.out.println("Assignment Constructor: Days size = " + days.size());

        days.forEach(d -> {
            dayToDomain.put(d, new HashSet<>(getValidPeople(d)));
            dateToDayMap.put(d.date, d);
        });

        // blue only
        if (dateToDayMap.containsKey(-1) && dateToDayMap.containsKey(0)) {
            dateToDayMap.get(-1).isAssigned = true;
            dateToDayMap.get(-1).assignedHOs = new UnorderedPair(Main.jonP, Main.sarah);

            dateToDayMap.get(0).isAssigned = true;
            dateToDayMap.get(0).assignedHOs = new UnorderedPair(Main.cheryl, Main.hongRong);
        }

//        dateToDayMap.get(3).isAssigned = true;
//        dateToDayMap.get(3).assignedHOs = new UnorderedPair(Main.hongRong, Main.cheryl);
//
//        dateToDayMap.get(5).isAssigned = true;
//        dateToDayMap.get(5).assignedHOs = new UnorderedPair(Main.akira, Main.jonP);
//
//        dateToDayMap.get(8).isAssigned = true;
//        dateToDayMap.get(8).assignedHOs = new UnorderedPair(Main.hongRong, Main.cheryl);
//
//        dateToDayMap.get(9).isAssigned = true;
//        dateToDayMap.get(9).assignedHOs = new UnorderedPair(Main.jonP, Main.sarah);
//
//        dateToDayMap.get(12).isAssigned = true;
//        dateToDayMap.get(12).assignedHOs = new UnorderedPair(Main.sarah, Main.akira);
//
//        dateToDayMap.get(15).isAssigned = true;
//        dateToDayMap.get(15).assignedHOs = new UnorderedPair(Main.hongRong, Main.jonP);
//
//
//        dateToDayMap.get(18).isAssigned = true;
//        dateToDayMap.get(18).assignedHOs = new UnorderedPair(Main.sarah, Main.cheryl);
//
//
//        dateToDayMap.get(21).isAssigned = true;
//        dateToDayMap.get(21).assignedHOs = new UnorderedPair(Main.jonP, Main.akira);
//
//
//        dateToDayMap.get(23).isAssigned = true;
//        dateToDayMap.get(23).assignedHOs = new UnorderedPair(Main.cheryl, Main.hongRong);

        this.daysLeftToAssign = days.stream().filter(d -> !d.isAssigned).count();
        System.out.println("DAYS TO ASSIGN =====> " + days.stream().filter(d -> !d.isAssigned).count());
    }

    boolean isComplete() {
        return daysLeftToAssign == 0;
    }

    // domain reduction
    List<Person> getValidPeople(Day d) {
        return Main.peopleMap.values().stream()
//                .filter(p -> !d.primaryHO.equals(p))
                .filter(p -> d.secondaryHO == null || !d.secondaryHO.equals(p))
                .filter(p -> p.color == d.color)
                .filter(p -> !p.leaveDays.contains(d.date))
                .filter(p -> !p.leaveDays.contains(d.date + 1))
                .collect(Collectors.toList());
    }

    HashSet<UnorderedPair> generatePairs(Day d) {
        HashSet<UnorderedPair> pairs = new HashSet<>();
//        if (d == null) System.out.println("NULLLLL ");
        for (Person p : dayToDomain.get(d)) {
            for (Person q : dayToDomain.get(d)) {
                if (isIllegalPair(p, q)) continue;
                if (!p.equals(q)) pairs.add(new UnorderedPair(p, q));
            }
        }
//        pairs.forEach(System.out::println);
//        System.out.println("<==========GENERATEPAIR=======>");
        return pairs;
    }

    static boolean isIllegalPair(Person p, Person q) {
        String illegal1 = "HongRong";
        String illegal2 = "JonP";
        return (p.name.equals(illegal1) && q.name.equals(illegal2)) ||
                (q.name.equals(illegal1) && p.name.equals(illegal2));
    }

    public boolean isGoodEnough() {
        HashMap<Person, Integer> scores = new HashMap<>();
        relevantPeople.forEach(
                x -> scores.put(x, 0)
        );
        int threshold = 3;
        int sameDayThreshold = 3;
        int currMax = -1;
        // count number of only-2 rest days
        int maxDays = dayToDomain.keySet().stream().map(d -> d.date).mapToInt(x -> x).max().getAsInt();
        for (Day d : dayToDomain.keySet()) {
            if (d.color != this.color) continue;
            Person ho1 = d.assignedHOs.ho1;
            Person ho2 = d.assignedHOs.ho2;
            if (d.assignedHOs.contains(d.primaryHO) || d.assignedHOs.contains(d.secondaryHO)) sameDayThreshold--;
            int key = d.date + 3;
            if (key <= maxDays) {
               if (dateToDayMap.containsKey(key)) {
                   if (dateToDayMap.get(key).assignedHOs.contains(ho1)) {
                       int curr = scores.get(ho1) + 1;
                       scores.put(ho1, curr);
                       currMax = Math.max(currMax, curr);
                   }
                   if (dateToDayMap.get(key).assignedHOs.contains(ho2)) {
                       int curr = scores.get(ho2) + 1;
                       scores.put(ho2, curr);
                       currMax = Math.max(curr, currMax);
                   }
               }
            }
        }

        return currMax <= threshold && sameDayThreshold >= 0;
    }
}
