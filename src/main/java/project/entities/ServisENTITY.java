package project.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(ServisPK.class)
@Table(name = "Servis")
public class ServisENTITY {
	@Id
	@ManyToOne
	@JoinColumn(name = "korisnikID", nullable = false)
	private KorisnikENTITY korisnik;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "strojID", nullable = false)
	private StrojENTITY stroj;
	
	@Id
	@Column(name = "datumPočetka", nullable = false)
	private LocalDate datumPočetka;
	
	@Id
	@Column(name = "datumKraja", nullable = false)
	private LocalDate datumKraja;
	
	@Column(name = "cijena", nullable = false)
	private double cijena;

	public KorisnikENTITY getKorisnik() {
		return this.korisnik;
	}

	public void setKorisnik(KorisnikENTITY korisnik) {
		this.korisnik = korisnik;
	}

	public StrojENTITY getStroj() {
		return this.stroj;
	}

	public void setStroj(StrojENTITY stroj) {
		this.stroj = stroj;
	}

	public LocalDate getDatumPočetka() {
		return this.datumPočetka;
	}

	public void setDatumPočetka(LocalDate datumPočetka) {
		this.datumPočetka = datumPočetka;
	}

	public LocalDate getDatumKraja() {
		return this.datumKraja;
	}

	public void setDatumKraja(LocalDate datumKraja) {
		this.datumKraja = datumKraja;
	}

	public double getCijena() {
		return this.cijena;
	}

	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
}