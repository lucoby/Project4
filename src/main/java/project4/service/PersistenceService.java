package project4.service;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project4.Mongo.Interface.LpInputRepository;
import project4.Mongo.Interface.StudentRepository;
import project4.login.model.StudentModel;
import project4.login.model.lpsolver.LpInput;

@Service
public class PersistenceService {
    Logger logger = Logger.getLogger(PersistenceService.class);
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LpInputRepository lpInputRepository;
    public void saveToDB(StudentModel student){
	studentRepository.save(student);
    }
    
    public void saveLpToDB(LpInput lpInput){
    	lpInputRepository.save(lpInput);
    }
    
    public List<StudentModel> findAll(){
	return studentRepository.findAll();
    }
    
    public StudentModel findById(String id){
	logger.info("finding student");
	return studentRepository.findById(id);
    }
    
    public LpInput findLpInput(String id){
    	logger.info("finding LpInput");
    	return lpInputRepository.findById(id);
    }
    
    public void removeAll(){
	studentRepository.deleteAll();
    }
    
   
}


