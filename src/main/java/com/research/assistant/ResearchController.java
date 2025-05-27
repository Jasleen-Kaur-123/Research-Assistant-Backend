package com.research.assistant;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*") //allow accessing all endpoints in controller from any frontend
@AllArgsConstructor
@Service
public class ResearchController{
	private final ResearchService researchService;
	
	@PostMapping("/process")
	public ResponseEntity<String> processContent(@RequestBody ResearchRequest request){
		String result = researchService.processContent(request);
			return ResponseEntity.ok(result);	
	}
}