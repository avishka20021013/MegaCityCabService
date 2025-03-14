package com.example.megaCabService.models;

public class CabModel {

	private int cabID;
	private String cabNumber;
	private String model;
	private String category;
	private int seats;
	private String driverName;
	private String driverContact;
	private String imageUrl;
	public int getCabID() {
		return cabID;
	}
	public void setCabID(int cabID) {
		this.cabID = cabID;
	}
	public String getCabNumber() {
		return cabNumber;
	}
	public void setCabNumber(String cabNumber) {
		this.cabNumber = cabNumber;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverContact() {
		return driverContact;
	}
	public void setDriverContact(String driverContact) {
		this.driverContact = driverContact;
	}
	public String getImageUrl() {
	    return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
	    this.imageUrl = imageUrl;
	}
	
}
