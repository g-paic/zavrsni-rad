package project.models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class Korisnik extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ID;

	public Korisnik(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled) {
		super(username, password, enabled, true, true, true, authorities);
		this.ID = 0;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
}