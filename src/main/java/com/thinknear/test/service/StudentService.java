package com.thinknear.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.thinknear.test.model.Student;
import com.thinknear.test.repo.StudentRepo;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@Service
public class StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
    
    @Resource
    private StudentRepo repo;

    
	public List<Student> retreive(Integer offset, Integer limit) {
		List<Student> students = repo.retreive(null, offset, limit);
		return students;
	}

	public Student get(String id) {
		return repo.get(id);
	}
	
	public String create(Student student) {
		Assert.notNull(student.getFirstName(), "Student firstName cannot be null");
		Assert.notNull(student.getLastName(), "Student lastName cannot be null");
		String id = repo.create(student);
		LOGGER.debug("Student: {} - created", id);
		return id;
	}

	public String update(String id, Student student) {
		String resId = repo.update(id, student);
		LOGGER.debug("Student: {} - updated", id);
		return resId;
	}

	public String remove(String id) {
		String resId = repo.remove(id);
		LOGGER.debug("Student: {} - removed", id);
		return resId;
	}

}
