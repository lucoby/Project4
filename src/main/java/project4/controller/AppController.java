package project4.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import project4.login.model.AdminModel;
import project4.login.model.LoginModel;
import project4.login.model.StudentModel;
import project4.login.model.classroom.Course;
import project4.login.model.classroom.Student;
import project4.login.model.lpsolver.LpInput;
import project4.login.model.lpsolver.LpOutput;
import project4.service.PersistenceService;
import project4.service.SuggestionService;

@Controller
@RequestMapping("/login")
public class AppController {
    Logger logger = Logger.getLogger(AppController.class);
    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private PersistenceService persistenceService;

    @RequestMapping("/")
    public String studentLogin(Model model) {
	model.addAttribute("loginModel", new LoginModel());
	return "studentLogin";
    }

    @RequestMapping("/admin")
    public String adminLogin(Model model) {
	model.addAttribute("loginModel", new LoginModel());
	return "adminLogin";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String studentLoginSubmission(@ModelAttribute LoginModel login,
	    Model model) {

	model.addAttribute("studentModel", new StudentModel());

	logger.info("id is " + login.getId());
	logger.info("pw is " + login.getPassword());
	LpInput in = persistenceService.findLpInput("123");
	LpOutput out = suggestionService.calculateSuggestion(in,login.getId());
	persistenceService.saveLpOutToDB(out);
	String[] courses = new String[5];
	
	for(Student s:in.getStudents()) {
		if(s.getId().equals(login.getId())) {
			for (String c : s.getNextSemester().keySet()) {
				String p = c + ", " + s.getNextSemester().get(c);
				logger.info(p);
				courses[Integer.valueOf(s.getNextSemester().get(c))-1] = p;
			}
		}
	}
	PriorityComp pcomp = new PriorityComp();
	Arrays.sort(courses,pcomp);
	
	model.addAttribute("courses", courses);
	
	model.addAttribute("lpOutput",out);
	// TODO set login to true
	return "studentMain";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String adminLoginSubmission(@ModelAttribute LoginModel login,
	    Model model) {
	model.addAttribute("adminModel", new AdminModel());
	logger.info("admin id is " + login.getId());
	logger.info("admin pw is " + login.getPassword());
	// TODO set login to true
	String studentHistory = "OMS-CS-CourseGrades_Final.csv";
	LpInput in = constructLpInput(studentHistory);
	persistenceService.saveLpToDB(in);
	return "adminMain";
    }

    @RequestMapping(value = "/studentView", method = RequestMethod.POST)
    public String studentDataSubmission(
	    @ModelAttribute("studentModel") StudentModel student, Model model) {

	for (String s : student.getCourses()) {
	    logger.info(s);
	}
	
	// TODO use service to update params for LP
	LpInput in = persistenceService.findLpInput("123");
	LpOutput out = persistenceService.findLpOutput(student.getId());
	
	

	
	String[] courses = new String[5];
	
	for(Student s:in.getStudents()) {
		if(s.getId().equals(student.getId())) {
			HashMap<String, Integer> nextSemester = new HashMap<String, Integer>();
			int i = 0;
			for (String c : student.getCourses()) {
				i++;
				nextSemester.put(c, i);
			}
			if(i == Integer.valueOf(student.getDesiredCourses())) {
				s.setNextSemester(nextSemester);
				logger.info("number of desired courses match");
			}
			
			logger.info("calculating new schedule based on student input");
			out = suggestionService.calculateSuggestion(in,student.getId());
			
			for (String c : s.getNextSemester().keySet()) {
				String p = c + ", " + s.getNextSemester().get(c);
				logger.info(p);
				courses[Integer.valueOf(s.getNextSemester().get(c))-1] = p;
			}
		}
	}
	PriorityComp pcomp = new PriorityComp();
	Arrays.sort(courses,pcomp);
	
	persistenceService.saveLpOutToDB(out);
	
	model.addAttribute("courses", courses);
	model.addAttribute("lpOutput",out);
	// TODO return view with graphs and stuff
	return "studentMain";
    }

    @RequestMapping(value = "/adminView", method = RequestMethod.POST)
    public String adminDataSubmission(
	    @ModelAttribute("adminModel") AdminModel admin, Model model) {

	// TODO use service to update params for LP
	logger.info("courses for next semester");
	for (String s : admin.getCoursesForSemester()) {
	    logger.info(s);
	}
	logger.info("TA for next semester");
	for (String s : admin.getTAs()) {
	    logger.info(s);
	}
	logger.info("Prof for next semester");
	for (String s : admin.getProfessors()) {
	    logger.info(s);
	}
	// TODO return view with graphs and stuff
	return "adminMain";
    }

    @RequestMapping(value = "/studentView", params = { "addRow" })
    public String addRow(final StudentModel student,
	    final BindingResult bindingResult, Model model) {
	student.getCourses().add(new String());
	
	LpInput in = persistenceService.findLpInput("123");
//	LpOutput out = suggestionService.calculateSuggestion(in,student.getId());
	LpOutput out = persistenceService.findLpOutput(student.getId());
	
	String[] courses = new String[5];
	
	for(Student s:in.getStudents()) {
		if(s.getId().equals(student.getId())) {
			for (String c : s.getNextSemester().keySet()) {
				String p = c + ", " + s.getNextSemester().get(c);
				logger.info(p);
				courses[Integer.valueOf(s.getNextSemester().get(c))-1] = p;
			}
		}
	}
	PriorityComp pcomp = new PriorityComp();
	Arrays.sort(courses,pcomp);
	
	model.addAttribute("courses", courses);
	model.addAttribute("lpOutput",out);
	
	return "studentMain";
    }

    @RequestMapping(value = "/studentView", params = { "removeRow" })
    public String removeRow(final StudentModel student,
	    final BindingResult bindingResult, final HttpServletRequest req) {
	final int rowId = Integer.valueOf(req.getParameter("removeRow"));
	logger.info("removing" + rowId);
	student.getCourses().remove(rowId);
	return "studentMain";
    }

    @RequestMapping(value = "/adminView", params = { "addCourse" })
    public String addCourse(final AdminModel admin,
	    final BindingResult bindingResult) {
	logger.info("adding course");
	admin.getCoursesForSemester().add(new String());
	return "adminMain";
    }

    @RequestMapping(value = "/adminView", params = { "removeCourse" })
    public String removeCourse(final AdminModel admin,
	    final BindingResult bindingResult, final HttpServletRequest req) {
	logger.info("removing course");
	final int rowId = Integer.valueOf(req.getParameter("removeCourse"));
	logger.info("removing" + rowId);
	admin.getCoursesForSemester().remove(rowId);
	return "adminMain";
    }

    @RequestMapping(value = "/adminView", params = { "addTA" })
    public String addTA(final AdminModel admin,
	    final BindingResult bindingResult) {
	logger.info("adding TA");
	admin.getTAs().add(new String());
	return "adminMain";
    }

    @RequestMapping(value = "/adminView", params = { "removeTA" })
    public String removeTA(final AdminModel admin,
	    final BindingResult bindingResult, final HttpServletRequest req) {
	logger.info("removing TA");
	final int rowId = Integer.valueOf(req.getParameter("removeTA"));
	logger.info("removing" + rowId);
	admin.getTAs().remove(rowId);
	return "adminMain";
    }

    @RequestMapping(value = "/adminView", params = { "addProf" })
    public String addProf(final AdminModel admin,
	    final BindingResult bindingResult) {
	logger.info("adding prof");
	admin.getProfessors().add(new String());
	return "adminMain";
    }

    @RequestMapping(value = "/adminView", params = { "removeProf" })
    public String removeProf(final AdminModel admin,
	    final BindingResult bindingResult, final HttpServletRequest req) {
	logger.info("removing prof");
	final int rowId = Integer.valueOf(req.getParameter("removeProf"));
	logger.info("removing" + rowId);
	admin.getProfessors().remove(rowId);
	return "adminMain";
    }

    public LpInput constructLpInput(String studentInputs) {
		// Because random is cooldom
		Random rand = new Random();

		// Create dummy LpInput
		LpInput ret = new LpInput();
		ret.setId("123");

		// Create coursesCatalog
		ArrayList<Course> coursesCatalog = new ArrayList<Course>();

		coursesCatalog.add(new Course("CS 6440"));
		coursesCatalog.add(new Course("CS 7641"));
		coursesCatalog.add(new Course("CS 6475"));
		coursesCatalog.add(new Course("CS 6300"));
		coursesCatalog.add(new Course("CS 8802"));
		coursesCatalog.add(new Course("CS 7637"));
		coursesCatalog.add(new Course("CS 6310"));
		coursesCatalog.add(new Course("CS 8803"));
		coursesCatalog.add(new Course("CS 4495"));
		coursesCatalog.add(new Course("CS 6210"));
		coursesCatalog.add(new Course("CS 6505"));
		coursesCatalog.add(new Course("CS 6250"));
		coursesCatalog.add(new Course("CS 6290"));
		coursesCatalog.add(new Course("CS 6035"));
		coursesCatalog.add(new Course("CS 7646"));

		ret.setCoursesCatalog(coursesCatalog);

		// Create dummy requiredCourses courses that will be offered next
		// semester
		ArrayList<Course> requiredCourses = new ArrayList<Course>();

		requiredCourses.add(new Course("CS 6440"));
		requiredCourses.add(new Course("CS 7641"));
		requiredCourses.add(new Course("CS 6475"));
		requiredCourses.add(new Course("CS 6300"));
		requiredCourses.add(new Course("CS 7637"));
		requiredCourses.add(new Course("CS 6310"));
		requiredCourses.add(new Course("CS 8803"));
		requiredCourses.add(new Course("CS 4495"));
		requiredCourses.add(new Course("CS 6210"));
		requiredCourses.add(new Course("CS 6505"));
		requiredCourses.add(new Course("CS 6250"));
		requiredCourses.add(new Course("CS 6290"));
		requiredCourses.add(new Course("CS 6035"));
		requiredCourses.add(new Course("CS 7646"));

		ret.setRequiredCourses(requiredCourses);

		// Create prereq data
		HashMap<String, String> prerequisite = new HashMap<String, String>();

		prerequisite.put("CS 7641", "CS 7646");
		prerequisite.put("CS 6300", "CS 6440");

		ret.setPrerequisite(prerequisite);

		// Create correq data
		HashMap<String, String> corequisite = new HashMap<String, String>();

		ret.setCorequisite(corequisite);

		// Create dummy enrollment limit data
		HashMap<String, Integer> enrollmentLimit = new HashMap<String, Integer>();

		enrollmentLimit.put("CS 6300", 550);
		enrollmentLimit.put("CS 6310", 550);

		ret.setEnrollmentLimit(enrollmentLimit);

		// Set default enrollment limit

		ret.setDefaultEnrollmentLimit(500);

		// Create student data from test file
		List<Student> students = new ArrayList<Student>();

		int numberStudents = 0;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(studentInputs);
			br = new BufferedReader(fr);

			String line;
			int lineNum = 0;

			// Read each line
			while ((line = br.readLine()) != null) {
				if (line.trim() != null && line.trim().length() > 0
						&& lineNum > 0) {
					Student s = null;

					String[] tokens = line.split("[,]+");
					String id = (tokens[0] + "," + tokens[1] + "," + tokens[2])
							.replace("\"", "");

					if (numberStudents - 1 >= 0
							&& students.get(numberStudents - 1).getId()
									.equals(id)) {
						// new line is not a new student
						s = students.get(numberStudents - 1);
					} else {
						// new line is a new student, create a student with id,
						// password, and 5 randomly prioritized courses
						s = new Student(id);

						s.setPwd("password" + id.replaceAll(",", ""));

						s.setDesiredCourses(5);

						HashMap<String, Integer> nextSemester = new HashMap<String, Integer>();
						String randCourse = getRandomCourse(rand,
								requiredCourses, nextSemester);
						nextSemester.put(randCourse, 1);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 2);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 3);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 4);
						randCourse = getRandomCourse(rand, requiredCourses,
								nextSemester);
						nextSemester.put(randCourse, 5);

						s.setNextSemester(nextSemester);

						students.add(s);

						s.setCoursesTaken(new ArrayList<String>());

						s.setSeniority(0);
						numberStudents++;
					}

					// Add the course if the student didn't withdraw and
					// increase the student's seniority
					if (tokens.length == 9) {
						s.getCoursesTaken().add(tokens[4]);
						s.setSeniority(s.getSeniority() + 1);
					}
				}
				lineNum++;
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ret.setStudents(students);

		return ret;
	}

	private String getRandomCourse(Random rand,
			ArrayList<Course> requiredCourses,
			HashMap<String, Integer> desiredCourse) {
		String randCourse = requiredCourses.get(
				rand.nextInt(requiredCourses.size())).getName();
		while (desiredCourse.containsKey(randCourse)) {
			randCourse = requiredCourses.get(
					rand.nextInt(requiredCourses.size())).getName();
		}
		return randCourse;
	}
}

class PriorityComp implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		// TODO Auto-generated method stub
		return Integer.valueOf(s1.charAt(s1.length() - 1)) - Integer.valueOf(s2.charAt(s2.length() - 1));
	}
	
}
