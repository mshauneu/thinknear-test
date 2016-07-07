package com.thinknear.test.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thinknear.test.exception.ExceptionMessage;
import com.thinknear.test.model.Course;
import com.thinknear.test.service.CourseService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@RestController
@RequestMapping(path = "/courses")
public class CourseController {

    @Resource
    private CourseService service;

    @ApiResponses(value = {
            @ApiResponse(code = 404, response = ExceptionMessage.class, message = "CourseNotFoundException")
    })
    @RequestMapping(path = "/{code}", method = RequestMethod.GET)
    public Course get(@PathVariable String code) {
		return service.get(code);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Course> retreive(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit) {
		return service.retreive(offset, limit);
    }
    
    @ApiResponses(value = {
            @ApiResponse(code = 409, response = ExceptionMessage.class, message = "CourseAlreadyExistsException")
    })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestBody Course course) {
		return service.create(course);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 404, response = ExceptionMessage.class, message = "CourseNotFoundException")
    })
    @RequestMapping(path = "/{code}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@PathVariable String code, @RequestBody Course course) {
		return service.update(code, course);
    }
    
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = ExceptionMessage.class, message = "CourseNotFoundException")
    })
    @RequestMapping(path = "/{code}", method = RequestMethod.DELETE)
    public String remove(@PathVariable String code) {
		return service.remove(code);
    }
    
}
