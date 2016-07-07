package com.thinknear.test.repo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.thinknear.test.exception.CourseAlreadyExistsException;
import com.thinknear.test.exception.CourseNotFoundException;
import com.thinknear.test.model.Course;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@Component
public class CourseRepo {

	private Map<String, Course> courses = new LinkedHashMap<>();
	
	public List<Course> retreive(List<String> courseCodes, Integer offset, Integer limit) {
		int from = offset == null ? 0 : Math.min(offset, courses.size());
		int to = limit == null ? courses.size() : Math.min(from + limit, courses.size()); 
		return courses.values().stream().filter(c -> {
			return courseCodes == null ? true : courseCodes.contains(c.getCode());
		}).collect(Collectors.toList()).subList(from, to);
	}
	
	public String create(Course course) {
		String code = course.getCode();
		if (courses.containsKey(code)) {
			throw new CourseAlreadyExistsException(String.format("Course with code: %s - exists", course.getCode()));
		}
		courses.put(code, course);
		return code;
	}

	public String update(String code, Course course) {
		Course anCourse = courses.get(code);
		if (anCourse == null) {
			throw new CourseNotFoundException("Course with code: " + code + " - not found");
		}
		if (course.getTitle() != null) {
			anCourse.setTitle(course.getTitle());
		}
		if (course.getDescription() != null) {
			anCourse.setDescription(course.getDescription());
		}
		return code;
	}

	public String remove(String code) {
		if (courses.remove(code) == null) {
			throw new CourseNotFoundException("Course with code: " + code + " - not found");
		}
		return code;
	}

	public Course get(String code) {
		Course course = courses.get(code);
		if (course == null) {
			throw new CourseNotFoundException("Course with code: " + code + " - not found");
		}
		return course;
	}

	public boolean exists(String courseCode) {
		return courses.containsKey(courseCode);
	}
	
}
