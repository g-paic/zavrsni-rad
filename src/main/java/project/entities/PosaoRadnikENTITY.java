package project.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(PosaoRadnikPK.class)
@Table(name = "PosaoRadnik")
public class PosaoRadnikENTITY {
	@Id
	@ManyToOne
	@JoinColumn(name = "korisnikID", nullable = false)
	private KorisnikENTITY korisnik;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "posaoID", nullable = false)
	private PosaoENTITY posao;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "radnikID", nullable = false)
	private RadnikENTITY radnik;
	
	@Column(name = "isplata", nullable = false)
	private double isplata;

	public KorisnikENTITY getKorisnik() {
		return this.korisnik;
	}

	public void setKorisnik(KorisnikENTITY korisnik) {
		this.korisnik = korisnik;
	}

	public PosaoENTITY getPosao() {
		return this.posao;
	}

	public void setPosao(PosaoENTITY posao) {
		this.posao = posao;
	}

	public RadnikENTITY getRadnik() {
		return this.radnik;
	}

	public void setRadnik(RadnikENTITY radnik) {
		this.radnik = radnik;
	}

	public double getIsplata() {
		return this.isplata;
	}

	public void setIsplata(double isplata) {
		this.isplata = isplata;
	}
}