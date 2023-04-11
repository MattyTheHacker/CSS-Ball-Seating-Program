import java.util.ArrayList;

public class SeatingPlanHandler {
    private static final ArrayList<Table> TABLES = new ArrayList<>();
    private static final ArrayList<Person> PEOPLE = new ArrayList<>();


    private static final int MAX_TABLES = 20;
    private static final int MAX_PEOPLE = 200;

    public static void addTable(Table table) {
        TABLES.add(table);
    }

    public static void addPerson(Person person) {
        // if the person isn't already in the list, add them
        if (!PEOPLE.contains(person)) {
            PEOPLE.add(person);
        }
    }

    public static void printSeatingPlan() {
        // print the seating plan
        System.out.println("Seating Plan:");
        for (Table t : TABLES) {
            System.out.println(t.getName() + ": ");
            t.printSeats();
            System.out.println("==================================");
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

    public static boolean personExists(String name) {
        for (Person person : PEOPLE) {
            if (person.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Person> getPeople() {
        return PEOPLE;
    }

    private static void findATable(Group group) {
        // we want to prioritise filling empty tables first
        for (Table t : TABLES) {
            if (t.getNumberOfEmptySeats() == 10 && group.getMembers().size() <= 10) {
                t.addGroup(group);
                return;
            }
        }

        for (Table t : TABLES) {
            int groupSize = group.getMembers().size();
            int emptySeats = t.getNumberOfEmptySeats();
            if (emptySeats >= groupSize) {
                // group fits on table, add them
                t.addGroup(group);
                return;
            }
        }

        // split the group
        Group g1 = new Group(group.getName() + " 1");
        Group g2 = new Group(group.getName() + " 2");

        // add the first half of the group to g1
        for (int i = 0; i < group.getMembers().size() / 2; i++) {
            g1.addMember(group.getMembers().get(i));
        }

        // add the second half of the group to g2
        for (int i = group.getMembers().size() / 2; i < group.getMembers().size(); i++) {
            g2.addMember(group.getMembers().get(i));
        }

        // try to add the groups to the table
        findATable(g1);
        findATable(g2);

    }

    public static void generateSeatingPlan() {
        // we need to generate a seating plan, putting people together based on preferences
        // first split the people into groups based on their preferences
        // then put them into tables

        if (PEOPLE.size() > MAX_PEOPLE) {
            throw new RuntimeException("[ERROR] Too many people! Max people: " + MAX_PEOPLE);
        }

        // create groups
        GroupHandler.generateGroups();

        // use the table names enum and iterate up to the max number of tables
        for (int i = 0; i < MAX_TABLES; i++) {
            // create a new table
            Table table = new Table(TableNames.values()[i].toString());
            // add the table to the list of tables
            addTable(table);
        }

        // sort the groups by size
        GroupHandler.sortGroupsBySize();

        // remove duplicate members from groups
        GroupHandler.removeAllDuplicates();

        // sort groups alphabetically
        GroupHandler.sortAllGroupsAlphabetically();

        // generate relationships within groups
        GroupHandler.generateAllRelationships();

        // print groups
//        System.out.println("Groups:");
//        GroupHandler.printGroups();

        // print the relationships within the biggest group
        System.out.println("Relationships:");
        GroupHandler.getGroups().get(0).printRelationships();

        // iterate over the groups, check if they fit on a table, if not, split
        // first loop over all tables to see if they have space for the group
        // if they don't, split the group and try again
        for (Group g : GroupHandler.getGroups()) {
            // try to find a table for a group
            findATable(g);
        }

        // print the seating plan
//        printSeatingPlan();
    }
}
