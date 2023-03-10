package com.prabathj.bookinggw.utilities;

import java.time.LocalDate;


public class ConvertStringtoLocaldate {

	
	public static LocalDate convert(String value) {
		
		LocalDate dt = LocalDate.parse(value);
		return dt;
	}
	
}
