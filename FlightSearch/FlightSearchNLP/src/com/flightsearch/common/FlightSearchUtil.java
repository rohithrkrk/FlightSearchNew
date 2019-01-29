package com.flightsearch.common;

import java.util.ArrayList;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.protobuf.ByteString;

public class FlightSearchUtil {
	
	 public static String correctParams(String arg){
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
	  }
	 
	 public static String translateText(String input){
		 Translate translate = TranslateOptions.getDefaultInstance().getService();

		    // The text to translate
		    String text = "Hello, world!";

		    // Translates some text into Russian
		    Translation translation =
		        translate.translate(
		            text,
		            TranslateOption.sourceLanguage("en"),
		            TranslateOption.targetLanguage("ja"));


		    System.out.printf("Text: %s%n", text);
		    System.out.printf("Translation: %s%n", translation.getTranslatedText());
		    return text;
		 
	 }
	 
	 
	 public static void transcribeMultiLanguage(byte[] content) throws Exception {
		 // Path path = Paths.get(fileName);
		  // Get the contents of the local audio file
		  //
	///	 byte[] content = Files.readAllBytes(path);

		  try (SpeechClient speechClient = SpeechClient.create()) {

		    RecognitionAudio recognitionAudio =
		        RecognitionAudio.newBuilder().setContent(ByteString.copyFrom(content)).build();
		    ArrayList<String> languageList = new ArrayList<>();
		    languageList.add("es-ES");
		    languageList.add("en-US");

		    // Configure request to enable multiple languages
		    RecognitionConfig config =
		        RecognitionConfig.newBuilder()
		            .setEncoding(AudioEncoding.LINEAR16)
		            .setSampleRateHertz(16000)
		            .setLanguageCode("ja-JP")
		            .addAllAlternativeLanguageCodes(languageList)
		            .build();
		    // Perform the transcription request
		    RecognizeResponse recognizeResponse = speechClient.recognize(config, recognitionAudio);

		    // Print out the results
		    for (SpeechRecognitionResult result : recognizeResponse.getResultsList()) {
		      // There can be several alternative transcripts for a given chunk of speech. Just use the
		      // first (most likely) one here.
		      SpeechRecognitionAlternative alternative = result.getAlternatives(0);
		      System.out.format("Transcript : %s\n\n", alternative.getTranscript());
		    }
		  }
	 }
}
