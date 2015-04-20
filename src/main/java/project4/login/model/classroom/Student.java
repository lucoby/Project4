package project4.login.model.classroom;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class Student {
    @Id
    private String id;

    private String pwd;

    private int desiredCourses;

    private Map<String, Integer> nextSemester;

    private int seniority;

    private List<String> coursesTaken;

    public Student() {

    }

    public Student(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getPwd() {
	return pwd;
    }

    public void setPwd(String pwd) {
	this.pwd = pwd;
    }

    public int getDesiredCourses() {
	return desiredCourses;
    }

    public void setDesiredCourses(int desiredCourses) {
	this.desiredCourses = desiredCourses;
    }

    public Map<String, Integer> getNextSemester() {
	return nextSemester;
    }

    public void setNextSemester(Map<String, Integer> nextSemester) {
	this.nextSemester = nextSemester;
    }

    public int getSeniority() {
	return seniority;
    }

    public void setSeniority(int seniority) {
	this.seniority = seniority;
    }

    public List<String> getCoursesTaken() {
	return coursesTaken;
    }

    public void setCoursesTaken(List<String> coursesTaken) {
	this.coursesTaken = coursesTaken;
    }

    /**
     * constructor
     * 
     * @param id
     */

}
