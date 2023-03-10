package com.prabathj.hotelavailabilitysrv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.Executors;

import com.prabathj.bookinggw.utilities.exception.ApplicationException;
import com.prabathj.hotelavailabilitysrv.model.ConfigData;
import com.prabathj.hotelavailabilitysrv.reqhandler.AvailabilityReqHandler;
import com.sun.net.httpserver.HttpServer;

public class AvailabilityService {

	public static void main(String[] args) {
    	try {
	        int serverPort = 5032;
	        HttpServer server;
	        ConfigData data=new ConfigData(10);
			try {
				data = AvailabilityService.configuredata();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        AvailabilityReqHandler apiRequestHandler = new AvailabilityReqHandler( AvailabilityServerConfiguration.getObjectMapper(),
	        		AvailabilityServerConfiguration.getErrorHandler(),data);		        
			server = HttpServer.create(new InetSocketAddress("localhost",serverPort), 0);
	        server.createContext("/api/book", apiRequestHandler::handle);
	        server.setExecutor(Executors.newFixedThreadPool(10)); 
	        server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static ConfigData configuredata() throws IOException, InterruptedException, ClassNotFoundException {
		
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create("http://localhost:5030/api/booking?config"))
				  .header("Content-Type", "application/json")
				  .header("Accpet","application/json")
				  .GET()
				  .build();		
		
		HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());
		
	    try (ByteArrayInputStream bis = new ByteArrayInputStream(response.body());
	            ObjectInputStream in = new ObjectInputStream(bis)) {
	    		ConfigData config= (ConfigData) in.readObject();
	    		
	    		if(config==null || (config!=null && config.getNo_rooms()<=0)) {
	    			return new ConfigData(10);
	    		}
	    		
	    		return config;	    		
	       } 

		

	}

}
