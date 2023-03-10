package com.prabathj.bookinggw.utilities;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabathj.bookinggw.utilities.exception.InvalidRequestException;
import com.prabathj.bookinggw.utilities.exception.MethodNotAllowedException;
import com.prabathj.bookinggw.utilities.exception.ResourceNotFoundException;
import com.sun.net.httpserver.HttpExchange;

public class GlobalExceptionHandler {
	
	
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    
    public void handle(Throwable throwable, HttpExchange exchange) {
        try {
            throwable.printStackTrace();
            exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            ErrorResponse response = getErrorResponse(throwable, exchange);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(objectMapper.writeValueAsBytes(response));
            responseBody.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ErrorResponse getErrorResponse(Throwable throwable, HttpExchange exchange) throws IOException {
    	ErrorResponse response = null;
        if (throwable instanceof InvalidRequestException) {
            InvalidRequestException exc = (InvalidRequestException) throwable;
            response=new ErrorResponse(exc.getCode(),exc.getMessage());
            exchange.sendResponseHeaders(400, 0);
        } else if (throwable instanceof ResourceNotFoundException) {
            ResourceNotFoundException exc = (ResourceNotFoundException) throwable;
            response=new ErrorResponse(exc.getCode(),exc.getMessage());
            exchange.sendResponseHeaders(404, 0);
        } else if (throwable instanceof MethodNotAllowedException) {
            MethodNotAllowedException exc = (MethodNotAllowedException) throwable;
            response=new ErrorResponse(exc.getCode(),exc.getMessage());
            exchange.sendResponseHeaders(405, 0);
        } else {
        	response=new ErrorResponse(500,throwable.getMessage());
            exchange.sendResponseHeaders(500, 0);
        }
        return response;
    }
    
}
