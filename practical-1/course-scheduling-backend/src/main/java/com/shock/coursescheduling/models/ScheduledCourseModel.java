package com.shock.coursescheduling.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class ScheduledCourseModel {
    private HashMap<String, String> rooms;
    private HashMap<String, String> times;
    private HashMap<String, HashMap<String, String[]>> roomWise;
//    private HashMap<String, HashMap<String, String[]>> courseWise;
}
