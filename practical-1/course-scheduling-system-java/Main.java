import java.util.*;
import courses.*;

public class Main {
  public static void main(String[] args) {
    Courses courses = new Courses("inputs/input1.txt", "inputs/input2.txt");

    courses.printData();
  }
}