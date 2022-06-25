package project.models;

public class Line {
	private String naziv;
	private double iznos;
	
	public Line(String naziv, double iznos) {
		super();
		this.naziv = naziv;
		this.iznos = iznos;
	}
	
	public String getNaziv() {
		return naziv;
	}
	
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public double getIznos() {
		return iznos;
	}
	
	public void setIznos(double iznos) {
		this.iznos = iznos;
	}
}