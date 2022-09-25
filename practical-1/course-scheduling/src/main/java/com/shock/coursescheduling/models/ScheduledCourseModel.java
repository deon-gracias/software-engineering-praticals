package com.shock.coursescheduling.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class ScheduledCourseModel {
    private HashMap<String, String> rooms;
    private HashMap<String, String> times;
}
