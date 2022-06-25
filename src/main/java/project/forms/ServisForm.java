package project.forms;

public class ServisForm {
	private String dateStart;
	private String dateEnd;
	private double cost;
	
	public ServisForm() {
		
	}
	
	public ServisForm(String dateStart, String dateEnd, double cost) {
		super();
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.cost = cost;
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
	
	public double getCost() {
		return this.cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
}