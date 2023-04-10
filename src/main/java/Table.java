import java.util.ArrayList;

public class Table {
    private static final int MAX_SEATS = 10;

    private final ArrayList<Person> SEATS = new ArrayList<>(MAX_SEATS);

    private final String letter;
    private final String name;

    public Table(String name) {
        this.name = name;
        this.letter = name.substring(0, 1);

        for (int i = 0; i < MAX_SEATS; i++) {
            SEATS.add(null);
        }
    }

    public String toString() {
        return String.format("Table %s: %s", name, SEATS);
    }

    public void printSeats() {
        for (int i = 0; i < MAX_SEATS; i++) {
            if (SEATS.get(i) == null) {
                System.out.printf("%s%d: Empty%n", letter, i);
            } else {
                System.out.printf("%s%d: %s%n", letter, i, SEATS.get(i).getName());
            }
        }
    }

    public String getLetter() {
        return letter;
    }

    public String getName() {
        return name;
    }

    public Person getSeat(int seatNumber) {
        return SEATS.get(seatNumber);
    }

    public void setSeat(int seatNumber, Person person) {
        if (!isSeatEmpty(seatNumber)) {
            System.out.printf("[WARN] Seat %s is already occupied!%n", letter + seatNumber);
        } else {
            SEATS.set(seatNumber, person);
            person.setSeat(letter + seatNumber);
        }
    }

    public void removeSeat(int seatNumber) {
        if (isSeatEmpty(seatNumber)) {
            System.out.printf("[WARN] Seat %s is already empty!%n", letter + seatNumber);
        } else {
            SEATS.get(seatNumber).setSeat(null);
            SEATS.set(seatNumber, null);
        }
    }

    public boolean isSeatEmpty(int seatNumber) {
        return SEATS.get(seatNumber) == null;
    }

    public int getNumberOfEmptySeats() {
        int emptySeats = MAX_SEATS;
        for (Person seat : SEATS) {
            if (seat != null) {
                emptySeats--;
            }
        }
        return emptySeats;
    }

    public boolean isFull() {
        return getNumberOfEmptySeats() == 0;
    }

    public void addGroup(Group group) {
        // add the group to the table
        // if the table is full, throw an exception
        for (Person person : group.getMembers()) {
            // if the person is already at a table, throw an exception
            if (person.getSeat() != null) {
                throw new IllegalArgumentException("[ERROR] Person " + person.getName() + " is already at a table!");
            }
            // if the table is full, throw an exception
            if (isFull()) {
                throw new IllegalArgumentException("[ERROR] Table " + name + " is full!");
            }
            // add the person to the table
            for (int i = 0; i < MAX_SEATS; i++) {
                if (isSeatEmpty(i)) {
                    setSeat(i, person);
                    person.setSeat(letter + i);
                    break;
                }
            }
        }
    }
}
