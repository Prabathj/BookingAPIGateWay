package com.prabathj.hotelavailabilitysrv.reqhandler;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prabathj.bookinggw.utilities.Constants;
import com.prabathj.bookinggw.utilities.ConvertStringtoLocaldate;
import com.prabathj.bookinggw.utilities.GlobalExceptionHandler;
import com.prabathj.bookinggw.utilities.Handler;
import com.prabathj.bookinggw.utilities.StatusCode;
import com.prabathj.bookinggw.utilities.exception.InvalidRequestException;
import com.prabathj.hotelavailabilitysrv.model.AvailableRooms;
import com.prabathj.hotelavailabilitysrv.model.BookingInfo;
import com.prabathj.hotelavailabilitysrv.model.Bookings;
import com.prabathj.hotelavailabilitysrv.model.ConfigData;
import com.prabathj.hotelavailabilitysrv.model.LocalDateCustom;
import com.sun.net.httpserver.HttpExchange;

public class AvailabilityReqHandler extends Handler{

	public AvailabilityReqHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler,ConfigData config) {
		super(objectMapper, exceptionHandler);
		NO_ROOMS=config.getNo_rooms();
	}

	public ConcurrentHashMap<LocalDateCustom,boolean[]> temporyBooking = new ConcurrentHashMap<>();
	public ConcurrentHashMap<LocalDateCustom,String[]> confimedBooking = new ConcurrentHashMap<>();
	public static int NO_ROOMS=10;
	
	
	@Override
	public void execute(HttpExchange exchange) throws IOException {
		

	    
	    if("GET".equals(exchange.getRequestMethod())) { 
	    	  String para=exchange.getRequestURI().toString().split("\\?")[1].split("=")[0];
	    	  String value=exchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
	    	  if(para.equals("date")) {	    		  
	    		  this.availableRooms(value, exchange);
	    	  }else if(para.equals("name")) {
	    		  this.searchBookings(value, exchange);
	    	  }else {
	    		 throw new InvalidRequestException(400, "Request is not allowed  " + exchange.getRequestURI());
	    	  }

	    }else if("POST".equals(exchange.getRequestMethod())) { 

			  String path = exchange.getRequestURI().getPath();
			  
			  if(path.matches("/api/book/prebook")) {
				  this.handlePreBookReq(exchange);
			  			  
			  }else if(path.matches("/api/book/confirmbook")) {
				  this.handleBookCnfReq(exchange); 
			  }else { 
				  throw new InvalidRequestException(400, "Request is not allowed  " + exchange.getRequestURI());
			  }
			  
			 
	    }else {
	    	throw new InvalidRequestException(400, "Request is not allowed  " + exchange.getRequestURI());
	    }  	  		
	    exchange.close();
	}
	
	private void availableRooms(String datestr,HttpExchange exchange) throws IOException{
		
		LocalDate searchdate=ConvertStringtoLocaldate.convert(datestr);
		
		boolean[] bookFlag = temporyBooking.getOrDefault(new LocalDateCustom(searchdate),null);
		List<Integer> rsp=new ArrayList<>();
		
		if(bookFlag==null) {
			
			for(int i=1;i<=NO_ROOMS;i++)
				rsp.add(i);
			
		}else {

			for(int i=1;i<=NO_ROOMS;i++) {
				if(!bookFlag[i])
					rsp.add(i);		
			}

		}

			byte[] response=writeResponse(new AvailableRooms(rsp));
			exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
			exchange.sendResponseHeaders(200, 0);
		    OutputStream responseBody = exchange.getResponseBody();
		    responseBody.write(response);
		    responseBody.close();	


	}
	
	private void searchBookings(String name,HttpExchange exchange) throws IOException {
		
		List<BookingInfo> bookings=new ArrayList<>();
		
		if(confimedBooking.size()!=0) {
			
			
			Set<Entry<LocalDateCustom, String[]>> entrySet = confimedBooking.entrySet();
		
			for (Entry<LocalDateCustom, String[]> entry : entrySet) {
				
				String[] value = entry.getValue();
				
				for (int i=1;i<value.length;i++) {
					
					if(value[i]!=null && value[i].equals(name)) {
						bookings.add(new BookingInfo(name,i,entry.getKey().getBookingDate()));
					}					
				}

			}
		}
			
		byte[] response=writeResponse(new Bookings(bookings));
		exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
		exchange.sendResponseHeaders(200, 0);
	    OutputStream responseBody = exchange.getResponseBody();
	    responseBody.write(response);
	    responseBody.close();				

	}

	
	private void handlePreBookReq(HttpExchange httpExchange) throws IOException{

			BookingInfo bookInfo = readRequest(httpExchange.getRequestBody(), BookingInfo.class);
			
			boolean[] bookFlag = temporyBooking.getOrDefault(new LocalDateCustom(bookInfo.getBookingDate()),null);
			int status=StatusCode.CREATED.getCode();
			
			if(bookInfo.getRoom()> NO_ROOMS  || bookInfo.getRoom()<=0 ) {
				
				status=StatusCode.BAD_REQUEST.getCode();
			}else {
			
				if(bookFlag==null) {
					boolean[] flags=new boolean[NO_ROOMS+1];
					flags[bookInfo.getRoom()]=true;
					temporyBooking.put(new LocalDateCustom(bookInfo.getBookingDate()), flags);
		
				}else if(!bookFlag[bookInfo.getRoom()]){
					bookFlag[bookInfo.getRoom()]=true;
				}else {
					status=StatusCode.BAD_REQUEST.getCode();
				}
			}
			httpExchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
			httpExchange.sendResponseHeaders(status, 0);

		    byte[] response = writeResponse(bookInfo);

		    OutputStream responseBody = httpExchange.getResponseBody();
		    responseBody.write(response);
		    responseBody.close();			

		
	}   
	
	private void handleBookCnfReq(HttpExchange httpExchange) throws IOException{
		
			BookingInfo bookInfo = readRequest(httpExchange.getRequestBody(), BookingInfo.class);
			
			boolean[] bookFlag = temporyBooking.getOrDefault(new LocalDateCustom(bookInfo.getBookingDate()),null);
			
			if(bookFlag!=null && bookFlag[bookInfo.getRoom()]) {
				String[] confirmed = confimedBooking.getOrDefault(new LocalDateCustom(bookInfo.getBookingDate()),null);
				
				if(confirmed==null){
					
					String[] newconfirmed=new String[NO_ROOMS+1];
					newconfirmed[bookInfo.getRoom()]=bookInfo.getName();
					confimedBooking.put(new LocalDateCustom(bookInfo.getBookingDate()), newconfirmed);
				}else {
					confirmed[bookInfo.getRoom()]=bookInfo.getName();
				}
				
				
			}else {
				//LOGs For Manual monitoring As this is offline process
			}
			
			httpExchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
			httpExchange.sendResponseHeaders(StatusCode.CREATED.getCode(), -1);
				
	}   
	
	

}
