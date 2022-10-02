package com.shock.coursescheduling.controllers;

import com.shock.coursescheduling.models.RequestCourses;
import com.shock.coursescheduling.models.ScheduledCourseModel;
import com.shock.coursescheduling.services.CourseSchedulingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.SchedulingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ScheduleController {

//    @GetMapping("/schedule") public ResponseEntity<String> getSchedule() {
//        return new ResponseEntity<>("Response", HttpStatus.OK);
//    }

    @PostMapping("/schedule")
    public ResponseEntity<Object> schedule(@RequestBody RequestCourses req) {
        try {
            ScheduledCourseModel result = CourseSchedulingService.schedule(req);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (SchedulingException se) {
            return  new ResponseEntity<>(se.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
