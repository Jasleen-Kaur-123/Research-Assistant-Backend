package com.research.assistant;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data  
//If there are any other property returned by API but that property unknow to particular class instead of throw error it will ignore those properties.  
@JsonIgnoreProperties(ignoreUnknown = true) 
public class GeminiResponse { // We are creating class which representing nested JSON Form.
	private List<Candidate> candidates;
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public static class Candidate{ //This Candidate is the part of GeminiResponse
		private Content content;
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public static class Content{ //This Content class is part of Candidate
		private List<Part> parts;
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public static class Part{ //This Part class is part of Content
		private String text;
	}
}
