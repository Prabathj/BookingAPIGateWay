package com.prabathj.hotelavailabilitysrv.model;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BookingInfo implements Serializable{
	

	private static final long serialVersionUID = -189251435350265176L;
	private String  name;
	private Integer room;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate bookingDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRoom() {
		return room.intValue();
	}
	public void setRoom(Integer room) {
		this.room = room;
	}
	public LocalDate getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}
	public BookingInfo(String name, Integer room, LocalDate bookingDate) {
		super();
		this.name = name;
		this.room = room;
		this.bookingDate = bookingDate;
	}
	public BookingInfo() {
		super();
	}

	
}
