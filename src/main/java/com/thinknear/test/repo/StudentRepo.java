package com.thinknear.test.repo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.thinknear.test.exception.StudentAlreadyExistsException;
import com.thinknear.test.exception.StudentNotFoundException;
import com.thinknear.test.model.Student;

/**
 *
 * @author <a href="mailto:m.shauneu@gmail.com">Mike Shauneu</a>
 */
@Component
public class StudentRepo {

	private Map<String, Student> students = new LinkedHashMap<>();
	
	public List<Student> retreive(List<String> studentIds, Integer offset, Integer limit) {
		int from = offset == null ? 0 : Math.min(offset, students.size());
		int to = limit == null ? students.size() : Math.min(from + limit, students.size()); 
		return students.values().stream().filter(s -> {
			return studentIds == null ? true : studentIds.contains(s.getId());
		}).collect(Collectors.toList()).subList(from, to);
	}

	public String create(Student student) {
		students.values().forEach(s -> {
			if (s.getFirstName().equals(student.getFirstName()) &&
				s.getLastName().equals(student.getLastName())) 
			{
				throw new StudentAlreadyExistsException(String.format(
						"Student with  fn: %s and ln: %s - exists", student.getFirstName(), student.getLastName()));
			}
		});
		String id = UUID.randomUUID().toString();
		student.setId(id);
		students.put(id, student);
		return id;
	}

	public String update(String id, Student student) {
		Student anStudent = students.get(id);
		if (anStudent == null) {
			throw new StudentNotFoundException("Student with id: " + id + " - not found");
		}
		
		if (student.getFirstName() != null) {
			anStudent.setFirstName(student.getFirstName());
		}
		if (student.getLastName() != null) {
			anStudent.setLastName(student.getLastName());
		}
		return id;
	}

	public String remove(String id) {
		if (students.remove(id) == null) {
			throw new StudentNotFoundException("Student with id: " + id + " - not found");
		}
		return id;
	}

	public Student get(String id) {
		Student student = students.get(id);
		if (student == null) {
			throw new StudentNotFoundException("Student with id: " + id + " - not found");
		}
		return student;
	}

	public boolean exists(String id) {
		return students.containsKey(id);
	}
	
}
