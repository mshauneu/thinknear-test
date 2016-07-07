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
import com.thinknear.test.model.Student;
import com.thinknear.test.service.StudentService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@RestController
@RequestMapping(path = "/students")
public class StudentController {

    @Resource
    private StudentService service;

    @ApiResponses(value = {
            @ApiResponse(code = 404, response = ExceptionMessage.class, message = "StudentNotFoundException")
    })
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Student get(@PathVariable String id) {
		return service.get(id);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> retreive(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit) {
		return service.retreive(offset, limit);
    }
    
    @ApiResponses(value = {
            @ApiResponse(code = 409, response = ExceptionMessage.class, message = "StudentAlreadyExistsException")
    })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestBody Student student) {
		return service.create(student);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 404, response = ExceptionMessage.class, message = "StudentNotFoundException")
    })
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@PathVariable String id, @RequestBody Student student) {
		return service.update(id, student);
    }
    
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = ExceptionMessage.class, message = "StudentNotFoundException")
    })
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public String remove(@PathVariable String id) {
		return service.remove(id);
    }
    
}
