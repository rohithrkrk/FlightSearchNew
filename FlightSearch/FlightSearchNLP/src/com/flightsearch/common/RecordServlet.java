package com.flightsearch.common;

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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;



@WebServlet("/MyServlet")
@MultipartConfig
public class RecordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RecordServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
        	String searchString=request.getParameter("searchString");
            InputStream input = request.getInputStream(); 
            byte[] bytes = IOUtils.toByteArray(input);
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
