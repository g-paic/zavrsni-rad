package project.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(PosaoServisPK.class)
@Table(name = "PosaoServis")
public class PosaoServisENTITY {
	@Id
	@ManyToOne
	@JoinColumn(name = "korisnikID", nullable = false)
	private KorisnikENTITY korisnik;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "posaoID", nullable = false)
	private PosaoENTITY posao;
	
	@Column(name = "trošak", nullable = false)
	private double trošak;

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

	public double getTrošak() {
		return this.trošak;
	}

	public void setTrošak(double trošak) {
		this.trošak = trošak;
	}
}