import java.util.ArrayList;

public class Table {
    private static final int MAX_SEATS = 10;
    private static final ArrayList<Person> SEATS = new ArrayList<>(MAX_SEATS);

    private final String letter;
    private final String name;

    public Table(String name) {
        this.name = name;
        this.letter = name.substring(0, 1);
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
        int emptySeats = 0;
        for (Person seat : SEATS) {
            if (seat == null) {
                emptySeats++;
            }
        }
        return emptySeats;
    }

    public boolean isFull() {
        return getNumberOfEmptySeats() == 0;
    }
}
