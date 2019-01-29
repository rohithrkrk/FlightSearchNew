package com.flightsearch.webspeech;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.flightsearch.common.DetailsDTO;
import com.flightsearch.common.DetectIntentAudio;
import com.flightsearch.common.DetectIntentTexts;

public class WebspeechServlet {

    private static final long serialVersionUID = 1L;

    public WebspeechServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
        	String searchString=request.getParameter("searchString");
           // String url = request.getParameter("audio_data");
            byte[] buffer = new byte[16 * 1024];

            InputStream input = request.getInputStream(); 
            byte[] bytes = IOUtils.toByteArray(input);
            InputStream inputStreamForFile=new ByteArrayInputStream(bytes);
            File file=new File("F:/decode/Recorded.wav");
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(input, outputStream);
            outputStream.close();
            /*try (FileOutputStream fos = new FileOutputStream("F:/decode/Recorded.wav")) {
            	   fos.write(bytes);
            	}*/
           // byte[] bytes = IOUtils.toByteArray(input);
         
            /*OutputStream output = new FileOutputStream("F:/decode/abc.mp3");
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1){
                System.out.println(bytesRead);
                output.write(buffer, 0, bytesRead);
            }
           output.close();
           input.close();*/
           // byte[] bytes = url.getBytes();
            //byte[] bytesDecode= Base64.decodeBase64(bytes);
           /* try(OutputStream strem=new FileOutputStream("F:/decode/abc.wav")){
            	strem.write(bytes);
            }*/
            String projectId = "flights-e0ff3";
    	    String sessionId = UUID.randomUUID().toString();
    	    String languageCode = "en-IN";
    	    DetailsDTO details=null;
    	    List<String> textlist=new ArrayList<>();
    	    textlist.add(searchString);
    	    if(searchString!=null&&!searchString.equals("")){
    	    	details= DetectIntentTexts.detectIntentTexts(projectId, textlist, sessionId,languageCode);
    	    }else{
              details=   DetectIntentAudio.detectIntentAudio(projectId, bytes, sessionId, languageCode);
    	    }
    	    PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.write(details.getFrom().replace("\n", "")+",");
            out.write(details.getTo().replace("\n", "")+",");
            out.write(details.getDepartureDate().replace("\n", ""));
    	   
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
