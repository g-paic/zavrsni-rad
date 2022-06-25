package project.models;

public class Pie {
	private String naziv;
	private double trošak;
	
	public Pie(String naziv, double trošak) {
		super();
		this.naziv = naziv;
		this.trošak = trošak;
	}
	
	public String getNaziv() {
		return this.naziv;
	}
	
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public double getTrošak() {
		return this.trošak;
	}
	
	public void setTrošak(double trošak) {
		this.trošak = trošak;
	}
}