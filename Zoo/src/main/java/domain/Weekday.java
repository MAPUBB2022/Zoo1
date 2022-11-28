package domain;

public enum Weekday {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

    private final int nr;
    Weekday(int nr) {
        this.nr = nr;
    }

    public int getNr() {
        return nr;
    }
}
