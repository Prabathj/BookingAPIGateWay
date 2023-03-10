package com.prabathj.bookinggw.reqhandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabathj.bookinggw.utilities.Constants;
import com.prabathj.bookinggw.utilities.GlobalExceptionHandler;
import com.prabathj.bookinggw.utilities.Handler;
import com.prabathj.bookinggw.utilities.exception.ApplicationException;
import com.prabathj.bookinggw.utilities.exception.MethodNotAllowedException;
import com.prabathj.hotelavailabilitysrv.model.ConfigData;
import com.sun.net.httpserver.HttpExchange;

public class APIRequestHandler extends Handler{

	public APIRequestHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
		super(objectMapper, exceptionHandler);
	}

	public void execute(HttpExchange exchange) throws IOException {

	    
	    if("GET".equals(exchange.getRequestMethod())) { 
	    
	    	handleGetRequest(exchange);

	    }else if("POST".equals(exchange.getRequestMethod())) { 
	    
	        handlePostRequest(exchange);        
	    
	    }else {
	    	throw new MethodNotAllowedException(405,"Method " + exchange.getRequestMethod() + 
	    			" is not allowed for " + exchange.getRequestURI());
	    }  

	}
	
	private void handleGetRequest(HttpExchange httpExchange) {
		
		try {
		
			String parameter=httpExchange.getRequestURI().toString().split("\\?")[1];	
			if(parameter.equals("config")) {
				ConfigData data = readFile(new File("data/configdata.json"),ConfigData.class);
				handleCOnfigResponse(httpExchange,data);
			}else {
				HttpClient client = HttpClient.newHttpClient();
				
				HttpRequest request = HttpRequest.newBuilder()
						  .uri(URI.create("http://localhost:5032/api/book?"+parameter))
						  .header("Content-Type", "application/json")
						  .header("Accpet","application/json")
						  .GET()
						  .build();		
				
				client.sendAsync(request, BodyHandlers.ofByteArray()).thenAccept(response ->{
	
						try {
							this.handleGetResponse(httpExchange, response);
						} catch (IOException e) {
							throw new ApplicationException(500,e.getMessage());
						}
	
				});
			}
		}catch(Exception ex) {
			throw new ApplicationException(422,ex.getMessage());
		}

	}
	
	private void handlePostRequest(HttpExchange httpExchange) {
		
		try{
			HttpClient client = HttpClient.newHttpClient();
			
			HttpRequest request = HttpRequest.newBuilder()
					  .uri(URI.create("http://localhost:5031/api/booking"))
					  .header("Content-Type", "application/json")
					  .POST(HttpRequest.BodyPublishers.ofInputStream(()-> httpExchange.getRequestBody()))
					  .build();
			
			client.sendAsync(request, BodyHandlers.ofByteArray()).thenAccept(response->{
				try {
					this.handleGetResponse(httpExchange, response);
				} catch (IOException e) {
					throw new ApplicationException(500,e.getMessage());
				}
			});		
		}catch(Exception ex) {
			throw new ApplicationException(422,ex.getMessage());
		}
	}	

	
	private void handleGetResponse(HttpExchange httpExchange, HttpResponse<byte[]> responseOfByteArray)  throws  IOException {
				
		httpExchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
		httpExchange.sendResponseHeaders(responseOfByteArray.statusCode(), 0);
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(responseOfByteArray.body());
		outputStream.flush();
		outputStream.close();
		httpExchange.close();
    }	
		
	private void handleCOnfigResponse(HttpExchange httpExchange, ConfigData data)  throws  IOException {
		
		httpExchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
		httpExchange.sendResponseHeaders(200, 0);
		OutputStream outputStream = httpExchange.getResponseBody();
		
	    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            ObjectOutputStream out = new ObjectOutputStream(bos)) {
	           
	    	out.writeObject(data);   
	   		outputStream.write(bos.toByteArray());
			outputStream.flush();
			outputStream.close();
			httpExchange.close();	           
	           
	       } 
		

    }		
}
