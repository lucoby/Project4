package project4.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import project4.login.model.AdminModel;
import project4.login.model.LoginModel;
import project4.login.model.StudentModel;

@Controller
@RequestMapping("/login")
public class AppController {
    Logger logger = Logger.getLogger(AppController.class);

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
	return "adminMain";
    }

    @RequestMapping(value = "/studentView", method = RequestMethod.POST)
    public String studentDataSubmission(
	    @ModelAttribute("studentModel") StudentModel student, Model model) {

	for (String s : student.getCourses()) {
	    logger.info(s);
	}

	// TODO use service to update params for LP

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
	    final BindingResult bindingResult) {
	student.getCourses().add(new String());
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

}
