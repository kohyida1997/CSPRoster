import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static int count = 0;
    static Random random = new Random();

    static LinkedList<Day> dayList = new LinkedList<Day>();
    static LinkedHashMap<String, Person> peopleMap = new LinkedHashMap<>();
    // blue
    static Person akira = Person.getBluePerson("Akira");
    static Person sarah = Person.getBluePerson("Sarah");
    static Person hongRong = Person.getBluePerson("HongRong");
    static Person cheryl = Person.getBluePerson("Cheryl");
    static Person jonP = Person.getBluePerson("JonP");
    // green
    public static Person alice = Person.getGreenPerson("Alice");
    public static Person abhinav = Person.getGreenPerson("Abhinav");
    public static Person jonT = Person.getGreenPerson("JonT");
    static Person ivan = Person.getGreenPerson("Ivan");
    static Person claribel = Person.getGreenPerson("Claribel");
    static Person charmaine = Person.getGreenPerson("Charmaine");

    static Person dummyGreen = Person.getGreenPerson("dummyGreen");
    static Person dummyBlue = Person.getBluePerson("dummyBlue");


    public static long DAYS_TO_ASSIGN = 0;

    public static void main(String[] args) {
        initPeople();
        initDays();

        Color colorToAssign = Color.BLUE;
        System.out.println("TOTAL DAYS: " + dayList.size());

        List<Day> targetDays =  dayList.stream().filter(x -> x.color == colorToAssign).collect(Collectors.toList());
        List<Person> targetPeople = peopleMap.values().stream().filter(x -> x.color == colorToAssign).collect(Collectors.toList());

//        List<Day> greenOnly = dayList.stream().filter(x -> x.color == Color.GREEN).collect(Collectors.toList());
//        List<Day> blueOnly = dayList.stream().filter(x -> x.color == Color.BLUE).collect(Collectors.toList());
//        List<Person> bluePpl = peopleMap.values().stream().filter(x -> x.color == Color.BLUE).collect(Collectors.toList());
//        List<Person> greenPpl = peopleMap.values().stream().filter(x -> x.color == Color.GREEN).collect(Collectors.toList());

        Assignment ass = new Assignment(targetDays, Color.BLUE, targetPeople);
//        Assignment ass = new Assignment(greenOnly, Color.GREEN, greenPpl);

//        System.out.println(ass.daysLeftToAssign);
        Assignment res = backtrack(ass);
//        Assignment resb = backtrack(assb);
        if (res == null) {
            System.out.println("NO SOLUTION");
            System.exit(2);
        }
        List<Day> temp = new ArrayList<>(ass.dayToDomain.keySet());
//        List<Day> temp2 = new ArrayList<>(assb.dayToDomain.keySet());
//        temp.addAll(temp2);
        LinkedList<Day> combined = new LinkedList<Day>(temp);
        Collections.sort(combined, Comparator.comparingInt(x -> x.date));
        System.out.println("====== ALLOCATION ======");

        combined.forEach(k -> {
            if (k.color != colorToAssign) return;
            System.out.print(k.date + " ");
            System.out.print(k.assignedHOs.toString());
            if (k.assignedHOs.contains(k.primaryHO) || k.assignedHOs.contains(k.secondaryHO))
                System.out.print(" (HO of Con is also on Call)");
            System.out.println();
        });


        System.out.println("\n ====== POINTS ======");

        peopleMap.values().stream().forEach(p -> {
            if (p.color == colorToAssign)
                System.out.println(String.format("%s %s [%.1f points]", p.color, p.name, p.points));
        });

        // Print [Sum of (ith_points - ColorMean)] for all i in BLUE

        System.out.println("\n ====== FAIRNESS ======");

        double mean = targetPeople.stream().mapToDouble(x -> x.points).average().getAsDouble();
        double diffs = targetPeople.stream().mapToDouble(x -> Math.abs(x.points - mean)).sum();
        System.out.println("[Sum of |ith_points - ColorMean|] for all i persons in " +  colorToAssign + ": " + diffs);

    }

    static void initDays() {
        dayList.addAll(Arrays.asList(
                Day.getBlueDay(-1, DayType.WEEKDAY, sarah),
                Day.getBlueDay(0, DayType.WEEKDAY, cheryl),

                Day.getGreenDay(1, DayType.WEEKDAY, abhinav),
                Day.getBlueDay(2, DayType.FRISUN, cheryl),
                Day.getGreenDay(3, DayType.SAT, ivan),
                Day.getGreenDay(4, DayType.FRISUN, claribel),

                Day.getBlueDay(5, DayType.WEEKDAY, cheryl),
                Day.getBlueDay(6, DayType.WEEKDAY, sarah),
                Day.getGreenDay(7, DayType.WEEKDAY, claribel),
                Day.getBlueDay(8, DayType.WEEKDAY, cheryl),
                Day.getGreenDay(9, DayType.FRISUN, jonT),
                Day.getBlueDay(10, DayType.SAT, sarah),
                Day.getBlueDay(11, DayType.FRISUN, sarah),

                Day.getGreenDay(12, DayType.WEEKDAY, jonT),
                Day.getGreenDay(13, DayType.WEEKDAY, ivan),
                Day.getBlueDay(14, DayType.WEEKDAY, sarah),
                Day.getBlueDay(15, DayType.WEEKDAY, akira),
                Day.getGreenDay(16, DayType.FRISUN, ivan),
                Day.getBlueDay(17, DayType.SAT, cheryl),
                Day.getBlueDay(18, DayType.FRISUN, akira),

                Day.getGreenDay(19, DayType.WEEKDAY, abhinav),
                Day.getGreenDay(20, DayType.WEEKDAY, ivan),
                Day.getBlueDay(21, DayType.WEEKDAY, akira),
                Day.getGreenDay(22, DayType.WEEKDAY, abhinav),
                Day.getBlueDay(23, DayType.FRISUN, akira),
                Day.getGreenDay(24, DayType.SAT, jonT),
                Day.getBlueDay(25, DayType.FRISUN, cheryl),

                Day.getBlueDay(26, DayType.WEEKDAY, dummyBlue)

        ));
        DAYS_TO_ASSIGN = dayList.size();
    }

    static void initBluePeople() {
        // BLUE
        akira.setPoints(7.5 + 8.5);
        peopleMap.put(akira.name, akira);

        sarah.setPoints(8 + 7.5);
        sarah.setNcRequests(6, 10, 11);
        sarah.setLeaveDays(7, 8, 12, 13);
        peopleMap.put(sarah.name, sarah);

        hongRong.setNcRequests(10, 11);
        hongRong.setPoints(7 + 6.5);
        hongRong.setLeaveDays(9, 12);
        peopleMap.put(hongRong.name, hongRong);

        cheryl.setcRequests(11, 14, 18);
        cheryl.setPoints(7.5 + 7.5);
        cheryl.setNcRequests(20, 24, 25);
        cheryl.setLeaveDays(21, 22, 23, 26);
        peopleMap.put(cheryl.name, cheryl);

        jonP.setNcRequests(11, 14, 17, 18, 23);
        jonP.setLeaveDays(15, 16);
        jonP.setPoints(7 + 8);
        peopleMap.put(jonP.name, jonP);
    }

    static void initGreenPeople() {
        // GREEN


        abhinav.setLeaveDays(12, 22, 26);
        abhinav.setPoints(10 + 6.5);
        peopleMap.put(abhinav.name, abhinav);

        jonT.setcRequests(4, 22);
        jonT.setNcRequests(9);
        jonT.setPoints(11.5 + 5.5);
        peopleMap.put(jonT.name, jonT);

        ivan.setLeaveDays(1, 15);
        ivan.setPoints(9.5 + 6.5);
        peopleMap.put(ivan.name, ivan);

        charmaine.setcRequests(1, 4);
        charmaine.setNcRequests(22);
        charmaine.setPoints(9.5 + 5.5);
        charmaine.setLeaveDays(6, 7);
        peopleMap.put(charmaine.name, charmaine);

        claribel.setLeaveDays(9, 19);
        claribel.setNcRequests(3, 8, 20);
        claribel.setPoints(7.5 + 8.5);
        peopleMap.put(claribel.name, claribel);

    }

    static void initPeople() {
        initGreenPeople();
        initBluePeople();
    }

    static Person getPersonByName(String name) {
        return peopleMap.get(name);
    }

    static Assignment backtrack(Assignment ass) {
        if (ass.isComplete()) return ass;

        Day currToAssign = chooseDayToAssign(ass); // should never be null
        if (currToAssign == null) System.out.println("WTF");
        HashSet<UnorderedPair> domain = ass.generatePairs(currToAssign);
//        LinkedList<UnorderedPair> ordered = orderByCallRequest(orderByPoints(domain), currToAssign);
        LinkedList<UnorderedPair> ordered = orderByCallRequest(domain, currToAssign);
//        LinkedList<UnorderedPair> ordered = new LinkedList<>(domain);

        for (UnorderedPair p : ordered) {
            if (canAssignPair(p, ass, currToAssign)) {
                count++;
                System.out.println(count);
                // assign
                currToAssign.assignedHOs = p;
                currToAssign.isAssigned = true;
                ass.daysLeftToAssign--;

                p.addPointsToBoth(currToAssign.dayType.getPointsWorth());
                Assignment res = backtrack(ass);

                if (res != null && res.isGoodEnough()) {
                    return res;
                }

                currToAssign.assignedHOs = null;
                currToAssign.isAssigned = false;

                ass.daysLeftToAssign++;

                p.minusPointsToBoth(currToAssign.dayType.getPointsWorth());

            }

        }

        return null;
    }

    private static boolean canAssignPair(UnorderedPair p, Assignment ass, Day d) {

        Person p1 = p.ho1;
        Person p2 = p.ho2;

        int MINIMUM_DISPLACEMENT = 2;
        for (int i = 1; i <= MINIMUM_DISPLACEMENT; i++) {
            int queryDate = d.date - i;
            if (queryDate < 0) break; // CHANGE

            if (ass.dateToDayMap.containsKey(queryDate) &&
                    (ass.dateToDayMap.get(queryDate).assignedHOs.contains(p1) || ass.dateToDayMap.get(queryDate).assignedHOs.contains(p2)))
                return false;
        }
        return true;
    }

    private static Day chooseDayToAssign(Assignment ass) {
        for (Day d : ass.dayToDomain.keySet()) {
            if (!d.isAssigned) return d;
        }
        return null;
    }

    static LinkedList<UnorderedPair> orderByPoints(Collection<UnorderedPair> list) {
        LinkedList<UnorderedPair> toReturn = new LinkedList<>(list);
        toReturn.sort((o1, o2) -> o1.getAvgPoints() - o2.getAvgPoints() < 0 ? -99
                : o1.getAvgPoints() == o2.getAvgPoints() ? 1 - 2 * random.nextInt(2)
                : 99);
        return toReturn;
    }

    static LinkedList<UnorderedPair> orderByCallRequest(Collection<UnorderedPair> list, Day day) {
        LinkedList<UnorderedPair> toReturn = new LinkedList<>(list);
        toReturn.sort((o1, o2) -> {
            int score1 = getPairScore(o1, day);
            int score2 = getPairScore(o2, day);
            if (score1 == score2) return 1 - 2 * random.nextInt(2);
            else return Integer.compare(score1, score2);
        });
        return toReturn;
    }

    static int getPairScore(UnorderedPair x, Day d) {
        if (x.contains(d.primaryHO) || x.contains(d.secondaryHO)) return 99999; // very very bad
        if (x.hasCOnThisDate(d.date) && !x.hasNcOnThisDate(d.date)) return -99; // give!
        if (x.hasCOnThisDate(d.date) && x.hasNcOnThisDate(d.date)) return 999; // conflict
        if (!x.hasCOnThisDate(d.date) && x.hasNcOnThisDate(d.date)) return 99; // dont give
        return 1; // dont care
    }

}
