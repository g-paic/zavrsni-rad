package project.models;

public class Kalendar {
	private String title;
	private String start;
	private String end;

	public Kalendar(String title, String start, String end) {
		super();
		this.title = title;
		this.start = start;
		this.end = end;
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
	
	@Override
	public String toString() {
		return "{title: \"" + this.title + "\", start: \"" + this.start + "\", end: \"" + this.end + "\"}";
	}
}