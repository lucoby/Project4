package project4.login.model.lpsolver;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

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
    
    private List<StudentModel> students;
}
