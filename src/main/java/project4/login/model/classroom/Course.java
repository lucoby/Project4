package project4.login.model.classroom;

import java.util.List;

public class Course {

    private String id;
    private String name;

    private String profId;

    private List<String> taIds;

    public Course(String name) {
	this.name = name;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getProfId() {
	return profId;
    }

    public void setProfId(String profId) {
	this.profId = profId;
    }

    public List<String> getTaIds() {
	return taIds;
    }

    public void setTaIds(List<String> taIds) {
	this.taIds = taIds;
    }

}
