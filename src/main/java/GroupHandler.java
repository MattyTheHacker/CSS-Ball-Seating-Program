import java.util.ArrayList;
import java.util.Comparator;

public class GroupHandler {

    private static final ArrayList<Group> GROUPS = new ArrayList<>();

    public static void addGroup(Group group) {
        GROUPS.add(group);
    }

    public static void removeGroup(Group group) {
        // empty the group
        GROUPS.remove(group);
    }

    public static void removeAllDuplicates() {
        for (Group group : GROUPS) {
            group.removeDuplicates();
        }
    }

    public static void sortAllGroupsAlphabetically() {
        for (Group group : GROUPS) {
            group.sortMembersAlphabetically();
        }
    }

    public static ArrayList<Group> getGroups() {
        return GROUPS;
    }

    public static void sortGroupsBySize() {
        GROUPS.sort((g1, g2) -> g2.getMembers().size() - g1.getMembers().size());
    }

    public static void printGroups(){
        for (Group group : GROUPS) {
            System.out.print(group.getName() + "(" + group.getMembers().size() + ") - ");
            for (Person person : group.getMembers()) {
                System.out.print(person.getName() + ",");
            }
            System.out.println();
        }
    }

    public static void RecursivelyGenerateGroups(Person person, Group targetGroup) {
        // check if the person is already in a group
        if (targetGroup.contains(person)) {
            return;
        }

        boolean inGroup = false;

        for (Group otherGroup : GROUPS) {
            if (otherGroup.contains(person)) {
                // the person is already in a group, so we need to merge the groups
                otherGroup.getMembers().forEach(targetGroup::addMember);
                targetGroup.getMembers().forEach(otherGroup::addMember);
                removeGroup(otherGroup);
                break;
            }
        }

        targetGroup.addMember(person);

        // if the person has a preference, add them to this group too
        if (person.getPreference1() != null) {
            RecursivelyGenerateGroups(person.getPreference1(), targetGroup);
        }
        if (person.getPreference2() != null) {
            RecursivelyGenerateGroups(person.getPreference2(), targetGroup);
        }
    }

    public static void generateGroups() {
        // iterate over people, creating groups based on preferences
        for (Person person : SeatingPlanHandler.getPeople()) {
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
                addGroup(group);
                RecursivelyGenerateGroups(person, group);
            }
        }
    }
}
