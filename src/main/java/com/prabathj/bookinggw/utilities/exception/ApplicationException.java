package com.prabathj.bookinggw.utilities.exception;

public class ApplicationException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5750359576724902416L;
	private final int code;

    public ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }

	public int getCode() {
		return code;
	}
    
}
