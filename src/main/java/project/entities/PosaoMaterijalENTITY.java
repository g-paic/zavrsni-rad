package project.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(PosaoMaterijalPK.class)
@Table(name = "PosaoMaterijal")
public class PosaoMaterijalENTITY {
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
	@JoinColumn(name = "materijalID", nullable = false)
	private MaterijalENTITY materijal;
	
	@Column(name = "cijena", nullable = false)
	private double cijena;

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

	public MaterijalENTITY getMaterijal() {
		return this.materijal;
	}

	public void setMaterijal(MaterijalENTITY materijal) {
		this.materijal = materijal;
	}

	public double getCijena() {
		return this.cijena;
	}

	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
}