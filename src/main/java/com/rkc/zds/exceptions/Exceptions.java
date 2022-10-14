package com.rkc.zds.exceptions;

public class Exceptions extends RuntimeException  {
	
    public Exceptions(String message) {
        super(message);
    }

    public Exceptions(String message, Throwable cause) {
        super(message, cause);
    }
    
	public static Exception notFound(String format) throws Exception {
		// TODO Auto-generated method stub
		return new Exception("Not found");
	}
	
}
