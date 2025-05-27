package com.research.assistant;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ResearchService{
	@Value("${gemini.api.url}") //Injected url from application.properties
	private String geminiApiUrl;
	
	@Value("${gemini.api.key}") //Injected key from application.properties
	private String geminiApiKey;
	
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	
	public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
		this.webClient = webClientBuilder.build(); //this refer to current class ; It helps to give instance of webclient
		this.objectMapper = objectMapper;
	}
	
	public String processContent(ResearchRequest request) { //This is the method that gives output ; query Gemini api or any ai model that will be part of gemini api
		//Build the prompt
		String prompt = buildPrompt(request);
		//Query the AI Model API
		Map<String, Object> requestBody = Map.of(
				"contents" , new Object[] {
						Map.of("parts", new Object[] {
								Map.of("text", prompt)
						})
				}
		);
		//actual API Call
		String response = webClient.post()
								   .uri(geminiApiUrl + geminiApiKey)
								   .bodyValue(requestBody)
								   .retrieve()
								   .bodyToMono(String.class)
								   .block();            //This is how make a request using WebClient
		//Parse the response 
		//Return response
		return extractTextFromResponse(response);
	}
	
	private String extractTextFromResponse(String response) {
		try {
			//pass Data/response
			GeminiResponse geminiResponse = objectMapper.readValue(response, GeminiResponse.class); //getting response mapped to geminiResponse
			//will check if gemini ai has candidates
			if(geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
				GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
				if(firstCandidate.getContent() != null 
						&& firstCandidate.getContent().getParts() != null 
						&& !firstCandidate.getContent().getParts().isEmpty()) {
					return firstCandidate.getContent().getParts().get(0).getText(); //we are finding "text" from api ; we do not rest of thing like parts,content
				}
			}
			return "No content found in response";
		}catch(Exception e) {
			return "Error Parsing: "+e.getMessage();
		}
	}

	//Working witg AI model
	private String buildPrompt(ResearchRequest request) { //It is crafting the entire prompt
		StringBuilder prompt = new StringBuilder(); 
		switch(request.getOperation()) { //based on opertion creating prompt
		case "summarize":
			//this is backend ai model ; If we want summarization in bullet we say summarize in bullets.
			//It is a AI model whatever asked for it is going to give
			prompt.append("Provide a clear and concise summary of the following text in a few sentences:\n\n");
			break;
		case "suggest":
			prompt.append("Based on the following content: suggest related topics and further reading.Format the response with clear heading and bullet points:\n\n");
			break;
		default:
			throw new IllegalArgumentException("Unknow Operation" + request.getOperation());
		}
		prompt.append(request.getContent()); // appending the content in prompt
		return prompt.toString();  //returning prompt
	}	 
}