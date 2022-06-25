package project.forms;

public class PosaoForm {
	private String name;
	private String dateStart;
	private String dateEnd;
	private String address;
	private String object;
	private double income;
	
	public PosaoForm() {
		
	}
	
	public PosaoForm(String name, String dateStart, String dateEnd, String address, String object, double income) {
		super();
		this.name = name;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.address = address;
		this.object = object;
		this.income = income;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateStart() {
		return this.dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return this.dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getObject() {
		return this.object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public double getIncome() {
		return this.income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	@Override
	public String toString() {
		return this.name + " " + this.dateStart + " " + this.dateEnd + " " + this.address + " " + this.object + " " + this.income;
	}
}