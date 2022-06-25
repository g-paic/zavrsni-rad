package project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.dao.KorisnikDAO;
import project.entities.KorisnikENTITY;
import project.models.Korisnik;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private KorisnikDAO korisnikDAO;
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    	KorisnikENTITY korisnik = korisnikDAO.findUserByName(userName);
        
        if(korisnik == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }
        
        System.out.println("Found User: " + korisnik);
      
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>(); 
        
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_VLASNIK");
    	grantList.add(authority);
    	
    	Korisnik kor = new Korisnik(korisnik.getKorisnickoIme(), korisnik.getLozinka(), grantList, true);
    	kor.setID(korisnik.getID());
        UserDetails userDetails = (UserDetails) kor;
        
        return userDetails;
    }
}