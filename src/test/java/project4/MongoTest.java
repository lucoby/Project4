package project4;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import project4.login.model.StudentModel;
import project4.service.PersistenceService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Project4Application.class)
@Transactional
public class MongoTest {
   @Autowired
    private PersistenceService pservice;

    @Test
    public void test() {
	StudentModel student1 = new StudentModel("123");
	
	pservice.saveToDB(student1);
	StudentModel studentobj= pservice.findById("123");
	
	assertNotEquals(studentobj, null);
	assertEquals(studentobj.getId(), "123");
	
	
    }
    @After
    public void after(){
	pservice.removeAll();
    }

}
