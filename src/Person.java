import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Person {
    String name;
    HashSet<Integer> cRequests = new HashSet<>();
    HashSet<Integer> ncRequests = new HashSet<>();
    HashSet<Integer> leaveDays = new HashSet<>();
    double points = 0.0;
    Color color;

    Person(String name, Color color) {
        this.name  = name;
        this.color = color;
    }

    void setcRequests(int... days) {
        cRequests.addAll(Arrays.stream(days).boxed().collect(Collectors.toList()));
    }

    void setNcRequests(int... days) {
        ncRequests.addAll(Arrays.stream(days).boxed().collect(Collectors.toList()));
    }

    void setLeaveDays(int... days) {
        leaveDays.addAll(Arrays.stream(days).boxed().collect(Collectors.toList()));
    }

    public boolean nameIsEqualTo(String name) {
        return this.name.equals(name);
    }

    static Person getGreenPerson(String name) {
        return new Person(name, Color.GREEN);
    }

    static Person getBluePerson(String name) {
        return new Person(name, Color.BLUE);
    }

    void setPoints(double points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        Person o = (Person) other;
        return o.name.equals(this.name);
    }

}
