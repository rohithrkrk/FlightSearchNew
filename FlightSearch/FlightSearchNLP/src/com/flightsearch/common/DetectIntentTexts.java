package com.flightsearch.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
// [END dialogflow_import_libraries]

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;


/**
 * DialogFlow API Detect Intent sample with text inputs.
 */
/**
 * @author RoHiT
 *
 */
public class DetectIntentTexts {

  // [START dialogflow_detect_intent_text]
  /**
   * Returns the result of detect intent with texts as inputs.
   *
   * Using the same `session_id` between requests allows continuation of the conversation.
   * @param projectId Project/Agent Id.
   * @param texts The text intents to be detected based on what a user says.
   * @param sessionId Identifier of the DetectIntent session.
   * @param languageCode Language code of the query.
   */
  public static DetailsDTO detectIntentTexts(String projectId, List<String> texts, String sessionId,
      String languageCode) throws Exception {
    // Instantiates a client
	  DetailsDTO dto=new DetailsDTO();
    try (SessionsClient sessionsClient = SessionsClient.create()) {
      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
      SessionName session = SessionName.of(projectId, sessionId);
      System.out.println("Session Path: " + session.toString());

      // Detect intents for each text input
      for (String text : texts) {
        // Set the text (hello) and language code (en-US) for the query
        Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

        // Build the query with the TextInput
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        // Performs the detect intent request
        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

        // Display the query result
        QueryResult queryResult = response.getQueryResult();
        Struct queryParam= queryResult.getParameters();
	     Map<String,Value> mapValue=queryParam.getFieldsMap();
	     //DetailsDTO dto=new DetailsDTO();
	     String from="";
	     String to="";
	     String depDate="";
	      for (Map.Entry<String,Value> entry : mapValue.entrySet()){  
	          //  System.out.println("Key = " + entry.getKey() + 
	            //                 ", Value = " + entry.getValue());
	            
	           if( entry.getKey().equals("from")){
	        	  // Struct fromMap=
	        	   if(entry.getValue().hasStructValue()){
	        		   entry.getValue().getStringValue();
	        		   Map<String,Value> struc=entry.getValue().getStructValue().getFieldsMap();
	        		   //System.out.println(struc);
	        		   for (Map.Entry<String,Value> strucOne : struc.entrySet()){
	        			   
	        			   String arr[]=strucOne.getValue().toString().split(":");
	        			  from= arr[1];
	        			  if(arr.length>0){
	        				  from= FlightSearchUtil.correctParams(arr[1].trim());
	        				  from.replaceFirst(" ","");
	        				  from.replace("\n","");
	            			  //System.out.println("From location"+to);
	            		   }
	        			 // System.out.println(from);
	        		   }
	        		   }
	        	   }
	        	   
	           
    if( entry.getKey().equals("to")){
   	   if(entry.getValue().hasStructValue()){
   		   entry.getValue().getStringValue();
   		   Map<String,Value> struc=entry.getValue().getStructValue().getFieldsMap();
   		   //System.out.println(struc);
   		   for (Map.Entry<String,Value> strucOne : struc.entrySet()){
   			  // System.out.println("Key = " + strucOne.getKey() + 
               //             ", Value = " + strucOne.getValue());
   			   String arr[]=strucOne.getValue().toString().split(":");
   			   if(arr.length>0){
   			  to= FlightSearchUtil.correctParams(arr[1].trim());
   			  to.replaceFirst(" ","");
   			  to.replace("\n","");
   			 // System.out.println("To location"+to);
   		   }
   		   }
	           }
	      }
    
    if( entry.getKey().equals("departure")){
    	   
    		 String dep=  entry.getValue().getStringValue();
    			   String arr[]=dep.split("T");
    			   if(arr.length>0){
    				   depDate= FlightSearchUtil.correctParams(arr[0].trim());
    		   }
 	           
 	      }
	      }
	      
	      dto.setFrom(from);
	      dto.setTo(to);
	      dto.setDepartureDate(depDate);

        System.out.println("====================");
        System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
        System.out.println("Departure date----"+depDate);
	      System.out.println("From value-----"+from);
	      System.out.println("To value--------"+to);
        System.out.format("Detected Intent: %s (confidence: %f)\n",
            queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
        System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
        
      }
   }
    return dto;
  }
  
 /* public static String correctParams(String arg){
	  if(arg.contains(" ")){
		  String[] split=  arg.split(" ");
		  return split[0];
	  }
	  else if(arg.contains("\\")){
		String[] split=  arg.split("\\\\");
		if(split[0].startsWith("\"")){
			split[0]=split[0].replace("\"","");
			return split[0];
		}else{
			return split[0];
		}
	  }else if(arg.startsWith("\"")||arg.endsWith("\"")){
		  return arg.replaceAll("\"", "");
	  }
	  
	  else{
		  return arg;
   }
  }*/
  // [END dialogflow_detect_intent_text]

  // [START run_application]
  public static void main(String[] args) throws Exception {
    ArrayList<String> texts = new ArrayList<>();
    

    String projectId = "flights-e0ff3";
    String sessionId = UUID.randomUUID().toString();
    String languageCode = "en-US";

    texts.add("NRT to CMB");

    detectIntentTexts(projectId, texts, sessionId, languageCode);
  }
  // [END run_application]
}