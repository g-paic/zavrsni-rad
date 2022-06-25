package project.entities;

import java.io.Serializable;
import java.time.LocalDate;

public class ServisPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private KorisnikENTITY korisnik;
	private StrojENTITY stroj;
	private LocalDate datumPočetka;
	private LocalDate datumKraja;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datumKraja == null) ? 0 : datumKraja.hashCode());
		result = prime * result + ((datumPočetka == null) ? 0 : datumPočetka.hashCode());
		result = prime * result + ((korisnik == null) ? 0 : korisnik.hashCode());
		result = prime * result + ((stroj == null) ? 0 : stroj.hashCode());
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
		ServisPK other = (ServisPK) obj;
		if (datumKraja == null) {
			if (other.datumKraja != null)
				return false;
		} else if (!datumKraja.equals(other.datumKraja))
			return false;
		if (datumPočetka == null) {
			if (other.datumPočetka != null)
				return false;
		} else if (!datumPočetka.equals(other.datumPočetka))
			return false;
		if (korisnik == null) {
			if (other.korisnik != null)
				return false;
		} else if (!korisnik.equals(other.korisnik))
			return false;
		if (stroj == null) {
			if (other.stroj != null)
				return false;
		} else if (!stroj.equals(other.stroj))
			return false;
		return true;
	}
}