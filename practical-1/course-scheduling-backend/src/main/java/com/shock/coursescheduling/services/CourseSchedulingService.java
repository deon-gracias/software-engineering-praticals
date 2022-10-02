package com.shock.coursescheduling.services;

import com.shock.coursescheduling.models.RequestCourses;
import com.shock.coursescheduling.models.ScheduledCourseModel;
import org.springframework.scheduling.SchedulingException;

import java.util.*;

public class CourseSchedulingService {
    public static ScheduledCourseModel schedule(RequestCourses courses) throws SchedulingException {

        // Standard
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

        // Room wise
        HashMap<String, HashMap<String, String[]>> roomWise = new HashMap<String, HashMap<String, String[]>>();

        for (Map.Entry<String, String> roomEntry: courseRooms.entrySet()) {
            HashMap<String, String[]> roomData = makeRoomwiseCourses(courseRooms, courseTimings, roomEntry.getValue());
            roomWise.put(roomEntry.getValue(), roomData);
        }

        // Course wise
        HashMap<String, HashMap<String, String[]>> courseWise = new HashMap<>();

        for (Map.Entry<String, String> courseEntry : courseRooms.entrySet()) {
            HashMap<String, String[]> courseData = makeCoursewiseRoom(courseRooms, courseTimings, courseEntry.getKey());
            courseWise.put(courseEntry.getKey(), courseData);
        }

        return new ScheduledCourseModel(courseRooms, courseTimings, roomWise, courseWise);
    }

    private static HashMap<String, String[]> makeRoomwiseCourses(HashMap<String,String> courseRooms, HashMap<String, String> courseTimings, String room) {
        HashMap<String, String[]> roomCourses = new HashMap<String, String[]>();
        for(Map.Entry<String,String> courseEntry: courseRooms.entrySet()) {
            if (courseEntry.getValue().equals(room))

            for(Map.Entry<String,String> timingEntry: courseTimings.entrySet()) {
                if(courseEntry.getKey() != timingEntry.getValue()) continue;

                String[] dayTime = timingEntry.getKey().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                String[] days = dayTime[0].split("");
                for (int i = 0; i < days.length; i++)
                    roomCourses.put(courseEntry.getKey(), new String[]{days[i], dayTime[1]});
            }
        }

        return roomCourses;
    }

    private static HashMap<String, String[]> makeCoursewiseRoom(HashMap<String,String> courseRooms, HashMap<String, String> courseTimings, String course) {
        String[] dayTime = new String[2];

        String[] roomTime = new String[2];
        String tempTime;
        HashMap<String, String[]> dayCourse = new HashMap<String, String[]>();
        Vector<String> rooms = new Vector<>();

        for (Map.Entry<String, String> entry : courseRooms.entrySet()) {
            if (Objects.equals(entry.getKey(), course)) {
                rooms.add(entry.getValue());
            }
        }

        for (Map.Entry<String, String> entry : courseTimings.entrySet()) {
            if (Objects.equals(entry.getValue(), course)) {
                tempTime = entry.getKey();
                dayTime = tempTime.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)", 2);
                roomTime = new String[]{String.valueOf(rooms.get(0)), dayTime[1]};
                String[] days = dayTime[0].split("");
                for (int i = 0; i < days.length; i++) {
                    dayCourse.put(days[i], roomTime);
                }
            }
        }

//        System.out.println(dayCourse);
        return dayCourse;
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
