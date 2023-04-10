import java.util.ArrayList;

public class SeatingPlanHandler {
    private static final ArrayList<Table> TABLES = new ArrayList<>();
    private static final ArrayList<Person> PEOPLE = new ArrayList<>();
    private static final ArrayList<Group> GROUPS = new ArrayList<>();

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

    public static void generateGroups(Person person, Group group) {
        // check if the person is already in a group
        if (group.contains(person)) {
            return;
        }

        for (Group g : GROUPS) {
            if (g.contains(person)) {
                return;
            }
        }

        // add the person to the group
        group.addMember(person);

        // if the person has a preference, add them to this group too
        if (person.getPreference1() != null) {
            generateGroups(person.getPreference1(), group);
        }
        if (person.getPreference2() != null) {
            generateGroups(person.getPreference2(), group);
        }
    }

    public static void generateSeatingPlan() {
        // we need to generate a seating plan, putting people together based on preferences
        // first split the people into groups based on their preferences
        // then put them into tables

        // we need to recursively explore connections between preferences to build groups
        // put every person into their own group, then recursively join groups where a person in a group has a preference in another group
        for (Person person : PEOPLE) {
            // check if the person is already in a group
            boolean inGroup = false;
            for (Group group : GROUPS) {
                if (group.contains(person)) {
                    inGroup = true;
                    break;
                }
            }
            if (!inGroup) {
                // the person isn't in a group, so we need to create a new group for them
                Group group = new Group(person.getName() + "'s Group");
                generateGroups(person, group);
                GROUPS.add(group);
            }
        }

        System.out.println("Groups:");
        for (Group group : GROUPS) {
            System.out.println(group);
        }
    }
}
