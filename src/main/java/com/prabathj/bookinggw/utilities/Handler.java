package com.prabathj.bookinggw.utilities;

import java.io.File;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabathj.bookinggw.utilities.exception.InvalidRequestException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public abstract class Handler {
	
    private final ObjectMapper objectMapper;
    private final GlobalExceptionHandler exceptionHandler;

    public Handler(ObjectMapper objectMapper,
                   GlobalExceptionHandler exceptionHandler) {
        this.objectMapper = objectMapper;
        this.exceptionHandler = exceptionHandler;
    }



    protected <T> T readRequest(InputStream is, Class<T> type) {
    	try {
			return objectMapper.readValue(is, type);
		} catch (Exception e) {

			throw new InvalidRequestException(400, e.getMessage());
		}

    }
    
    protected <T> T readFile(File is, Class<T> type) {
    	try {
			return objectMapper.readValue(is, type);
		} catch (Exception e) {

			throw new InvalidRequestException(400, e.getMessage());
		}

    }

    protected <T> byte[] writeResponse(T response) {
    	
    	try {
			return objectMapper.writeValueAsBytes(response);
		} catch (Exception e) {
	    	
			throw new InvalidRequestException(400, e.getMessage());
		}

    }
    
    public void handle(HttpExchange exchange) {
    	try {
    		this.execute(exchange);
    	}catch(Exception ex) {
    		exceptionHandler.handle(ex, exchange);
    		exchange.close();
    	}

    }

    protected abstract void execute(HttpExchange exchange) throws Exception;    
    

    protected static Headers getHeaders(String key, String value) {
        Headers headers = new Headers();
        headers.set(key, value);
        return headers;
    }
    
}
