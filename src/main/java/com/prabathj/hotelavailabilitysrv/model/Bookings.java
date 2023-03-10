package com.prabathj.hotelavailabilitysrv.model;

import java.io.Serializable;
import java.util.List;

public class Bookings implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1234651517668224687L;
	private List<BookingInfo> bookings;

	public Bookings() {
		super();
	}

	public Bookings(List<BookingInfo> bookings) {
		super();
		this.bookings = bookings;
	}

	public List<BookingInfo> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingInfo> bookings) {
		this.bookings = bookings;
	}
	
	
	
	
}
