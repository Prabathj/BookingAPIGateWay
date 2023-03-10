package com.prabathj.bookinggw;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.prabathj.bookinggw.reqhandler.APIRequestHandler;
import com.sun.net.httpserver.HttpServer;


public class AppServer 
{
    public static void main( String[] args )
    {
    	try {
	        int serverPort = 5030;
	        HttpServer server;
			server = HttpServer.create(new InetSocketAddress("localhost",serverPort), 0);
			
			APIRequestHandler apiRequestHandler = new APIRequestHandler( AppServerConfiguration.getObjectMapper(),
					AppServerConfiguration.getErrorHandler());			
			
	        server.createContext("/api/booking", apiRequestHandler::handle);
	        server.setExecutor(Executors.newFixedThreadPool(10)); 
	        server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}


