package com.prabathj.hotelavailabilitysrv.model;

import java.io.Serializable;

public class ConfigData implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3839637892106382867L;
	private int no_rooms;

	public int getNo_rooms() {
		return no_rooms;
	}

	public void setNo_rooms(int no_rooms) {
		this.no_rooms = no_rooms;
	}

	public ConfigData(int no_rooms) {
		super();
		this.no_rooms = no_rooms;
	}

	public ConfigData() {
		super();
	}
	
	
	
}
