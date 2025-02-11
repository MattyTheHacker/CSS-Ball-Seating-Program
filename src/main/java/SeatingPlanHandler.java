import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SeatingPlanHandler {
    private static final ArrayList<Table> TABLES = new ArrayList<>();
    private static final ArrayList<Person> PEOPLE = new ArrayList<>();

    private static final int MAX_TABLES = 26;
    private static final int MAX_PEOPLE = 260;

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
        throw new IllegalArgumentException("[ERROR] Person " + name + " does not exist!");
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
        // check if the group actually fits on any table (<10)
        if (group.getNumberOfMembers() <= 10) {
            // we want to prioritise filling empty tables first
            for (Table t : TABLES) {
                if (t.getNumberOfEmptySeats() == 10) {
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
        }
        System.out.println("Group: " + group.getName() + " is too big for a table!");
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

        // remove duplicate members from groups
        GroupHandler.removeAllDuplicates();

        // sort the groups by size
        GroupHandler.sortGroupsBySize();

        // sort groups alphabetically
        GroupHandler.sortAllGroupsAlphabetically();

        // print groups
        System.out.println("Groups:");
        GroupHandler.printGroups();

        // generate relationships within groups
        GroupHandler.generateAllRelationships();

        // print the relationships within the biggest group
        GroupHandler.printAllRelationships();

        // iterate over the groups, check if they fit on a table, if not, split
        // first loop over all tables to see if they have space for the group
        // if they don't, split the group and try again
        for (Group g : GroupHandler.getGroups()) {
            // try to find a table for a group
            findATable(g);
        }

        for (Group g : GroupHandler.getSplitGroups()) {
            // add split groups to the primary groups list
            GroupHandler.addGroup(g);
        }

        // empty the split groups list
        GroupHandler.emptySplitGroups();

        // remove empty groups from the list
        GroupHandler.removeEmptyGroups();

        // print the seating plan
        printSeatingPlan();

        // has anyone not been seated?
        for (Person p : PEOPLE) {
            if (p.getSeat() == null) {
                System.out.println("[DEBUG] " + p.getName() + " in group " + p.getGroup() + " has not been seated!");
            }
        }
    }
}
