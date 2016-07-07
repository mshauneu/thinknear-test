# Test for thinknear.com
System that assigns students to classes.
## Docs: 
URL to Swagger API spec is `http://localhost:7777/v2/api-docs`.
Swagger Plugin for Chrome could be found at: `https://chrome.google.com/webstore/detail/swagger-ui-console/ljlmonadebogfjabhkppkoohjkjclfai?hl=en`

## Build:
`mvn package`
## Run:
`mvn spring-boot:run`
## Run in Docker:
`mvn package && docker-compose up --build`
## Usage:
### Students:
Create student with name `John Doe`:

    curl -H "Content-Type: application/json" -XPOST localhost:7777/students -d '{"firstName": "John", "lastName": "Doe"}'

Result:

    44116717-555d-4a35-9ffc-98ef17019b1d

List all students:

    curl -XGET 'localhost:7777/students'

Result:

    [{"id":"44116717-555d-4a35-9ffc-98ef17019b1d","lastName":"Doe","firstName":"John"}]

Update first name:

    curl -H "Content-Type: application/json" -XPUT localhost:7777/students/44116717-555d-4a35-9ffc-98ef17019b1d -d '{"firstName": "Johnny"}'

Result:

    44116717-555d-4a35-9ffc-98ef17019b1d

List first 10 students:

    curl -XGET 'localhost:7777/students?offset=0&limit=10'

Result:

    [{"id":"44116717-555d-4a35-9ffc-98ef17019b1d","lastName":"Doe","firstName":"Johnny"}]

Delete the user:	

    curl -XDELETE localhost:7777/students/44116717-555d-4a35-9ffc-98ef17019b1d

Result:

    44116717-555d-4a35-9ffc-98ef17019b1d

### Courses

Very close to Students. Please see docs.

### Courses and Students

To assign a student to a course:

    curl -H "Content-Type: application/json" -XPOST localhost:7777/enrollments/ -d '{"courseCode": "C_1", "studentId": "S_1"}'
    curl -H "Content-Type: application/json" -XPOST localhost:7777/enrollments/ -d '{"courseCode": "C_1", "studentId": "S_2"}'
    curl -H "Content-Type: application/json" -XPOST localhost:7777/enrollments/ -d '{"courseCode": "C_1", "studentId": "S_3"}'

    curl -H "Content-Type: application/json" -XPOST localhost:7777/enrollments/ -d '{"courseCode": "C_2", "studentId": "S_1"}'
    curl -H "Content-Type: application/json" -XPOST localhost:7777/enrollments/ -d '{"courseCode": "C_2", "studentId": "S_2"}'

To find all courses that student assigned: 

    curl -XGET 'localhost:7777/enrollments?studentId=S_1'

Result:

	[{
		"studentId":"S_1",
		"courseCode":"C_1"
	}, {
		"studentId":"S_1",
		"courseCode":"C_2"
	}]

To find all students that picked up course: 
	
	curl -XGET 'localhost:7777/enrollments?courseCode=C_1'`

Result:

	[{
		"studentId":"S_1",
		"courseCode":"C_1"
	}, {
		"studentId":"S_3",
		"courseCode":"C_1"
	}, {
		"studentId":"S_2",
		"courseCode":"C_1"
	}]

To cancel assignment: 
    
    curl -H "Content-Type: application/json" -XDELETE localhost:7777/enrollments/ -d '{"courseCode": "C_2", "studentId": "S_1"}'
	
### Exceptions handling: 
Below example  of  exception raised when we  try to assign user to  course that he/she was already assigned:

	{
		"status":409,
		"error":"Conflict",
		"reason":"StudentAlreadyEnrolledException",
		"message":"Student with id: S_1 already enrolled to course with code: C_2"
	}

For detailed information about `status`, `reason` and `conflict` values see API swagger doc for every endpoint's call. 
