package com.example.megaCabService.models;

public class BookingModel {
	private int bookingID;
    private int clientID;
    private int cabID;
    private String driverStatus;
    private String bookedDates;
    private String bookingDate;
    private double billAmount;
    public int getBookingID() {
		return bookingID;
	}
	public void setBookingID(int bookingID) {
		this.bookingID = bookingID;
	}
	public int getClientID() {
		return clientID;
	}
	public void setClientID(int clientID) {
		this.clientID = clientID;
	}
	public int getCabID() {
		return cabID;
	}
	public void setCabID(int cabID) {
		this.cabID = cabID;
	}
	public String getDriverStatus() {
		return driverStatus;
	}
	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}
	public String getBookedDates() {
		return bookedDates;
	}
	public void setBookedDates(String bookedDates) {
		this.bookedDates = bookedDates;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
    public double getBillAmount() {
        return billAmount;
    }
    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }
}
