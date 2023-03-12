package com.prabathj.bookinggw;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.prabathj.hotelavailabilitysrv.model.BookingInfo;
import com.prabathj.hotelavailabilitysrv.reqhandler.AvailabilityReqHandler;

public class TestDataSetConfiguration {
	
	private final List<BookingInfo> testBooks;
	private final String name;
	
	
	public TestDataSetConfiguration() {
		testBooks=new ArrayList<>();
		
		//Room No <= AvailabilityReqHandler.NO_ROOMS for bookings
		
		testBooks.add(new BookingInfo("mike", 2, LocalDate.now()));
		testBooks.add(new BookingInfo("bruno", 3, LocalDate.now()));
		testBooks.add(new BookingInfo("mike", 7, LocalDate.now()));
		testBooks.add(new BookingInfo("messi", 8, LocalDate.now()));
		testBooks.add(new BookingInfo("ronaldo",10, LocalDate.now()));
		
		
		testBooks.add(new BookingInfo("mike", 4, LocalDate.of(2023,04,02)));
		testBooks.add(new BookingInfo("messi", 6, LocalDate.of(2023,04,02)));
		testBooks.add(new BookingInfo("ronaldo", 1, LocalDate.of(2023,04,02)));
		
		testBooks.add(new BookingInfo("mike", 1, LocalDate.of(2023,04,07)));
		testBooks.add(new BookingInfo("bruno", 4, LocalDate.of(2023,04,07)));
		testBooks.add(new BookingInfo("ann", 6, LocalDate.of(2023,04,07)));
		
		name="mike";
	}


	public List<Integer> getAvailableRoomsListOfDateNow() {
		
		
		Set<Integer> booked=new HashSet<>();
		booked.add(2);
		booked.add(3);
		booked.add(7);
		booked.add(8);
		booked.add(10);
		
		
		List<Integer> list=new ArrayList<>();
		for(int r=1;r<=AvailabilityReqHandler.NO_ROOMS;r++) {
			
			if(!booked.contains(r))
				list.add(r);
			
		}
		return list;
	}
	
	
	public List<BookingInfo> getBookingsPerUser(List<BookingInfo> testBooks, String name) {
				
		List<BookingInfo> list=new ArrayList<>();

		for (BookingInfo booking : testBooks) {
			if(booking.getName().equals(name))
				list.add(booking);
		}
		
		return list;
	}
		
	
	
	
	public List<BookingInfo> loadData() {

		return testBooks;

	}


	public String getName() {
		return name;
	}
	
	
}
