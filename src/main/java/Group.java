import java.util.*;

public class Group {
    private final ArrayList<Person> MEMBERS = new ArrayList<>();
    private int[][] RELATIONSHIPS;

    private final String name;

    public Group(String name) {
        this.name = name;
    }

    public void addMember(Person person) {
        MEMBERS.add(person);
    }

    public void removeMember(Person person) {
        MEMBERS.remove(person);
    }

    public void removeDuplicates() {
        // remove duplicate people, using a set
        Set<Person> set = new HashSet<>(MEMBERS);
        MEMBERS.clear();
        MEMBERS.addAll(set);
    }

    public void printRelationships() {
        // print relationships in the form of a table
        // first row and column are the names of the people
        // the rest of the table is the relationships matrix
        StringBuilder sb = new StringBuilder();
        for (Person member : MEMBERS) {
            sb.append("\t").append(member.getName());
        }
        sb.append("\n");
        for (int i = 0; i < MEMBERS.size(); i++) {
            sb.append(MEMBERS.get(i).getName());
            for (int j = 0; j < MEMBERS.size(); j++) {
                sb.append("\t");
                sb.append(RELATIONSHIPS[i][j]);
            }
            sb.append("\n");
        }

        // convert tabs to commas and save to csv
        String csv = sb.toString().replace("\t", ",");
        FileHandler.saveCSV(csv, "data/relationships.csv");

    }

    public void generateRelationships() {
        // initialise the relationships matrix
        RELATIONSHIPS = new int[MEMBERS.size()][MEMBERS.size()];

        // if there are less than 2 people in the group, there are no relationships
        if (MEMBERS.size() < 2) {
            // empty the relationships matrix
            for (int[] relationship : RELATIONSHIPS) {
                Arrays.fill(relationship, -1);
            }
        }

        // set the diagonal to -1
        for (int i = 0; i < MEMBERS.size(); i++) {
            RELATIONSHIPS[i][i] = -1;
        }

        // for each person in the group, if they have selected someone as a preference, increment relationship by 1
        for (Person person : MEMBERS) {
            ArrayList<Person> preferences = person.getPreferences();
            if (preferences.size() > 0) {
                for (Person preference : preferences) {
                    if (MEMBERS.contains(preference)) {
                        // increment the relationship
                        RELATIONSHIPS[MEMBERS.indexOf(person)][MEMBERS.indexOf(preference)]++;
                    }

                    // if two people have put each other as preferences, set to 7
                    if (preference.getPreferences().contains(person)) {
                        RELATIONSHIPS[MEMBERS.indexOf(person)][MEMBERS.indexOf(preference)] = 7;
                    }
                }
            }
        }
    }

    public int getRelationshipBetweenTwoPeople(Person person1, Person person2) {
        // check if both people are in the group
        if (!MEMBERS.contains(person1) || !MEMBERS.contains(person2)) {
            return -1;
        } else {
            return RELATIONSHIPS[MEMBERS.indexOf(person1)][MEMBERS.indexOf(person2)];
        }
    }

    public void sortMembersAlphabetically() {
        MEMBERS.sort(Comparator.comparing(Person::getName));
    }

    public ArrayList<Person> getMembers() {
        return MEMBERS;
    }

    public boolean contains(Person person) {
        return MEMBERS.contains(person);
    }

    public String toString() {
        return String.format("Group %s: %s", name, MEMBERS);
    }

    public String getName() {
        return name;
    }

    public int getNumberOfMembers() {
        return MEMBERS.size();
    }

    public void empty() {
        MEMBERS.clear();
    }
}
