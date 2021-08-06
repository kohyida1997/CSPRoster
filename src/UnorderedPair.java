import java.util.Arrays;

public class UnorderedPair {
    Person ho1;
    Person ho2;

    UnorderedPair(Person ho1, Person ho2) {
        this.ho1 = ho1;
        this.ho2 = ho2;
    }

    @Override
    public boolean equals(Object other) {
        UnorderedPair o = (UnorderedPair) other;
        boolean f1 = o.ho1 == this.ho1 && o.ho2 == this.ho2;
        boolean f2 = o.ho1 == this.ho2 && o.ho2 == this.ho1;
        return f1 || f2;
    }

    @Override
    public int hashCode() {
        String[] arr = {ho1.name, ho2.name};
        Arrays.sort(arr);
        return arr[0].concat(arr[1]).hashCode();
    }



    public boolean hasNcOnThisDate(int date) {
        return ho1.ncRequests.contains(date) || ho2.ncRequests.contains(date);
    }

    public boolean hasCOnThisDate(int date) {
        return ho1.cRequests.contains(date) || ho2.cRequests.contains(date);
    }

    public String toString() {
        return String.format("[%s, %s]", ho1.name, ho2.name);
    }

    public void addPointsToBoth(double points) {
        ho1.points += points;
        ho2.points += points;
    }

    public void minusPointsToBoth(double points) {
        ho1.points -= points;
        ho2.points -= points;
    }

    public double getAvgPoints() {
        return (ho1.points + ho2.points) / 2;
    }

    boolean contains(Person p) {
        if (p == null) return false;
        return p.equals(ho1) || p.equals(ho2);
    }
}
