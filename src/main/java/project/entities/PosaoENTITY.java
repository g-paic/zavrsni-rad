package project.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Posao")
public class PosaoENTITY {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, columnDefinition = "serial")
	private int ID;
	
	@OneToOne
	@JoinColumn(name = "korisnikID", nullable = false)
	private KorisnikENTITY korisnik;
	
	@Column(name = "naziv", nullable = false)
	private String naziv;
	
	@Column(name = "datumPocetka", nullable = false)
	private LocalDate datumPocetka;
	
	@Column(name = "datumKraja", nullable = false)
	private LocalDate datumKraja;
	
	@Column(name = "adresa", nullable = false)
	private String adresa;
	
	@Column(name = "objekt", nullable = false)
	private String objekt;
	
	@Column(name = "zarada", nullable = false)
	private double zarada;
	
	@Column(name = "troškovi", nullable = false)
	private double troškovi;

	public int getID() {
		return this.ID;
	}

	public void setID(int iD) {
		this.ID = iD;
	}

	public KorisnikENTITY getKorisnik() {
		return this.korisnik;
	}

	public void setKorisnik(KorisnikENTITY korisnik) {
		this.korisnik = korisnik;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public LocalDate getDatumPocetka() {
		return this.datumPocetka;
	}

	public void setDatumPocetka(LocalDate datumPočetka) {
		this.datumPocetka = datumPočetka;
	}

	public LocalDate getDatumKraja() {
		return this.datumKraja;
	}

	public void setDatumKraja(LocalDate datumKraja) {
		this.datumKraja = datumKraja;
	}

	public String getAdresa() {
		return this.adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getObjekt() {
		return this.objekt;
	}

	public void setObjekt(String objekt) {
		this.objekt = objekt;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((adresa == null) ? 0 : adresa.hashCode());
		result = prime * result + ((datumKraja == null) ? 0 : datumKraja.hashCode());
		result = prime * result + ((datumPocetka == null) ? 0 : datumPocetka.hashCode());
		result = prime * result + ((korisnik == null) ? 0 : korisnik.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		result = prime * result + ((objekt == null) ? 0 : objekt.hashCode());
		long temp;
		temp = Double.doubleToLongBits(troškovi);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(zarada);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PosaoENTITY other = (PosaoENTITY) obj;
		if (ID != other.ID)
			return false;
		if (adresa == null) {
			if (other.adresa != null)
				return false;
		} else if (!adresa.equals(other.adresa))
			return false;
		if (datumKraja == null) {
			if (other.datumKraja != null)
				return false;
		} else if (!datumKraja.equals(other.datumKraja))
			return false;
		if (datumPocetka == null) {
			if (other.datumPocetka != null)
				return false;
		} else if (!datumPocetka.equals(other.datumPocetka))
			return false;
		if (korisnik == null) {
			if (other.korisnik != null)
				return false;
		} else if (!korisnik.equals(other.korisnik))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		if (objekt == null) {
			if (other.objekt != null)
				return false;
		} else if (!objekt.equals(other.objekt))
			return false;
		if (Double.doubleToLongBits(troškovi) != Double.doubleToLongBits(other.troškovi))
			return false;
		if (Double.doubleToLongBits(zarada) != Double.doubleToLongBits(other.zarada))
			return false;
		return true;
	}
}