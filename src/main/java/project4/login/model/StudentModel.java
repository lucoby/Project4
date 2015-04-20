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

<<<<<<< HEAD
	@Id
	private String id;
=======
    private String id;
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

<<<<<<< HEAD
	private String pwd;
=======
    private String desiredCourses;
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

<<<<<<< HEAD
	private int desiredCourses;
=======
    private List<String> courses;
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

<<<<<<< HEAD
	private Map<String,Integer> nextSemester;

	private int seniority;
=======
    /**
     * constructor
     * 
     */
    public StudentModel() {

    }
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

<<<<<<< HEAD
	private List<String> coursesTaken;

	/**
	 * constructor
	 * 
	 * @param id
	 */
	public StudentModel(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}
=======
    public String getDesiredCourses() {
	return desiredCourses;
    }
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

<<<<<<< HEAD
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getDesiredCourses() {
		return desiredCourses;
	}

	public void setDesiredCourses(int desiredCourses) {
		this.desiredCourses = desiredCourses;
	}
=======
    public void setDesiredCourses(String desiredCourses) {
	this.desiredCourses = desiredCourses;
    }
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

	public Map<String,Integer> getNextSemester() {
		return nextSemester;
	}

	public void setNextSemester(Map<String,Integer> nextSemester) {
		this.nextSemester = nextSemester;
	}

<<<<<<< HEAD
	public String getId() {
		return id;
	}
=======
    public List<String> getCourses() {
	if (courses == null) {
	    courses = new ArrayList<String>();
	}

	return courses;
    }
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

<<<<<<< HEAD
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
=======
    public void setCourses(List<String> courses) {
	this.courses = courses;
    }
>>>>>>> branch 'master' of https://github.com/zhang3r/Project4.git

}
