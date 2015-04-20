package project4.login.model.lpsolver;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import project4.login.model.StudentModel;
import project4.login.model.classroom.Course;

public class LpInput {

    @Id
    private String id;

    private List<Course> coursesCatalog;

    private List<Course> requiredCourses;

     // private Map<String, String> taAssignments;

    // courseid, courseid
    private Map<String, String> prerequisite;

    // courseid, courseid
    private Map<String, String> corequisite;
    
    private Map<String, Integer> enrollmentLimit;
    
    private int defaultEnrollmentLimit;
    
    private List<StudentModel> students;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Course> getCoursesCatalog() {
		return coursesCatalog;
	}

	public void setCoursesCatalog(List<Course> coursesCatalog) {
		this.coursesCatalog = coursesCatalog;
	}

	public List<Course> getRequiredCourses() {
		return requiredCourses;
	}

	public void setRequiredCourses(List<Course> requiredCourses) {
		this.requiredCourses = requiredCourses;
	}

	public Map<String, String> getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(Map<String, String> prerequisite) {
		this.prerequisite = prerequisite;
	}

	public Map<String, String> getCorequisite() {
		return corequisite;
	}

	public void setCorequisite(Map<String, String> corequisite) {
		this.corequisite = corequisite;
	}

	public Map<String, Integer> getEnrollmentLimit() {
		return enrollmentLimit;
	}

	public void setEnrollmentLimit(Map<String, Integer> enrollmentLimit) {
		this.enrollmentLimit = enrollmentLimit;
	}

	public List<StudentModel> getStudents() {
		return students;
	}

	public void setStudents(List<StudentModel> students) {
		this.students = students;
	}

	public int getDefaultEnrollmentLimit() {
		return defaultEnrollmentLimit;
	}

	public void setDefaultEnrollmentLimit(int defaultEnrollmentLimit) {
		this.defaultEnrollmentLimit = defaultEnrollmentLimit;
	}
}
