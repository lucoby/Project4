package project4.login.model.classroom;

import java.util.Map;

/**
 * professor object
 * @author Zhang3r
 *
 */
public class Professor {
    
    private String name;
    private String id;
    private Map<String, Integer> competency;
    public Professor(){
	
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Map<String, Integer> getCompetency() {
        return competency;
    }
    public void setCompetency(Map<String, Integer> competency) {
        this.competency = competency;
    }

}
