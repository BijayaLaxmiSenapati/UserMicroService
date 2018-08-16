package com.bridgelabz.fundoo.user.exceptions;

public class UnAuthorisedAccessException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnAuthorisedAccessException(String message) {
		super(message);
	}
}

