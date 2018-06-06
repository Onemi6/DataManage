package com.hzlf.sampletest.entityclass;

public class UploadImg {

	private String status;
	private String message;

	public UploadImg() {
	}

	public UploadImg(String status, String message) {
		this.status = status;
		this.message = message;
	}
	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	
}
