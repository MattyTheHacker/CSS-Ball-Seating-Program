import java.io.*;

public class Gephify {

    private static final String INPUT_FILE = "data/attendees.csv";
    private static final String OUTPUT_FILE = "data/gephi.csv";

    public static void main(String[] args) {
        System.out.println("Converting attendees.csv to Gephi format...");

        try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE)); BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            String line;

            writer.write("Source,Target\n");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String person = parts[0].trim();
                String pref1 = parts[1].trim();
                String pref2 = parts[2].trim();

                if (!(pref1.equals("N/A"))) {
                    writer.write(person + "," + pref1 + "\n");
                }

                if (!(pref2.equals("N/A"))) {
                    writer.write(person + "," + pref2 + "\n");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
