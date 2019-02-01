package com.rjs.myshows.server.controller;

import org.springframework.http.HttpStatus;

public class RestException extends Exception {
	private final HttpStatus httpStatus;

	public RestException() {
		this(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public RestException(String message) {
		this(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public RestException(String message, Throwable cause) {
		this(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public RestException(Throwable cause) {
		this(cause, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		this(message, cause, enableSuppression, writableStackTrace, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public RestException(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public RestException(String message, HttpStatus httpStatus) {
		super(message);

		this.httpStatus = httpStatus;
	}

	public RestException(String message, Throwable cause, HttpStatus httpStatus) {
		super(message, cause);

		this.httpStatus = httpStatus;
	}

	public RestException(Throwable cause, HttpStatus httpStatus) {
		super(cause);

		this.httpStatus = httpStatus;
	}

	public RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus httpStatus) {
		super(message, cause, enableSuppression, writableStackTrace);

		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
