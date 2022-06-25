package project.forms;

public class MaterijalForm {
	private String name;
	private double price;
	
	public MaterijalForm() {
		
	}
	
	public MaterijalForm(String name, double price) {
		super();
		this.name = name;
		this.price = price;
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
}