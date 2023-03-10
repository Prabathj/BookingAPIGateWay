package com.prabathj.hotelavailabilitysrv.model;

import java.io.Serializable;
import java.util.List;

public class AvailableRooms implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 239671581914668069L;
	private List<Integer> rooms;

	public List<Integer> getRooms() {
		return rooms;
	}

	public void setRooms(List<Integer> rooms) {
		this.rooms = rooms;
	}

	public AvailableRooms(List<Integer> rooms) {
		super();
		this.rooms = rooms;
	}

	public AvailableRooms() {

	}
	
	

}
