package com.prabathj.bookingsrv;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.prabathj.bookingsrv.reqhandler.BookRequestHandler;

import com.sun.net.httpserver.HttpServer;

public class BookingService {

	public static void main(String[] args) {
    	try {
	        int serverPort = 5031;
	        HttpServer server;
	        BookRequestHandler apiRequestHandler = new BookRequestHandler( BookingServerConfiguration.getObjectMapper(),
	        		BookingServerConfiguration.getErrorHandler());	
	        
			server = HttpServer.create(new InetSocketAddress("localhost",serverPort), 0);
	        server.createContext("/api/booking", apiRequestHandler::handle);
	        server.setExecutor(Executors.newFixedThreadPool(10)); 
	        server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
