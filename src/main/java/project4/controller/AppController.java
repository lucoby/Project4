package project4.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import project4.login.model.LoginModel;


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
    public String studentLoginSubmission(
	  @ModelAttribute LoginModel login,
	    Model model) {
	logger.info("id is "+login.getId());
	logger.info("pw is "+login.getPassword());
	//TODO set login to true
	return "studentMain";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String adminLoginSubmission( @ModelAttribute LoginModel login,Model model) {
	logger.info("admin id is "+login.getId());
	logger.info("admin pw is "+login.getPassword());
	//TODO set login to true
	return "adminMain";
    }

}
