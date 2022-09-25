import java.io.*;
import java.util.*;
import java.util.Map.*;

public class Courses {
  protected String[] courses;

  protected HashSet<String> timings = new HashSet<String>();

  // [Course, Enrollment, Preferences]
  protected Vector<Vector<String>> preferences = new Vector<Vector<String>>();

  // Room -> Capacity
  protected HashMap<String, Integer> rooms = new HashMap<String, Integer>();

  // Time -> Course
  protected HashMap<String, String> courseTimings = new HashMap<String, String>();
  
  // Course -> Room
  protected HashMap<String, Integer> courseRooms = new HashMap<String, Integer>();

  // Courses Constructor : requires 2 input files path
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

      // Schedule Courses
      this.schedule();

      scan.close();
    } catch (IOException e) {
      // Error Handling
      System.out.println(e);
    }
  }

  // Output Result into File
  protected void outputFile() {
    try {
      FileWriter outputFile = new FileWriter("outputs/output.txt");
  
      outputFile.write("Course\t\tRoom\t\t\tTiming\n");
      for (Entry<String, String> entry : this.courseTimings.entrySet()) {
        String course = entry.getValue();
        String timing = entry.getKey();
        Integer room = (this.courseRooms.get(course));
        outputFile.write(course + "\t\t\t" + room + "\t\t\t" + timing + "\n");
      }

      outputFile.close();
    } catch(IOException io) {
      System.out.println("IOException occured");
    }
  }
  
  // Output Result into File
  protected void outputFileCsv() {
    try {
      FileWriter outputFileCsv = new FileWriter("outputs/output.csv");
  
      outputFileCsv.write("Course,Room,Timing\n");
      for (Entry<String, String> entry : this.courseTimings.entrySet()) {
        String course = entry.getValue();
        String timing = entry.getKey();
        Integer room = (this.courseRooms.get(course));
        outputFileCsv.write(course + "," + room + "," + timing + "\n");
      }

      outputFileCsv.close();
    } catch(IOException io) {
      System.out.println("IOException occured");
    }
  }

  // Schedule the Courses
  protected void schedule() {
    String[] prefsList;
    String course, availableTiming;
    boolean foundRoom = false;
    int enrollmentDiff = Integer.MAX_VALUE;
    HashSet<String> tempTimes = (HashSet<String>) timings.clone();

    for (Vector<String> row : this.preferences) {
      // Get course name
      course = row.get(0);

      // Find Suitable Room for Coures
      for (Entry<String, Integer> entry : this.rooms.entrySet()) {
        int enrollment = Integer.parseInt(row.get(1));
        
        if (entry.getValue() >= enrollment && enrollmentDiff > entry.getValue() - enrollment) {
          foundRoom = true;
          this.courseRooms.put(course, Integer.parseInt(entry.getKey().strip()));
        }
      }

      // Room not found
      if (!foundRoom) {
        System.out.println("Couldn't find room for course");
        return;
      }

      // Check if row has preference
      if (row.size() > 2) {
        // Split preference timings
        prefsList = row.get(2).split(",");

        // Check for available time according to preference
        availableTiming = getAvailable(prefsList);

        // Check if available course in found
        if (availableTiming.equals("")) {
          System.out.println("Couldn't find suicourseTimings allotment according to preferred timing for " + availableTiming);
          return;
        }

        // Allot timing for available course
        tempTimes.remove(availableTiming);
        this.courseTimings.put(availableTiming, course);
      } else {

        // Check if sufficient timings are available
        if (tempTimes.size() < 1) {
          System.out.println("Insufficient timings for alloted courses");
          return;
        }

        // Get first available timing
        availableTiming = tempTimes.iterator().next();

        // Assign timings
        tempTimes.remove(availableTiming);
        this.courseTimings.put(availableTiming, course);
      }
    }
  }

  // Get available timings
  protected String getAvailable(String[] prefsList) {
    // Search for available timing
    for (int i = 0; i < prefsList.length; i++) {
      // Found available timing
      if (!this.courseTimings.containsKey(prefsList[i]))

        // Cour
        return prefsList[i];
    }

    // Couldn't find preferred timing
    return "";
  }

  // Read Rooms from File
  protected void readRooms(Scanner scan) {
    String str;
    boolean exit = false;

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

  // Read Courses from File
  protected void readCourses(Scanner scan) {
    String str;

    // Get line with list of courses
    str = scan.nextLine();

    // Remove whitespaces and ;
    str = str.replace(";", "").replace(" ", "");

    // Split elements by ,
    courses = str.split(",");
  }

  // Read Times from File
  protected void readTimes(Scanner scan) {
    String str;

    // Get line with list of times
    str = scan.nextLine();

    // Remove whitespaces and ;
    str = str.replace(";", "").replace(" ", "");

    // Split elements by ,
    timings = new HashSet<String>(Arrays.asList(str.split(",")));
  }

  // Read Preferences from File
  protected void readPreferences(Scanner scan) {
    String str;

    // Skip First Line
    scan.nextLine();

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
    System.out.println("Rooms\n" + rooms);

    System.out.println("\nCourses");
    for (String course : courses)
      System.out.print("\"" + course + "\"" + ", ");

    System.out.println("\n\nTimings");
    for (String time : timings)
      System.out.print("\"" + time + "\"" + ", ");

    System.out.println("\n\nPreferences");
    for (Vector<String> prefs : preferences)
      System.out.print(prefs + ",");

    System.out.println("\n\nResult");
    System.out.println(courseTimings);

    // Cour
  }
}
