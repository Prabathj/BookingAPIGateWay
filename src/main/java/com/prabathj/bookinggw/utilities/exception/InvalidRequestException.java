package com.prabathj.bookinggw.utilities.exception;

public class InvalidRequestException extends ApplicationException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6169249377840874155L;

	public InvalidRequestException(int code, String message) {
        super(code, message);
    }
}
