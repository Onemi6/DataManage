package com.hzlf.sampletest.entityclass;

public class Upload {

	private String id;
	private String status;
	private String message;

	public Upload() {
	}

	public Upload(String id, String status, String message) {
		this.id = id;
		this.status = status;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
