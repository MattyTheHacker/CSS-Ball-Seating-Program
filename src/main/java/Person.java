public class Person {
    private final String name;

    private Person preference1;
    private Person preference2;

    private String seat;

    private boolean active;

    public Person(String name) {
        this.name = name;
        this.preference1 = null;
        this.preference2 = null;
        this.seat = null;
        this.active = false;
    }

    public String toString() {
        // print the person's name, their preferences, and their seat
        // if a preference is not set, print None
        // if a seat is not set, print None
        return String.format("%s(%s): %s, %s, %s",
                name,
                active ? "Active" : "Inactive",
                preference1 == null ? "No Preference 1" : preference1.getName(),
                preference2 == null ? "No Preference 2" : preference2.getName(),
                seat == null ? "No Seat" : seat);
    }

    public String getName() {
        return name;
    }

    public Person getPreference1() {
        return preference1;
    }

    public void setPreference1(Person preference1) {
        this.preference1 = preference1;
    }

    public Person getPreference2() {
        return preference2;
    }

    public void setPreference2(Person preference2) {
        this.preference2 = preference2;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        // seats should be in the format of "A1", "B2", etc.
        this.seat = seat;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
