package domain;

public enum Weekday {
    Monday(1), Tuesday(2), Wednesday(3), Thursday(4), Friday(5), Saturday(6), Sunday(7);

    private final int nr;
    Weekday(int nr) {
        this.nr = nr;
    }

    public int getNr() {
        return nr;
    }
}
