package project4.login.model;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * student model
 * contains all the info inputed by the student
 * and keeps track of data related to the student
 * @author Zhang3r
 *
 */
public class StudentModel {

    @Id
    private String id;

    private String pwd;

    private int desiredCourses;

    private List<String> nextSemester;
    
    private int seniority;
    
    private List<String> coursesTaken;

    
    /**
     * constructor
     * @param id
     */
    public StudentModel(String id) {
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

    public List<String> getNextSemester() {
	return nextSemester;
    }

    public void setNextSemester(List<String> nextSemester) {
	this.nextSemester = nextSemester;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
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

}
