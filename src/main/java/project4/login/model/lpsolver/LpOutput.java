package project4.login.model.lpsolver;

import org.springframework.data.annotation.Id;

public class LpOutput {
	
    @Id
    private String id;
    
	private int[][] suggestion;
	
	private String[] suggestionS;

	public int[][] getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(int[][] suggestion) {
		this.suggestion = suggestion;
	}

	public String[] getSuggestionS() {
		return suggestionS;
	}

	public void setSuggestionS(String[] suggestionS) {
		this.suggestionS = suggestionS;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
