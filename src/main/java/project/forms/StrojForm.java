package project.forms;

import java.time.LocalDate;

public class StrojForm {
	private String name;
	private double price;
	private String date;
	
	public StrojForm() {
		
	}

	public StrojForm(String name, double price, String date) {
		super();
		this.name = name;
		this.price = price;
		this.date = date;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return this.name + " " + this.price + " " + this.date;
	}
}