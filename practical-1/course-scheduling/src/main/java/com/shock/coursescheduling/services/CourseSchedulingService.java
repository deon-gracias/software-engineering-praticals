package com.shock.coursescheduling.services;

import com.shock.coursescheduling.models.RequestCourses;
import com.shock.coursescheduling.models.ScheduledCourseModel;
import org.springframework.scheduling.SchedulingException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CourseSchedulingService {
    public static ScheduledCourseModel schedule(RequestCourses courses) throws SchedulingException {
        String[] prefsList;
        String availableTiming;
        boolean foundRoom = false;
        int enrollmentDiff;

        HashSet<String> tempTimes =  courses.getTimes();

        HashMap<String, String> courseRooms = new HashMap<String, String>();
        HashMap<String, String> courseTimings = new HashMap<String, String>();

        for (Map.Entry<String, Integer> course : courses.getCourses().entrySet()) {
            // Set enrollment diff to highest
            enrollmentDiff = Integer.MAX_VALUE;

            // Get Prefs List
            prefsList = courses.getPreferences().getOrDefault(course.getKey(), new String[0]);

            // Find Suitable Room for Course
            for (Map.Entry<String, Integer> entry : courses.getRooms().entrySet()) {
                int enrollment = course.getValue();

                if (entry.getValue() >= enrollment && enrollmentDiff > entry.getValue() - enrollment) {
                    foundRoom = true;
                    courseRooms.put(course.getKey(), entry.getKey());
                }
            }

            // Room not found
            if (!foundRoom) {
                throw new SchedulingException("Couldn't find room for " + course.getKey());
            }

            // Check if row has preference
            if (prefsList.length > 0) {
                // Check for available time according to preference
                availableTiming = getAvailable(prefsList, courseTimings);

                // Check if available course in found
                if (availableTiming.equals("")) {
                    throw new SchedulingException("Couldn't find suitable course timings allotment according to preferred timing for " + course.getKey());
                }

            } else {

                // Check if sufficient timings are available
                if (tempTimes.size() < 1) {
                    throw new SchedulingException("Insufficient timings for allotted courses");
                }

                // Get first available timing
                availableTiming = tempTimes.iterator().next();
            }
            // Allot timing for available course
            tempTimes.remove(availableTiming);
            courseTimings.put(availableTiming, course.getKey());
        }

        return new ScheduledCourseModel(courseRooms, courseTimings);
    }

    private static String getAvailable(String[] prefsList, HashMap<String, String> allocatedCourses) {
        // Search for available timings
        for (String pref: prefsList)
            if (!allocatedCourses.containsKey(prefsList))
                return pref;

        // Couldn't find preferred time
        return "";
    }
}
