import java.util.HashSet;

public class Day {

    int date;
    Color color;
    Person primaryHO;
    Person secondaryHO; // sometimes null
    UnorderedPair assignedHOs = null;
    DayType dayType;
    boolean isAssigned = false;

    Day(int date, DayType dayType, Color color, Person primaryHO) {
        this.color = color;
        this.primaryHO = primaryHO;
        this.date = date;
        this.dayType = dayType;
    }

    Day(int date, DayType dayType, Color color, Person primaryHO, Person secondaryHO) {
        this.color = color;
        this.primaryHO = primaryHO;
        this.date = date;
        this.dayType = dayType;
        this.secondaryHO = secondaryHO;
    }

    static Day getGreenDay(int date, DayType dayType, Person primaryHO) {
        return new Day(date, dayType, Color.GREEN, primaryHO);
    }

    static Day getBlueDay(int date, DayType dayType, Person primaryHO) {
        return new Day(date, dayType, Color.BLUE, primaryHO);
    }

    static Day getGreenDay(int date, DayType dayType, Person primaryHO, Person secondaryHO) {
        return new Day(date, dayType, Color.GREEN, primaryHO, secondaryHO);
    }


    public String toString() {
        if (secondaryHO != null) {
            return String.format("[June %d]: %s, %s, %s/%s",
                    date, dayType, color, primaryHO.name, secondaryHO.name);
        }

        return String.format("[June %d]: %s, %s, %s",
                date, dayType, color, primaryHO.name);
    }

}
