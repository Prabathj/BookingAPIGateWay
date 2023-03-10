package com.prabathj.bookingsrv.reqhandler;

import java.io.IOException;
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
import com.sun.net.httpserver.HttpExchange;

public class BookRequestHandler extends Handler{

	public BookRequestHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
		super(objectMapper, exceptionHandler);
		// TODO Auto-generated constructor stub
	}
	public void execute(HttpExchange exchange) throws IOException {

	    
	    if("GET".equals(exchange.getRequestMethod())) { 
	    
	    	String requestParamValue = handleGetRequest(exchange);
	    	handleResponse(exchange,requestParamValue); 
	    	exchange.close();
	    }else if("POST".equals(exchange.getRequestMethod())) { 
 
	    	handlePostRequest(exchange);  
	    }  
		
	}
	private String handleGetRequest(HttpExchange httpExchange) {
		return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];		
	}
	
	private void handlePostRequest(HttpExchange httpExchange) throws IOException {
		
	
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create("http://localhost:5032/api/book/prebook"))
				  .header("Content-Type", "application/json")
				  .POST(HttpRequest.BodyPublishers.ofInputStream(()->httpExchange.getRequestBody()))
				  .build();
		
		client.sendAsync(request, BodyHandlers.ofByteArray()).thenAccept(response->{
			try {
				this.handleByteStreamResponse(httpExchange, response);
				this.handleBookCnfReq(httpExchange,response);
			} catch (IOException e) {
				throw new ApplicationException(500,e.getMessage());
			}
		});
		
/*		
		.thenAccept(response->{
		
			try {
				this.handleBookCnfReq(httpExchange);
			} catch (IOException e) {
				//Log
				e.printStackTrace();
				httpExchange.close();
			}
			
			
		});		
*/
	}	
	
	private void handleByteStreamResponse(HttpExchange httpExchange, HttpResponse<byte[]> responseOfByteArray)  throws  IOException {
		
		httpExchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
		httpExchange.sendResponseHeaders(responseOfByteArray.statusCode(), 0);
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(responseOfByteArray.body());
		outputStream.flush();
		outputStream.close();
    }		
	
	private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
		
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append(requestParamValue);
		
		String hResponse = htmlBuilder.toString();
		
		httpExchange.sendResponseHeaders(200, hResponse.getBytes().length);
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(hResponse.getBytes());
		outputStream.flush();
		outputStream.close();
    }	
	
	private void handleBookCnfReq(HttpExchange httpExchange,HttpResponse<byte[]> responseOfByteArray) throws IOException {
		
		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create("http://localhost:5032/api/book/confirmbook"))
				  .header("Content-Type", "application/json")
				  .POST(HttpRequest.BodyPublishers.ofByteArray(responseOfByteArray.body()))
						  //.ofInputStream(() ->httpExchange.getRequestBody()))
				  .build();		
		
		client.sendAsync(request, BodyHandlers.ofByteArray()).thenAccept(response-> httpExchange.close());

	}
	
	
}
