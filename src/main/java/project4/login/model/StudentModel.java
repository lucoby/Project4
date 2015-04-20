package project4.login.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * student model contains all the info inputed by the student and keeps track of
 * data related to the student
 * 
 * @author Zhang3r
 *
 */
public class StudentModel {

    private String id;

    private String desiredCourses;

    private List<String> courses;

    /**
     * constructor
     * 
     */
    public StudentModel() {

    }

    public String getDesiredCourses() {
	return desiredCourses;
    }

    public void setDesiredCourses(String desiredCourses) {
	this.desiredCourses = desiredCourses;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public List<String> getCourses() {
	if (courses == null) {
	    courses = new ArrayList<String>();
	}

	return courses;
    }

    public void setCourses(List<String> courses) {
	this.courses = courses;
    }

}
