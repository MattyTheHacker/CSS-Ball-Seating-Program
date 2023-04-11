import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Group {
    private final ArrayList<Person> MEMBERS = new ArrayList<>();

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
}
