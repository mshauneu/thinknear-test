package com.thinknear.test.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.thinknear.test.Application;
import com.thinknear.test.model.Course;
import com.thinknear.test.model.Enrollment;
import com.thinknear.test.model.Student;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@WebIntegrationTest(randomPort = true)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EnrollmentControllerTest {
	
    @Value("${local.server.port}")
    private int serverPort;

    @Resource
    private ServerProperties serverProperties;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void test() throws Exception {

    	int studentCount = 7;
    	List<String> studentIds = new ArrayList<>(studentCount);
    	for (int i = 0; i < studentCount; i++) {
			studentIds.add(createStudent("fn" + i, "ln" + i));
		}
    	
    	int courseCount = 5;
    	List<String> courseCodes = new ArrayList<>(courseCount);
    	for (int i = 0; i < courseCount; i++) {
    		courseCodes.add(createCourse("c" + i, "t" + i, "d" + i));
		}
    	
    	enrollStudentToCourse(studentIds.get(0), courseCodes.get(0));
    	enrollStudentToCourse(studentIds.get(0), courseCodes.get(1));
    	enrollStudentToCourse(studentIds.get(0), courseCodes.get(2));
    	enrollStudentToCourse(studentIds.get(1), courseCodes.get(0));
    	enrollStudentToCourse(studentIds.get(1), courseCodes.get(1));
    	
		try {
			enrollStudentToCourse(studentIds.get(0), courseCodes.get(0));
			Assert.assertTrue("409 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(409, e.getStatusCode().value());
		}
		try {
			disenrollStudentFromCourse(studentIds.get(studentCount - 1), courseCodes.get(0));
			Assert.assertTrue("404 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(404, e.getStatusCode().value());
		}
		try {
			disenrollStudentFromCourse(studentIds.get(0), courseCodes.get(courseCount - 1));
			Assert.assertTrue("404 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(404, e.getStatusCode().value());
		}
    	
		Assert.assertEquals(3, getStudenEnrollments(studentIds.get(0)).size());
		Assert.assertEquals(2, getStudenEnrollments(studentIds.get(1)).size());

		Assert.assertEquals(2, getCourseEnrollments(courseCodes.get(0)).size());
		Assert.assertEquals(1, getCourseEnrollments(courseCodes.get(2)).size());
		
		disenrollStudentFromCourse(studentIds.get(0), courseCodes.get(0));
		Assert.assertEquals(2, getStudenEnrollments(studentIds.get(0)).size());
		Assert.assertEquals(1, getCourseEnrollments(courseCodes.get(0)).size());
		
		courseCodes.forEach(this::deleteCourse);
		studentIds.forEach(this::deleteStudent);
    }
    
	private List<Enrollment> getStudenEnrollments(String studentId) {
        return restTemplate.exchange(
        		"http://localhost:" + serverPort + serverProperties.getContextPath() + "/enrollments?studentId=" + studentId, 
        		HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<List<Enrollment>>() {}).getBody();
	}

	private List<Enrollment> getCourseEnrollments(String courseCode) {
        return restTemplate.exchange(
        		"http://localhost:" + serverPort + serverProperties.getContextPath() + "/enrollments?courseCode=" + courseCode, 
        		HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<List<Enrollment>>() {}).getBody();
	}
    
	private void enrollStudentToCourse(String studentId, String courseCode) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath() + "/enrollments";
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseCode(courseCode);
        restTemplate.exchange(serverUri, HttpMethod.POST, new HttpEntity<>(enrollment, headers), Void.class).getBody();
	}
    
	private void disenrollStudentFromCourse(String studentId, String courseCode) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath() + "/enrollments";
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseCode(courseCode);
        restTemplate.exchange(serverUri, HttpMethod.DELETE, new HttpEntity<>(enrollment, headers), Void.class).getBody();
	}
    
	private String createCourse(String code, String title, String description) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath() + "/courses";
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Course course = new Course();
        course.setCode(code);
        course.setTitle(title);
        course.setDescription(description);
        return restTemplate.exchange(serverUri, HttpMethod.POST, new HttpEntity<>(course, headers), String.class).getBody();
	}
	
	private String deleteCourse(String id) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath();
        return restTemplate.exchange(serverUri  + "/courses/" + id, 
        		HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class).getBody();
	}
	
    
	private String createStudent(String firstName, String lastName) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath() + "/students";
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        return restTemplate.exchange(serverUri, HttpMethod.POST, new HttpEntity<>(student, headers), String.class).getBody();
	}

	private String deleteStudent(String id) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath();
        return restTemplate.exchange(serverUri  + "/students/" + id, 
        		HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class).getBody();
	}
	
}
