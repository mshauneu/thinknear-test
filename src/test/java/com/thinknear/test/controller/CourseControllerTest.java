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
import com.thinknear.test.model.Course;


/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@WebIntegrationTest(randomPort = true)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CourseControllerTest {
	
    @Value("${local.server.port}")
    private int serverPort;
    
    

    @Resource
    private ServerProperties serverProperties;

    private RestTemplate restTemplate = new RestTemplate();

    
    @Test
    public void testCreateAndDelete() throws Exception {
    	String code = saveCourse("c", "t", "d");
		Course course = getCourse(code);
		Assert.assertEquals("c", course.getCode());
		Assert.assertEquals("t", course.getTitle());
		Assert.assertEquals("d", course.getDescription());
		
		try {
			saveCourse("c", "t", "d");
			Assert.assertTrue("409 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(409, e.getStatusCode().value());
		}

		deleteCourse(code);
		Assert.assertEquals(0, getCourses().size());
    }

    @Test
    public void testUpdate() throws Exception {
    	Course course = null;
    	String code = saveCourse("c", "t0", "d0");
		course = getCourse(code);
		Assert.assertEquals("t0", course.getTitle());
		Assert.assertEquals("d0", course.getDescription());

    	updateCourse(code, "t", null);
		course = getCourse(code);
		Assert.assertEquals("t", course.getTitle());
		Assert.assertEquals("d0", course.getDescription());

		updateCourse(code, null, "d");
		course = getCourse(code);
		Assert.assertEquals("t", course.getTitle());
		Assert.assertEquals("d", course.getDescription());

		try {
			updateCourse("cc", "t", "d");
			Assert.assertTrue("404 status expected", false);
		} catch (HttpClientErrorException e) {
			Assert.assertEquals(404, e.getStatusCode().value());
		}
		
		deleteCourse(code);
		Assert.assertEquals(0, getCourses().size());
    }
    
    @Test
    public void testRetrieve() throws Exception {
		List<String> courseCodes = new ArrayList<>();
		courseCodes.add(saveCourse("c1", "t1", "d1"));
		courseCodes.add(saveCourse("c2", "t2", "d2"));
		courseCodes.add(saveCourse("c3", "t3", "d3"));
		courseCodes.add(saveCourse("c4", "t4", "d4"));
		courseCodes.add(saveCourse("c5", "t5", "d5"));
		Assert.assertEquals(5, getCourses().size());
		Assert.assertEquals(5, getCourses(0, 100).size());
		Assert.assertEquals(0, getCourses(100, 100).size());
		
		List<Course> courses = getCourses(1, 3);
		courses.sort((l, r) -> l.getTitle().compareTo(r.getTitle()));
		Assert.assertEquals(3, courses.size());
		Assert.assertEquals("t2", courses.get(0).getTitle());
		Assert.assertEquals("t4", courses.get(courses.size() - 1).getTitle());
		
		courseCodes.forEach(this::deleteCourse);
		Assert.assertEquals(0, getCourses().size());
    }


	private String saveCourse(String code, String title, String description) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath() + "/courses";
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Course course = new Course();
        course.setCode(code);
        course.setTitle(title);
        course.setDescription(description);
        return restTemplate.exchange(serverUri, HttpMethod.POST, new HttpEntity<>(course, headers), String.class).getBody();
	}

	private String updateCourse(String code, String title, String description) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath() + "/courses/" + code;
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Course course = new Course();
        course.setCode(code);
        course.setTitle(title);
        course.setDescription(description);
        return restTemplate.exchange(serverUri, HttpMethod.PUT, new HttpEntity<>(course, headers), String.class).getBody();
	}
	
	private Course getCourse(String id) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath();
        return restTemplate.exchange(serverUri  + "/courses/" + id, 
        		HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), Course.class).getBody();
	}
	
	private List<Course> getCourses() {
		return getCourses(null, null);
	}

	private List<Course> getCourses(Integer offset, Integer limit) {
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(
				"http://localhost:" + serverPort + serverProperties.getContextPath() + "/courses");
		if (offset != null) {
			uri.queryParam("offset", offset);
		}
		if (limit != null) {
			uri.queryParam("limit", limit);
		}
        return restTemplate.exchange(uri.build().encode().toUri(), 
        		HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<List<Course>>() {}).getBody();
	}
	
	private String deleteCourse(String id) {
		String serverUri = "http://localhost:" + serverPort + serverProperties.getContextPath();
        return restTemplate.exchange(serverUri  + "/courses/" + id, 
        		HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class).getBody();
	}
}
