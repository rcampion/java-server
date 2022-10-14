package com.rkc.zds.exceptions;

public class ExceptionTest {

	public static void main(String[] args) {
		ExceptionTest app = new ExceptionTest();
		
		app.testException();

	}

	private void testException() {
		try {
			
			System.out.println("In try block");
			
			return;
			
			//throw new SinglyLinkedListException("Test Exception");
			
			
		} catch(Exception e) {
			
			System.out.println("In catch block");
			
			e.printStackTrace();
			
		} finally {
			
			System.out.println("In finally block");
		}
		
	}

}
