package project4.login.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class AdminModel {

    @Id
    private String id;

    private String classEnrollmentLimit;

    private List<String> TAs;

    private List<String> coursesForSemester;

    private List<String> professors;

    public AdminModel() {

    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getClassEnrollmentLimit() {
	return classEnrollmentLimit;
    }

    public void setClassEnrollmentLimit(String classEnrollmentLimit) {
	this.classEnrollmentLimit = classEnrollmentLimit;
    }

    public List<String> getTAs() {
	if (TAs == null) {
	    TAs = new ArrayList<>();
	}
	return TAs;
    }

    public void setTAs(List<String> tAs) {
	TAs = tAs;
    }

    public List<String> getCoursesForSemester() {
	if (coursesForSemester == null) {
	    coursesForSemester = new ArrayList<>();
	}
	return coursesForSemester;
    }

    public void setCoursesForSemester(List<String> coursesForSemester) {
	this.coursesForSemester = coursesForSemester;
    }

    public List<String> getProfessors() {
	if (professors == null) {
	    professors = new ArrayList<>();
	}
	return professors;
    }

    public void setProfessors(List<String> professors) {
	this.professors = professors;
    }

}
