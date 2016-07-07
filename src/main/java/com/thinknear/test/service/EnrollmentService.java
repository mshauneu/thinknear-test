package com.thinknear.test.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.thinknear.test.model.Enrollment;
import com.thinknear.test.repo.CourseRepo;
import com.thinknear.test.repo.EnrollmentRepo;
import com.thinknear.test.repo.StudentRepo;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@Service
public class EnrollmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentService.class);
    
    @Resource
    private StudentRepo studentRepo;

    @Resource
    private CourseRepo courseRepo;

    @Resource
    private EnrollmentRepo enrollmentRepo;
    
    
	public List<Enrollment> retreive(String studentId, String courseCode) {
		return enrollmentRepo.retreive(studentId, courseCode);
	}
    
	public void create(Enrollment enrollment) {
		validate(enrollment);
		enrollmentRepo.create(enrollment);
		LOGGER.debug("Enrollment: {} - created", enrollment);
	}

	private void validate(Enrollment enrollment) {
		Assert.notNull(enrollment.getStudentId(), "Enrolment studentId cannot be null");
		Assert.notNull(enrollment.getCourseCode(), "Enrolment courseCode cannot be null");
	}


	public void remove(Enrollment enrollment) {
		validate(enrollment);
		enrollmentRepo.remove(enrollment);
		LOGGER.debug("Enrollment: {} - removed", enrollment);
	}

}
