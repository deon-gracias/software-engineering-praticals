package com.shock.coursescheduling.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourses {
    private HashMap<String, Integer> courses;
    private HashMap<String, Integer> rooms;
    private HashSet<String> times;
    private HashMap<String, String[]> preferences;
}
