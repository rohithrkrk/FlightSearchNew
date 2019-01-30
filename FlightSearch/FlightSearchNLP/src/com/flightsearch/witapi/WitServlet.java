package com.flightsearch.witapi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
public class WitServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String searchString=req.getParameter("searchString");
		getApiResponse(searchString);
		
		super.doPost(req, resp);
	}

	
	
	public static void getApiResponse(String searchString) throws IOException {
	    String url = "https://api.wit.ai/speech";
	    String key = "3041268802566135";

	    String param1 = "20190130";
	    String param2 = "command";
	    String charset = "UTF-8";

	    String query = String.format("v=%s",
	            URLEncoder.encode(param1, charset));


	    URLConnection connection = new URL(url + "?" + query).openConnection();
	    connection.setRequestProperty ("Authorization","Bearer " + key);
	    //connection.setRequestProperty("Content-Type", "audio/wav");
	    connection.setDoOutput(true);
	    OutputStream outputStream = connection.getOutputStream();
	    //connection
	    outputStream.write(searchString.getBytes());
	   /* FileChannel fileChannel = new FileInputStream("").getChannel();*/
	  /*  ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

	    while((fileChannel.read(byteBuffer)) != -1) {
	        byteBuffer.flip();
	        byte[] b = new byte[byteBuffer.remaining()];
	        byteBuffer.get(b);
	        outputStream.write(b);
	        byteBuffer.clear();
	    }*/

	    BufferedReader response = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String line;
	    while((line = response.readLine()) != null) {
	        System.out.println(line);
	    }
	}
}
