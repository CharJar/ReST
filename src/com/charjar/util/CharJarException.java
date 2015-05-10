package com.charjar.util;

public class CharJarException extends Throwable {
	private static final long serialVersionUID = 1L;
	private String message;
	private int errorID;
	
	public CharJarException(int errorID) {
		this.errorID = errorID;
		this.message = getErrorMessage(errorID);
	}
	
	public CharJarException(Throwable error) {
		this.errorID = 0;
		this.message = error.getMessage();
	}

	public String getMessage() {
		return message;
	}
	
	public int getErrorID() {
		return this.errorID;
	}
	
	protected String getErrorMessage(int errorID) {		
		switch(errorID) {
		case 0:
			return "General SQL Error";
		case 1: 
			return "Project already exists.";
		case 2: 
			return "GetNextProject Problem.";
		default:
			return "Unknown error";
		}
	}
}
