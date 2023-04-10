import java.util.ArrayList;

public class SeatingPlanHandler {
    private static final ArrayList<Table> TABLES = new ArrayList<>();
    private static final ArrayList<Person> PEOPLE = new ArrayList<>();

    private static final int MAX_TABLES = 10;
    private static final int MAX_PEOPLE = 200;

    public static void printPeople() {
        for (Person person : PEOPLE) {
            System.out.println(person);
        }
    }

    public static void addTable(Table table) {
        TABLES.add(table);
    }

    public static void removeTable(Table table) {
        TABLES.remove(table);
    }

    public static void addPerson(Person person) {
        // if the person isn't already in the list, add them
        if (!PEOPLE.contains(person)) {
            PEOPLE.add(person);
        }
    }

    public static Person getPerson(String name) {
        for (Person person : PEOPLE) {
            if (person.getName().equals(name)) {
                return person;
            }
        }
        throw new IllegalArgumentException("[ERORR] Person " + name + " does not exist!");
    }

    public static void removePerson(Person person) {
        PEOPLE.remove(person);
    }

    public static boolean personExists(String name) {
        for (Person person : PEOPLE) {
            if (person.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void printInactivePeople() {
        for (Person person : PEOPLE) {
            if (!person.isActive()) {
                System.out.println(person);
            }
        }
    }
}
