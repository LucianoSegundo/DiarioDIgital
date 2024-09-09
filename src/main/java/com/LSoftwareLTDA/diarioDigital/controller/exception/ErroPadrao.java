package com.LSoftwareLTDA.diarioDigital.controller.exception;

import java.io.Serializable;
import java.time.Instant;

public class ErroPadrao implements Serializable {
	
	private Instant timestamp;
	private Integer status;
	private String message;
	private String path;
	public ErroPadrao() {}
	
	
	public ErroPadrao(Instant timestamp, Integer status, String message, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.path = path;
	}


	public Instant getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	

}
