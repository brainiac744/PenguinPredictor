package net.pyromonkey.penguinpredictor;

public class Penguin implements Comparable<Penguin> {
    private String name;
    private int ordinal;

    public Penguin(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public String getName() {
        return name;
    }

    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Penguin o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return name.compareToIgnoreCase(o.getName());
    }
}
