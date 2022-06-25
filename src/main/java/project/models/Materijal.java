package project.models;

public class Materijal {
	private int ID;
	private String naziv;
	private double cijena;
	
	public Materijal(int ID, String naziv, double cijena) {
		super();
		this.ID = ID;
		this.naziv = naziv;
		this.cijena = cijena;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	public String getNaziv() {
		return this.naziv;
	}
	
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public double getCijena() {
		return this.cijena;
	}
	
	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
}