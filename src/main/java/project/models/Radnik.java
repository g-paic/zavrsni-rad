package project.models;

public class Radnik {
	private int ID;
	private String ime;
	private String prezime;
	private String korisnickoIme;
	private double isplata;
	
	public Radnik(int ID, String ime, String prezime, String korisnickoIme, double isplata) {
		super();
		this.ID = ID;
		this.ime = ime;
		this.prezime = prezime;
		this.korisnickoIme = korisnickoIme;
		this.isplata = isplata;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return this.prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getKorisnickoIme() {
		return this.korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public double getIsplata() {
		return isplata;
	}

	public void setIsplata(double isplata) {
		this.isplata = isplata;
	}
}