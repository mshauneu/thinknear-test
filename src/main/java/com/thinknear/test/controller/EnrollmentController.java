package com.thinknear.test.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thinknear.test.exception.ExceptionMessage;
import com.thinknear.test.model.Enrollment;
import com.thinknear.test.service.EnrollmentService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@RestController
@RequestMapping(path = "/enrollments")
public class EnrollmentController {

    @Resource
    private EnrollmentService service;


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Enrollment> retreive(@RequestParam(required = false) String studentId, @RequestParam(required = false) String courseCode) {
		return service.retreive(studentId, courseCode);
    }
    
    @ApiResponses(value = {
            @ApiResponse(code = 409, response = ExceptionMessage.class, message = "StudentAlreadyEnrolledException")
    })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody Enrollment enrollment) {
		service.create(enrollment);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 404, response = ExceptionMessage.class, message = "StudentWasNotEnrolledException")
    })
    @RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void remove(@RequestBody Enrollment enrollment) {
		service.remove(enrollment);
    }
    
}
