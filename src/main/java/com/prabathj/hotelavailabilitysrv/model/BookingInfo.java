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
	
	@Override
	public String toString() {
		return "BookingInfo [name=" + name + ", room=" + room + ", bookingDate=" + bookingDate + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookingDate == null) ? 0 : bookingDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookingInfo other = (BookingInfo) obj;
		if (bookingDate == null) {
			if (other.bookingDate != null)
				return false;
		} else if (!bookingDate.equals(other.bookingDate))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		return true;
	}

	
	
}
