package com.flightsearch.common;

import java.util.Map;
import java.util.UUID;

import com.google.cloud.dialogflow.v2.AudioEncoding;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.InputAudioConfig;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.protobuf.ByteString;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

/**
 * @author RoHiT
 *
 */
/**
 * @author RoHiT
 *
 */
public class DetectIntentAudio {
	  /**
	   * Returns the result of detect intent with an audio file as input.
	   *
	   * Using the same `session_id` between requests allows continuation of the conversation.
	   * @param projectId Project/Agent Id.
	   * @param audioFilePath Path to the audio file.
	   * @param sessionId Identifier of the DetectIntent session.
	   * @param languageCode Language code of the query.
	   */
	  public static DetailsDTO detectIntentAudio(String projectId, byte[] audioFilePath, String sessionId,
	      String languageCode)
	      throws Exception {
		  
		  
	    // Instantiates a client
	    SessionsClient sessionsClient = SessionsClient.create(); 
	      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
	      SessionName session = SessionName.of(projectId, sessionId);
	      System.out.println("Session Path: " + session.toString());

	      // Note: hard coding audioEncoding and sampleRateHertz for simplicity.
	      // Audio encoding of the audio content sent in the query request.
	      AudioEncoding audioEncoding = AudioEncoding.AUDIO_ENCODING_LINEAR_16;
	      //int sampleRateHertz = 16000;

	      // Instructs the speech recognizer how to process the audio content.
	      InputAudioConfig inputAudioConfig = InputAudioConfig.newBuilder()
	          .setAudioEncoding(audioEncoding) // audioEncoding = AudioEncoding.AUDIO_ENCODING_LINEAR_16
	          .setLanguageCode(languageCode).build();// languageCode = "en-US"
	           // sampleRateHertz = 16000
	          

	      // Build the query with the InputAudioConfig
	      QueryInput queryInput = QueryInput.newBuilder().setAudioConfig(inputAudioConfig).build();

	      // Read the bytes from the audio file
	    // byte[] inputAudio = Files.readAllBytes(Paths.get(audioFilePath));
            FlightSearchUtil.transcribeMultiLanguage(audioFilePath);
	      // Build the DetectIntentRequest
	      DetectIntentRequest request = DetectIntentRequest.newBuilder()
	          .setSession(session.toString())
	          .setQueryInput(queryInput)
	          .setInputAudio(ByteString.copyFrom(audioFilePath))
	          .build();

	      // Performs the detect intent request
	      DetectIntentResponse response = sessionsClient.detectIntent(request);

	      // Display the query result
	      QueryResult queryResult = response.getQueryResult();
	      //Map<FieldDescriptor,Object> map=queryResult.getAllFields();
	      System.out.println("====================");
	      Struct queryParam= queryResult.getParameters();
	     Map<String,Value> mapValue=queryParam.getFieldsMap();
	     String from="";
	     String to="";
	     String depDate="";
	      for (Map.Entry<String,Value> entry : mapValue.entrySet()){  
	            System.out.println("Key = " + entry.getKey() + 
	                             ", Value = " + entry.getValue());
	            
	           if( entry.getKey().equals("from")){
	        	  // Struct fromMap=
	        	   if(entry.getValue().hasStructValue()){
	        		   entry.getValue().getStringValue();
	        		   Map<String,Value> struc=entry.getValue().getStructValue().getFieldsMap();
	        		   System.out.println(struc);
	        		   for (Map.Entry<String,Value> strucOne : struc.entrySet()){
	        			  // System.out.println("Key = " + strucOne.getKey() + 
		                    //         ", Value = " + strucOne.getValue());
	        			   String arr[]=strucOne.getValue().toString().split(":");
	        			  if(arr.length>0){
	        				 from=from.replaceFirst(" ","");
	        				  from=from.replace("\n","");
	        				  from= FlightSearchUtil.correctParams(arr[1].trim());
	        				 
	            			 // System.out.println("From location"+from);
	            		   }
	        			 // System.out.println(from);
	        		   }
	        		   }
	        	   }
	        	   
	           
     if( entry.getKey().equals("to")){
    	   if(entry.getValue().hasStructValue()){
    		   entry.getValue().getStringValue();
    		   Map<String,Value> struc=entry.getValue().getStructValue().getFieldsMap();
    		 //  System.out.println(struc);
    		   for (Map.Entry<String,Value> strucOne : struc.entrySet()){
    			   System.out.println("Key = " + strucOne.getKey() + 
                             ", Value = " + strucOne.getValue());
    			   String arr[]=strucOne.getValue().toString().split(":");
    			   if(arr.length>0){
    				 to=  arr[1].replaceFirst(" ","");
    	    			to=  arr[1].replace("\n","");
    			  to= arr[1].trim();
    			 
    			 // System.out.println("To location"+to);
    		   }
    		   }
	           }
	      }
     if( entry.getKey().equals("departure")){
  	   //if(entry.getValue().hasStructValue()){
  		 String dep=  entry.getValue().getStringValue();
  		   //Map<String,Value> struc=entry.getValue().getStructValue().getFieldsMap();
  		   System.out.println(entry.getValue().getStringValue());
  		  // for (Map.Entry<String,Value> strucOne : struc.entrySet()){
  			   //System.out.println("Key = " + strucOne.getKey() + 
                          // ", Value = " + strucOne.getValue());
  			   String arr[]=dep.split("T");
  			   if(arr.length>0){
  				   depDate= arr[0].trim();
  			 // to.replaceFirst(" ","");
  			  //to.replace("\n","");
  			  //System.out.println("Departure date"+depDate);
  		  // }
  		   }
	           //}
	      }
	  }
	      DetailsDTO dto=new DetailsDTO();
	      dto.setFrom(from);
	      dto.setTo(to);
	      dto.setDepartureDate(depDate);
	      
	      System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
	      System.out.format("Detected Intent: %s (confidence: %f)\n",
	      queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
	      System.out.println("Departure date----"+depDate);
	      System.out.println("From value-----"+from);
	      System.out.println("To value--------"+to);
	      System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
	      return dto;
	    }
	  
	  // [END dialogflow_detect_intent_audio]

	  // [START run_application]
	  public static void main(String[] args) throws Exception {
	    String audioFilePath = "C:\\Users\\RoHiT\\Downloads\\2019-01-23T09_37_33.159Z.wav";
	    String projectId = "air-search-agent";
	    String sessionId = UUID.randomUUID().toString();
	    String languageCode = "en-US";

		
	    System.out.print(System.getenv("JAVA_HOME"));
	    System.out.print(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
	 // detectIntentAudio(projectId, audioFilePath, sessionId, languageCode);
	  
	  // [END run_application]
	}
}
