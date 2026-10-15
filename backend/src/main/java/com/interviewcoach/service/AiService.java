package com.interviewcoach.service;
import com.fasterxml.jackson.databind.*;import org.springframework.beans.factory.annotation.Value;import org.springframework.http.*;import org.springframework.stereotype.Service;import org.springframework.web.client.RestClient;import org.springframework.web.server.ResponseStatusException;import java.util.*;
@Service public class AiService {
 private final ObjectMapper mapper;private final String key,model,url;private final RestClient client=RestClient.create();
 public AiService(ObjectMapper mapper,@Value("${app.ai-key}")String key,@Value("${app.ai-model}")String model,@Value("${app.ai-url}")String url){this.mapper=mapper;this.key=key;this.model=model;this.url=url;}
 public boolean available(){return key!=null&&!key.isBlank();}
 public String ask(String instruction,String input){if(!available())throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Ollama configuration is missing");try{
  Map<String,Object> body=Map.of("model",model,"temperature",0.3,"messages",List.of(Map.of("role","system","content",instruction),Map.of("role","user","content",input)));
  String response=client.post().uri(url).header("Authorization","Bearer "+key).contentType(MediaType.APPLICATION_JSON).body(body).retrieve().body(String.class);
  JsonNode choices=mapper.readTree(response).path("choices");String result=choices.path(0).path("message").path("content").asText();if(result.isBlank())throw new IllegalStateException("Empty AI response");return result;
 }catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Local Ollama request failed. Check that Ollama is running and the model is installed",e);}}
 public JsonNode askJson(String system,String input){String result=ask(system+" Reply with a valid JSON object only; no markdown fences.",input);try{return mapper.readTree(result.replaceAll("(?s)^```(?:json)?\\s*|\\s*```$",""));}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"AI returned invalid JSON",e);}}
}
