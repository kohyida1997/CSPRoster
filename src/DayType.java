public enum DayType {

    WEEKDAY(1), FRISUN(1.5), SAT(2);

    double pointsWorth;

    DayType(double pointsWorth) {
        this.pointsWorth = pointsWorth;
    }

    double getPointsWorth() {
        return pointsWorth;
    }
}
