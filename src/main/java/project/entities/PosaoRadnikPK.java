package project.entities;

import java.io.Serializable;

public class PosaoRadnikPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private KorisnikENTITY korisnik;
	private PosaoENTITY posao;
	private RadnikENTITY radnik;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((korisnik == null) ? 0 : korisnik.hashCode());
		result = prime * result + ((posao == null) ? 0 : posao.hashCode());
		result = prime * result + ((radnik == null) ? 0 : radnik.hashCode());
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
		PosaoRadnikPK other = (PosaoRadnikPK) obj;
		if (korisnik == null) {
			if (other.korisnik != null)
				return false;
		} else if (!korisnik.equals(other.korisnik))
			return false;
		if (posao == null) {
			if (other.posao != null)
				return false;
		} else if (!posao.equals(other.posao))
			return false;
		if (radnik == null) {
			if (other.radnik != null)
				return false;
		} else if (!radnik.equals(other.radnik))
			return false;
		return true;
	}
}