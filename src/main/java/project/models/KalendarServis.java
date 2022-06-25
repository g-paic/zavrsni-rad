package project.models;

public class KalendarServis {
	private String start;
	private String end;
	private double cost;
	
	public KalendarServis(String start, String end, double cost) {
		super();
		this.start = start;
		this.end = end;
		this.cost = cost;
	}
	
	public String getStart() {
		return this.start;
	}
	
	public void setStart(String start) {
		this.start = start;
	}
	
	public String getEnd() {
		return this.end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
}
