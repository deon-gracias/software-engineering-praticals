package com.shock.coursescheduling.models;

import lombok.Data;

@Data
public class CourseModel {
    private String name;
    private int enrolled;
    private String room;
    private String time;
}
