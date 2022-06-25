package project.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(PosaoStrojPK.class)
@Table(name = "PosaoStroj")
public class PosaoStrojENTITY {
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
	@JoinColumn(name = "strojID", nullable = false)
	private StrojENTITY stroj;

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

	public StrojENTITY getStroj() {
		return this.stroj;
	}

	public void setStroj(StrojENTITY stroj) {
		this.stroj = stroj;
	}
}