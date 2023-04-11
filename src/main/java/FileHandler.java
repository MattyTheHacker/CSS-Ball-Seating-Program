import java.io.*;

public class FileHandler {
    // location of csv: data/attendees.csv
    private static final String FILENAME = "data/attendees.csv";

    // format of csv: name, preference1, preference2

    public static boolean fileExists(String filename) {
        return new File(filename).isFile();
    }

    public static String sanitiseFilename(String filename) {
        // remove any illegal characters from the filename
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public static void saveCSV(String content, String filename) {
        // take in a string in the format of a csv and save it to a file
        // if the file exists, overwrite it
        // if the file does not exist, create it
        String sanitisedFilename = sanitiseFilename(filename);
        String filepath = "data/" + sanitisedFilename;

        try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(filepath))) {
            // save the content to the file
            writer.write(content.getBytes());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        Person attendee = new Person(data[0]);
                        SeatingPlanHandler.addPerson(attendee);
                        attendee.setActive(true);
                    }

                    Person attendee = SeatingPlanHandler.getPerson(data[0]);

                    if (preference1Exists) {
                        // first preference is given, check if they exist
                        if (SeatingPlanHandler.personExists(data[1])) {
                            // the person exists, set the preference
                            Person preference1 = SeatingPlanHandler.getPerson(data[1]);
                            attendee.setPreference1(preference1);
                            SeatingPlanHandler.addPerson(preference1);
                        } else {
                            // the person does not exist, create them
                            Person preference1 = new Person(data[1]);
                            attendee.setPreference1(preference1);
                            SeatingPlanHandler.addPerson(preference1);
                        }
                    }

                    if (preference2Exists) {
                        // second preference is given, check if they exist
                        if (SeatingPlanHandler.personExists(data[2])) {
                            // the person exists, set the preference
                            Person preference2 = SeatingPlanHandler.getPerson(data[2]);
                            attendee.setPreference2(preference2);
                            SeatingPlanHandler.addPerson(preference2);
                        } else {
                            // the person does not exist, create them
                            Person preference2 = new Person(data[2]);
                            attendee.setPreference2(preference2);
                            SeatingPlanHandler.addPerson(preference2);
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