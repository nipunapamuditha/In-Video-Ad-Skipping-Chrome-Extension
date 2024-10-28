package com.newsloop.newsloop_be.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsloop.newsloop_be.service.UserService;

import io.github.thoroldvix.api.Transcript;
import io.github.thoroldvix.api.TranscriptContent;
import io.github.thoroldvix.api.TranscriptList;
import io.github.thoroldvix.api.TranscriptRetrievalException;
import io.github.thoroldvix.api.YoutubeTranscriptApi;
import io.github.thoroldvix.internal.TranscriptApiFactory;

import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {


    private static final String API_KEY = "empty string for security reasons";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

 

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @CrossOrigin(origins = {"http://localhost", "https://www.youtube.com"})
    @PostMapping("/getadtimestamps")
    @ResponseBody
    public double[][] getadtimestamps(@RequestBody Map<String, String> requestParams) throws IOException, InterruptedException, TranscriptRetrievalException {
        String videoid = requestParams.get("videoid");
        String transcripString = requestParams.get("transcript");
        // need to get transcript 

        double timestampsfromapi[][] = fetchSegmentTimestamps(transcripString);

        System.out.println("printing timestampsfromapi");

        System.out.println(timestampsfromapi[0][0]);
        

    
      //  JSONObject jsonObject = new JSONObject(videoid);
    //    videoid = jsonObject.getString("videoid");
        System.out.println("printing videoid");
        System.out.println(videoid);

        String output = "xxt";

        double[][] arrayz;


        if (timestampsfromapi[0][1] == 0.0) {


            System.out.println("using gemini to transcript analyzsis");

            output= getGeminiAIResponse(videoid);

            output = convertTimestampsToSeconds(output);
            // Your code here

            arrayz = convertStringToArray(output);
        }
        else {
            arrayz = timestampsfromapi;
        }



       // System.out.println("user name: " + videoid);    

        

        System.out.println(output);

        String[][] timestamps = {
            {"timesdsa", "ddd"},
            {"timestaadsad", "timestaadca"}
        };
        
    
        
        return arrayz;
    }



    public static double[][] convertStringToArray(String arrayString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(arrayString, double[][].class);
    }



@SuppressWarnings("deprecation")
private double[][] fetchSegmentTimestamps(String videoid) throws IOException {
    String url = "https://sponsor.ajay.app/api/skipSegments?videoID=" + videoid;

    try {
        // Make the API request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Check if the response status code is not 200
        if (response.getStatusCodeValue() != 200) {
            System.out.println("API response code is not 200. Returning default value.");
            return new double[][]{{0.0, 0.0}};
        }

        String modifiedBody = response.getBody().replaceAll("\"segment\":(\\d+),(\\d+\\.\\d+)", "\"segment\":[\"$1\",\"$2\"]");
        ResponseEntity<String> modifiedResponse = new ResponseEntity<>(modifiedBody, response.getStatusCode());

        System.out.println("Modified Response Body: " + modifiedResponse.getBody());

        // Parse the JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(modifiedResponse.getBody());
        Iterator<JsonNode> elements = rootNode.elements();

        List<double[]> segmentsList = new ArrayList<>();

        while (elements.hasNext()) {
            JsonNode node = elements.next();
            if (node.has("segment")) {
                JsonNode segmentNode = node.get("segment");
                double start = segmentNode.get(0).asDouble();
                double end = segmentNode.get(1).asDouble();
                segmentsList.add(new double[]{start, end});
                System.out.println("Parsed segment: [" + start + ", " + end + "]");
            }
        }

        // Convert List to 2D array
        double[][] segmentsArray = new double[segmentsList.size()][2];
        for (int i = 0; i < segmentsList.size(); i++) {
            segmentsArray[i] = segmentsList.get(i);
        }

        // Print the 2D array for troubleshooting
        System.out.println("Segments 2D Array:");
        for (double[] segment : segmentsArray) {
            System.out.println("[" + segment[0] + ", " + segment[1] + "]");
        }

        System.out.println("End function body");

        return segmentsArray;

    } catch (HttpClientErrorException.NotFound e) {
        System.out.println("API returned 404 Not Found. Returning default value.");
        return new double[][]{{0.0, 0.0}};
    } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
        throw e;
    }
}



 

    public static String convertTimestampsToSeconds(String output) {
        Pattern pattern = Pattern.compile("(\\d+):(\\d+)");
        Matcher matcher = pattern.matcher(output);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            int minutes = Integer.parseInt(matcher.group(1));
            int seconds = Integer.parseInt(matcher.group(2));
            int totalSeconds = minutes * 60 + seconds;
            matcher.appendReplacement(result, String.valueOf(totalSeconds));
        }
        matcher.appendTail(result);

        return result.toString();
    }




    public static String getOpenAIResponse(String payloadStr) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
    
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
            Map.of("role", "system", "content", 
            "You are an assistant that processes YouTube video transcripts. Your task is to examine the given transcript to identify the start and end timestamps of sponsored segments. " +
             
            "Provide the results as a 2D array with start and end times , like this: [[start1, end1], [start2, end2], ...]. " +
            "If no sponsored segments are found, return [[0,0]]."
        ),
            Map.of("role", "user", "content", payloadStr) // payloadStr contains the actual transcript
        ));
        
        // Use the requestBody in your API call
        
    
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();


        String content = "[[0,0]]";

        if (responseBody.contains("\"code\": \"context_length_exceeded\"")) {
                 System.out.println("xyz");
            }

            
        else {
            JSONObject jsonObject = new JSONObject(responseBody);
            System.out.println(responseBody);
            content = jsonObject.getJSONArray("choices")
                                       .getJSONObject(0)
                                       .getJSONObject("message")
                                       .getString("content");
    
            
    
            System.out.println("bodyyyyyyyy");
    
            System.out.println(content);
        }

        
    
    
        return content;
    }




    public static String getGeminiAIResponse(String payloadStr) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=emptystringforsecurityreasons"))
            .POST(BodyPublishers.ofString("{\n  \"contents\": [\n    {\n      \"role\": \"user\",\n      \"parts\": [\n        {\n          \"text\": \"" + payloadStr + "\"\n        }\n      ]\n    }\n],\n  \"systemInstruction\": {\n    \"role\": \"user\",\n    \"parts\": [\n      {\n        \"text\": \"You are an assistant that processes YouTube video transcripts. Your task is to examine the given transcript to identify the start and end timestamps of sponsored segments. The timestamps are provided in the transcript in minutes and seconds format. Present the results as a 2D array with start and end times, like this: [[1:34, 2:32], [10:22, 12:33], ...]. If no sponsored segments are found, return [[0, 0]].\"\n      }\n    ]\n  },\n  \"generationConfig\": {\n    \"temperature\": 1,\n    \"topK\": 64,\n    \"topP\": 0.95,\n    \"maxOutputTokens\": 8192,\n    \"responseMimeType\": \"text/plain\"\n  }\n}"))
            .setHeader("Content-Type", "application/json")
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Parse the JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseBody);
        String result = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        // Extract the desired content
        result = result.replace("```python\n", "").replace("\n``` \n", "").trim();

        return result;
    }

}

