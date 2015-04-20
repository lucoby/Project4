package project4.login.model.classroom;


import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;



public class Student {
    @Id
	private String id;

	private String pwd;

	private int desiredCourses;

	private Map<String,Integer> nextSemester;

	private int seniority;

	private List<String> coursesTaken;

	/**
	 * constructor
	 * 
	 * @param id
	 */
	public Student(String id) {
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

	public Map<String,Integer> getNextSemester() {
		return nextSemester;
	}

}
