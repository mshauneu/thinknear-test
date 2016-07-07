package com.thinknear.test.repo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.thinknear.test.exception.StudentAlreadyEnrolledException;
import com.thinknear.test.exception.StudentWasNotEnrolledException;
import com.thinknear.test.model.Enrollment;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@Component
public class EnrollmentRepo {

	private Set<Enrollment> enrollments = new HashSet<>();


	public List<Enrollment> retreive(String studentId, String courseCode) {
		return enrollments.stream().filter(e -> {
			if (studentId != null) {
				if (!studentId.equals(e.getStudentId())) {
					return false;
				}
			}
			if (courseCode != null) {
				if (!courseCode.equals(e.getCourseCode())) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());
	}
	
	public void create(Enrollment enrollment) {
		if (!enrollments.add(enrollment)) {
			throw new StudentAlreadyEnrolledException(String.format(
					"Student with id: %s already enrolled to course with code: %s",  
					enrollment.getStudentId(), enrollment.getCourseCode()));
		}
		System.err.println(enrollments);
	}

	public void remove(Enrollment enrollment) {
		if (!enrollments.remove(enrollment)) {
			throw new StudentWasNotEnrolledException(String.format(
					"Student with id: %s was not been enrolled to course with code: %s",  
					enrollment.getStudentId(), enrollment.getCourseCode()));
		}
	}

}
