import java.util.ArrayList;

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

    public boolean isEmpty() {
        return MEMBERS.isEmpty();
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
}
