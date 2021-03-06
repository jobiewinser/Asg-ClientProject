package com.nsa.team10.asgproject.controllers.instructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/instructor/courses")
public class CourseController
{
    @GetMapping()
    public String list()
    {
        return "/instructor/gscourses/index";
    }

    @GetMapping("/{id:[0-9]+}")
    public String manage()
    {
        return "/instructor/gscourses/manage";
    }
}
