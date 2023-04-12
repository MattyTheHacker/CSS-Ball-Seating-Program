import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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

        if (group.getMembers().size() == 1) {
            System.out.println("[DEBUG] Group " + group.getName() + " has " + group.getMembers().size() + " members");
            System.out.println("[DEBUG] Members: " + group.getMembers());
            System.out.println("[DEBUG] Things have gone very wrong! Exiting...");
            System.exit(1);
        }

        // split the group
        Group g1 = new Group(group.getName() + " 1");
        Group g2 = new Group(group.getName() + " 2");

        // add the groups to the split groups list
        GroupHandler.addSplitGroup(g1);
        GroupHandler.addSplitGroup(g2);

        boolean splitSuccess = false;

        // loop over everyone in the group and add both people to the list with their relationship strength
        // check if it's a strong connection, if so - do not add them to the list
        // also add the strength of the relationship to the list
        ArrayList<Relationship> relationshipList = new ArrayList<>();

        for (Person p1 : group.getMembers()) {
            for (Person p2 : group.getMembers()) {
                if (!p1.equals(p2)) {
                    if (group.isDirectConnection(p1, p2)) {
                        if (!group.isStrongConnection(p1, p2)) {
                            int relationship = group.getRelationshipBetweenTwoPeople(p1, p2) + group.getRelationshipBetweenTwoPeople(p2, p1);
                            relationshipList.add(new Relationship(p1, p2, relationship, false));
                        }
                    }
                }
            }
        }

        // sort the list by relationship strength with weakest first
        relationshipList.sort(Comparator.comparingInt(Relationship::getStrength));

        for (Relationship r : relationshipList) {
            if (GroupHandler.checkGroupSplit(r.getPerson1(), r.getPerson2())) {
                g1.addMember(r.getPerson1());
                g2.addMember(r.getPerson2());

                ArrayList<Person> tmp1 = new ArrayList<>();
                tmp1.add(r.getPerson1());

                ArrayList<Person> tmp2 = new ArrayList<>();
                tmp2.add(r.getPerson2());

                ArrayList<Person> p1Preferences = GroupHandler.recursivelyGetAllPreferencesExcept(r.getPerson1(), tmp1, r.getPerson2());
                ArrayList<Person> p2Preferences = GroupHandler.recursivelyGetAllPreferencesExcept(r.getPerson2(), tmp2, r.getPerson1());

                Set<Person> p1Set = new HashSet<>(p1Preferences);
                Set<Person> p2Set = new HashSet<>(p2Preferences);

                for (Person p : p1Set) {
                    if (!g1.getMembers().contains(p)) {
                        g1.addMember(p);
                    }
                }

                for (Person p : p2Set) {
                    if (!g2.getMembers().contains(p)) {
                        g2.addMember(p);
                    }
                }

                group.empty();

                splitSuccess = true;

                System.out.println("[DEBUG] Split Group " + group.getName() + " on " + r.getPerson1().getName() + " and " + r.getPerson2().getName() + "!");

                break;
            } else {
                System.out.println("[DEBUG] Failed to split group " + group.getName() + " on " + r.getPerson1().getName() + " and " + r.getPerson2().getName() + "!");
            }
        }

        if (!splitSuccess) {
            System.out.println("[ERROR] Could not split group " + group.getName() + "!");
            System.out.println("[DEBUG] Relationships: ");
            for (Relationship r : relationshipList) {
                System.out.println(r.getPerson1().getName() + " - " + r.getPerson2().getName() + " : " + r.getStrength());
            }
            System.exit(1);
        }


        // empty the old group
        group.empty();

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
    }
}
