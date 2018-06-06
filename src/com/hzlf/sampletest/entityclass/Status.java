package com.hzlf.sampletest.entityclass;

public class Status {
	private String NO;
	private String NAME;
	private String status;
	private String message;

	public Status() {
	}

	public Status(String NO, String NAME, String status, String message) {
		this.NO = NO;
		this.NAME = NAME;
		this.status = status;
		this.message = message;
	}

	public String getNo() {
		return NO;
	}

	public String getName() {
		return NAME;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
