import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    // location of csv: data/attendees.csv
    private static final String FILENAME = "data/attendees.csv";

    // format of csv: name, preference1, preference2

    public static boolean fileExists(String filename) {
        return new File(filename).isFile();
    }

    public static void getData() {
        // open the file and create a Person for each entry
        // add the Person to the PEOPLE ArrayList in SeatingPlanHandler
        if (fileExists(FILENAME)) {
            // read the file
            try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
                String line;
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    // check how many arguments and set booleans to true
                    boolean preference1Exists = false;
                    boolean preference2Exists = false;

                    if (data.length == 3) {
                        preference1Exists = true;
                        preference2Exists = true;
                    } else if (data.length == 2) {
                        preference1Exists = true;
                    }


                    // check if the person exists
                    if (SeatingPlanHandler.personExists(data[0])) {
                        // the attendee exists, set them to active
                        Person attendee = SeatingPlanHandler.getPerson(data[0]);
                        attendee.setActive(true);
                    } else {
                        // the attendee does not exist, create them
                        Person attendee = new Person(data[0], true);
                        SeatingPlanHandler.addPerson(attendee);
                    }

                    Person attendee = SeatingPlanHandler.getPerson(data[0]);

                    if (preference1Exists) {
                        // first preference is given, check if they exist
                        if (SeatingPlanHandler.personExists(data[1])) {
                            // the person exists, set the preference
                            Person preference1 = SeatingPlanHandler.getPerson(data[1]);
                            attendee.setPreference1(preference1);
                        } else {
                            // the person does not exist, create them
                            Person preference1 = new Person(data[1], false);
                            attendee.setPreference1(preference1);
                        }
                    }

                    if (preference2Exists) {
                        // second preference is given, check if they exist
                        if (SeatingPlanHandler.personExists(data[2])) {
                            // the person exists, set the preference
                            Person preference2 = SeatingPlanHandler.getPerson(data[2]);
                            attendee.setPreference2(preference2);
                        } else {
                            // the person does not exist, create them
                            Person preference2 = new Person(data[2], false);
                            attendee.setPreference2(preference2);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("[ERROR] Could not read file!");
            }
        } else {
            System.out.printf("[ERROR] File %s not found!%n", FILENAME);
        }
    }
}