package com.example.megaCabService.models;

public class TaxAndDiscountModel {
	private int id;
    private double taxPercentage;
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	public double getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	private double discountPercentage;
}
