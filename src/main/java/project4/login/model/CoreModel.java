package project4.login.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import project4.login.model.classroom.Course;

/**
 * core Model keeps track of data related to the calculations that needs to be done
 * 
 * @author Zhang3r
 *
 */
public class CoreModel {

    @Id
    private String id;

    private List<Course> coursesCatalog;

    private List<Course> requiredCourses;

    private Map<String, String> taAssignments;

    // courseid, courseid
    private Map<String, String> prerequisite;

    // courseid, courseid
    private Map<String, String> corequisite;
    
    
    private int enrollmentLimit;
    
    /**
     * constructor
     */
    public CoreModel() {

    }

    public CoreModel(String id) {
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

    public Map<String, String> getTaAssignments() {
	return taAssignments;
    }

    public void setTaAssignments(Map<String, String> taAssignments) {
	this.taAssignments = taAssignments;
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

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }
}
