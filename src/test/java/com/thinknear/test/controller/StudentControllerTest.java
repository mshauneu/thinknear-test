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
import org.springframework.web.util.UriComponentsBuilder;

import com.thinknear.test.Application;
import com.thinknear.test.model.Student;


/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@WebIntegrationTest(randomPort = true)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class StudentControllerTest {
	
    @Value("${local.server.port}")
    private int serverPort;
    
    @Resource
    private ServerProperties serverProperties;

    private RestTemplate restTemplate = new RestTemplate();

    
    @Test
    public void testCreateAndDelete() throws Exception {
    	String id = saveStudent(null, "fn", "ln");
		Student student = getStudent(id);
		Assert.assertEquals("fn", student.getFirstName());
		Assert.assertEquals("ln", student.getLastName());

		try {
			saveStudent(null, "fn", "ln");
			Assert.assertTrue("409 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(409, e.getStatusCode().value());
		}
		try {
			deleteStudent("-");
			Assert.assertTrue("404 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(404, e.getStatusCode().value());
		}
		
		deleteStudent(id);
		Assert.assertEquals(0, getStudents().size());
    }

    @Test
    public void testUpdate() throws Exception {
    	Student student = null;
    	String id = saveStudent(null, "fn0", "ln0");
		student = getStudent(id);
		Assert.assertEquals("fn0", student.getFirstName());
		Assert.assertEquals("ln0", student.getLastName());

		saveStudent(id, "fn", null);
		student = getStudent(id);
		Assert.assertEquals("fn", student.getFirstName());
		Assert.assertEquals("ln0", student.getLastName());

		saveStudent(id, null, "ln");
		student = getStudent(id);
		Assert.assertEquals("fn", student.getFirstName());
		Assert.assertEquals("ln", student.getLastName());
		
		try {
			saveStudent("-", "fn", "ln");
			Assert.assertTrue("404 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(404, e.getStatusCode().value());
		}
		
		deleteStudent(id);
		Assert.assertEquals(0, getStudents().size());
    }
    
    @Test
    public void testRetrieve() throws Exception {
		List<String> studentsIds = new ArrayList<>();
		studentsIds.add(saveStudent(null, "fn1", "ln1"));
		studentsIds.add(saveStudent(null, "fn2", "ln2"));
		studentsIds.add(saveStudent(null, "fn3", "ln3"));
		studentsIds.add(saveStudent(null, "fn4", "ln4"));
		studentsIds.add(saveStudent(null, "fn5", "ln5"));
		Assert.assertEquals(5, getStudents().size());
		Assert.assertEquals(5, getStudents(0, 100).size());
		Assert.assertEquals(0, getStudents(100, 100).size());
		
		List<Student> students = getStudents(1, 3);
		students.sort((l, r) -> l.getFirstName().compareTo(r.getFirstName()));
		Assert.assertEquals(3, students.size());
		Assert.assertEquals("fn2", students.get(0).getFirstName());
		Assert.assertEquals("fn4", students.get(students.size() - 1).getFirstName());
		
		studentsIds.forEach(this::deleteStudent);
		Assert.assertEquals(0, getStudents().size());
    }


	private String saveStudent(String id, String firstName, String lastName) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath() + "/students";
		if (id != null) {
			serverUri = serverUri + "/" + id;
		}
		
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        return restTemplate.exchange(serverUri, id == null ? HttpMethod.POST : HttpMethod.PUT, new HttpEntity<>(student, headers), String.class).getBody();
	}

	private Student getStudent(String id) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath();
        return restTemplate.exchange(serverUri  + "/students/" + id, 
        		HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), Student.class).getBody();
	}
	
	private List<Student> getStudents() {
		return getStudents(null, null);
	}

	private List<Student> getStudents(Integer offset, Integer limit) {
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(
				"http://localhost:" + serverPort + serverProperties.getContextPath() + "/students");
		if (offset != null) {
			uri.queryParam("offset", offset);
		}
		if (limit != null) {
			uri.queryParam("limit", limit);
		}
        return restTemplate.exchange(uri.build().encode().toUri(), 
        		HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<List<Student>>() {}).getBody();
	}
	
	private String deleteStudent(String id) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath();
        return restTemplate.exchange(serverUri  + "/students/" + id, 
        		HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class).getBody();
	}

}
