package project.models;

public class ServisTrosak {
	private int id;
	private double zarada;
	private double troškovi;
	
	public ServisTrosak(int id, double zarada, double troškovi) {
		super();
		this.id = id;
		this.zarada = zarada;
		this.troškovi = troškovi;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getZarada() {
		return this.zarada;
	}

	public void setZarada(double zarada) {
		this.zarada = zarada;
	}

	public double getTroškovi() {
		return this.troškovi;
	}

	public void setTroškovi(double troškovi) {
		this.troškovi = troškovi;
	}	
}