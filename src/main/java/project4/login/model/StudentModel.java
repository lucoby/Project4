package project4.login.model;

import org.springframework.data.annotation.Id;

public class StudentModel {
    
    @Id
    private String id;
    
    public StudentModel(String id){
	this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
