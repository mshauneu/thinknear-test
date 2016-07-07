package com.thinknear.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.thinknear.test.model.Course;
import com.thinknear.test.repo.CourseRepo;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@Service
public class CourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);
    
    @Resource
    private CourseRepo repo;

    
	public List<Course> retreive(Integer offset, Integer limit) {
		return repo.retreive(null, offset, limit);
	}

	public Course get(String code) {
		return repo.get(code);
	}
	
	public String create(Course course) {
		Assert.notNull(course.getCode(), "Course code cannot be null");
		Assert.notNull(course.getTitle(), "Course title cannot be null");
		String code = repo.create(course);
		LOGGER.debug("Course: {} - created", code);
		return code;
	}

	public String update(String code, Course course) {
		String resCode = repo.update(code, course);
		LOGGER.debug("Course: {} - updated", code);
		return resCode;
	}

	public String remove(String code) {
		String resCode = repo.remove(code);
		LOGGER.debug("Course: {} - removed", code);
		return resCode;
	}

}
