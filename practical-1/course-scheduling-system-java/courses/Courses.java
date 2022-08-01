import java.io.*;
import java.util.*;

public class Courses {
  public String[] courses, times;
  public Vector<Vector<String>> preferences = new Vector<Vector<String>>();
  public HashMap<String, Integer> rooms = new HashMap<String, Integer>();

  // Courses Constructor : requires input files
  public Courses(String file1, String file2) {
    String section;

    try {
      // File containing rooms, courses and time
      Scanner scan = new Scanner(new FileReader(file1));

      while (scan.hasNextLine()) {
        // Read section
        section = scan.nextLine();

        // Read Rooms
        if (section.equals("rooms"))
          this.readRooms(scan);

        // Read Courses
        else if (section.equals("courses"))
          this.readCourses(scan);

        // Read Times
        else if (section.equals("times"))
          this.readTimes(scan);
      }

      // File containing preferences
      scan = new Scanner(new FileReader(file2));

      // Read Preferences
      this.readPreferences(scan);

      scan.close();
    } catch (IOException e) {
      // Error Handling
      System.out.println(e);
    }
  }

  public void readRooms(Scanner scan) {
    String str;
    Boolean exit = false;

    do {
      // Read line
      str = scan.nextLine();

      // Check if last line
      if (str.contains(";")) {
        // Replace ;
        str = str.replace(" ;", "");
        exit = true;
      }

      // Split String on ':' to get key and value pair
      String[] splitString = str.split(" : ");

      // Put key and value pair in HashMap
      rooms.put(splitString[0], Integer.parseInt(splitString[1]));
    } while (!exit);

  }

  public void readCourses(Scanner scan) {
    String str;

    // Get line with list of courses
    str = scan.nextLine();

    // Remove whitespaces and ;
    str = str.replace(";", "").replace(" ", "");

    // Split elements by ,
    courses = str.split(",");
  }

  public void readTimes(Scanner scan) {
    String str;

    // Get line with list of times
    str = scan.nextLine();

    // Remove whitespaces and ;
    str = str.replace(";", "").replace(" ", "");

    // Split elements by ,
    times = str.split(",");
  }

  public void readPreferences(Scanner scan) {
    String str;

    while (scan.hasNextLine()) {
      str = scan.nextLine();

      // Remove whitespaces
      str = str.replace(" ", "");

      // Split on '|'
      String[] prefs = str.split("\\|");

      // Append String[] to Preferences
      preferences.add(new Vector<String>(Arrays.asList(prefs)));
    }
  }

  // Debugging
  public void printData() {
    System.out.println(rooms);

    for (String course : courses)
      System.out.println(course);

    for (String time : times)
      System.out.println(time);

    for (Vector<String> prefs : preferences)
      System.out.println(prefs);
  }
}
