public class Relationship {
    private Person person1;
    private Person person2;
    private int strength;
    private boolean isStrong;

    public Relationship(Person person1, Person person2, int strength, boolean isStrong) {
        this.person1 = person1;
        this.person2 = person2;
        this.strength = strength;
        this.isStrong = isStrong;
    }

    public Person getPerson1() {
        return person1;
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public boolean isStrong() {
        return isStrong;
    }

    public void setStrong(boolean strong) {
        isStrong = strong;
    }
}
