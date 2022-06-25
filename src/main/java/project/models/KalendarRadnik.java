package project.models;

public class KalendarRadnik {
	private String title;
	private String start;
	private String end;
	private double isplata;

	public KalendarRadnik(String title, String start, String end, double isplata) {
		super();
		this.title = title;
		this.start = start;
		this.end = end;
		this.isplata = isplata;
	}

	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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

	public double getIsplata() {
		return this.isplata;
	}

	public void setIsplata(double isplata) {
		this.isplata = isplata;
	}
}
