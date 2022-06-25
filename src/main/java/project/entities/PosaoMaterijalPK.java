package project.entities;

import java.io.Serializable;

public class PosaoMaterijalPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private KorisnikENTITY korisnik;
	private PosaoENTITY posao;
	private MaterijalENTITY materijal;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((korisnik == null) ? 0 : korisnik.hashCode());
		result = prime * result + ((materijal == null) ? 0 : materijal.hashCode());
		result = prime * result + ((posao == null) ? 0 : posao.hashCode());
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
		PosaoMaterijalPK other = (PosaoMaterijalPK) obj;
		if (korisnik == null) {
			if (other.korisnik != null)
				return false;
		} else if (!korisnik.equals(other.korisnik))
			return false;
		if (materijal == null) {
			if (other.materijal != null)
				return false;
		} else if (!materijal.equals(other.materijal))
			return false;
		if (posao == null) {
			if (other.posao != null)
				return false;
		} else if (!posao.equals(other.posao))
			return false;
		return true;
	}
}