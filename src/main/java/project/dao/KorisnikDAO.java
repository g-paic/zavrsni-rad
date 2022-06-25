package project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import project.entities.KorisnikENTITY;
import project.forms.KorisnikForm;

@Repository
@Transactional
public class KorisnikDAO {
	@Autowired
    private EntityManager entityManager;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
    private static String tableName = "Korisnik";
    
    public KorisnikENTITY findUserByID(int ID) {
        try {
            String sql = "SELECT e FROM " + KorisnikENTITY.class.getName() + " e " //
                    + " WHERE e.ID = :ID ";

            Query query = entityManager.createQuery(sql, KorisnikENTITY.class);
            query.setParameter("ID", ID);
            
            return (KorisnikENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    public KorisnikENTITY findUserByName(String korisnickoIme) {
        try {
            String sql = "SELECT e FROM " + KorisnikENTITY.class.getName() + " e " //
                    + " WHERE e.korisnickoIme = :korisnickoIme ";

            Query query = entityManager.createQuery(sql, KorisnikENTITY.class);
            query.setParameter("korisnickoIme", korisnickoIme);
            
            return (KorisnikENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    public KorisnikENTITY findUserByCompany(String tvrtka) {
        try {
            String sql = "SELECT e FROM " + KorisnikENTITY.class.getName() + " e " //
                    + " WHERE e.tvrtka = :tvrtka ";

            Query query = entityManager.createQuery(sql, KorisnikENTITY.class);
            query.setParameter("tvrtka", tvrtka);
            
            return (KorisnikENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    public KorisnikENTITY findUserByEmail(String email) {
        try {
            String sql = "SELECT e FROM " + KorisnikENTITY.class.getName() + " e " //
                    + " WHERE e.email = :email ";

            Query query = entityManager.createQuery(sql, KorisnikENTITY.class);
            query.setParameter("email", email);
            
            return (KorisnikENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    public boolean createKorisnik(KorisnikForm form) {
    	String encryptedPassword = this.passwordEncoder.encode(form.getPassword());
    	
    	try {
            String sql = "insert into " + tableName + " (ime, prezime, email, tvrtka, korisnicko_ime, lozinka) values (?, ?, ?, ?, "
            		+ "?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, form.getFirstName())
            			.setParameter(2, form.getLastName())
            			.setParameter(3, form.getEmail())
            			.setParameter(4, form.getCompany())
            			.setParameter(5, form.getUserName())
            			.setParameter(6, encryptedPassword)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
    	
    	return false;
    }
    
    public List<KorisnikENTITY> getUsers() {
		String sql = "SELECT e FROM " + KorisnikENTITY.class.getName() + " e ";
    	Query query = entityManager.createQuery(sql, KorisnikENTITY.class);
        
        return (List<KorisnikENTITY>) query.getResultList();
    }
    
    public boolean updateData(int korisnikID, String ime, String prezime, String korisnickoIme, String email, String tvrtka) {
		try {
            String sql = "UPDATE " + KorisnikENTITY.class.getName() +
            		" e SET e.ime = :ime, e.prezime = :prezime, e.korisnickoIme = :korisnickoIme, "
            		+ "e.email = :email, e.tvrtka = :tvrtka " +
            		" WHERE e.id = :korisnikID";
            
            entityManager.createQuery(sql)
            			.setParameter("korisnikID", korisnikID)
            			.setParameter("ime", ime)
            			.setParameter("prezime", prezime)
            			.setParameter("korisnickoIme", korisnickoIme)
            			.setParameter("email", email)
            			.setParameter("tvrtka", tvrtka)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
}