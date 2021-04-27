package com.meli.dto;

public class MessageDTO {
	private String id;
	private int position;
	private String nameSatellite;
	private String message;
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getNameSatellite() {
		return nameSatellite;
	}
	public void setNameSatellite(String nameSatellite) {
		this.nameSatellite = nameSatellite;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
