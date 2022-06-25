package project.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import project.entities.PosaoENTITY;
import project.entities.PosaoServisENTITY;
import project.entities.ServisENTITY;

@Repository
@Transactional
public class ServisDAO {
	@Autowired
    private EntityManager entityManager;
	
	private static String tableName = "Servis";
	
	public boolean checkServis(LocalDate d1, LocalDate d2, List<PosaoENTITY> popis, List<ServisENTITY> services) {
		for(PosaoENTITY posao: popis) {
			long a1 = posao.getDatumPocetka().toEpochDay();
			long a2 = posao.getDatumKraja().toEpochDay();
			
			long b1 = d1.toEpochDay();
			long b2 = d2.toEpochDay();
			
			if((a1 >= b1 && a1 <= b2) || (a2 >= b1 && a2 <= b2)) {
				return true;
			}
			
			if((b1 >= a1 && b1 <= a2) || (b2 >= a1 && b2 <= a2)) {
				return true;
			}
			
			if(b1 <= a1 && b2 >= a2) {
				return true;
			}
			
			if(a1 <= b1 && a2 >= b2) {
				return true;
			}
		}
		
		for(ServisENTITY servis: services) {
			long a1 = servis.getDatumPočetka().toEpochDay();
			long a2 = servis.getDatumKraja().toEpochDay();
			
			long b1 = d1.toEpochDay();
			long b2 = d2.toEpochDay();
			
			if((a1 >= b1 && a1 <= b2) || (a2 >= b1 && a2 <= b2)) {
				return true;
			}
			
			if((b1 >= a1 && b1 <= a2) || (b2 >= a1 && b2 <= a2)) {
				return true;
			}
			
			if(b1 <= a1 && b2 >= a2) {
				return true;
			}
			
			if(a1 <= b1 && a2 >= b2) {
				return true;
			}
		}
		
		return false;
	}
	
    public boolean setServis(int korisnikID, int strojID, LocalDate d1, LocalDate d2, double cijena) {
    	try {
            String sql = "insert into " + tableName + " (korisnikid, strojid, datum_početka, datum_kraja, cijena) "
            		+ "values (?, ?, ?, ?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, korisnikID)
            			.setParameter(2, strojID)
            			.setParameter(3, d1)
            			.setParameter(4, d2)
            			.setParameter(5, cijena)
            			.executeUpdate();
            
//            String sql = "insert into " + ServisENTITY.class.getName() + " (datum_početka, datum_kraja, cijena) "
//            		+ "values (?, ?, ?)";
//            
//            entityManager.createNativeQuery(sql)
//            			.setParameter(1, d1)
//            			.setParameter(2, d2)
//            			.setParameter(3, cijena)
//            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
    	
    	return false;
    }
    
    public List<ServisENTITY> getServices(int korisnikID, int strojID) {
    	String sql = "SELECT e FROM " + ServisENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :korisnikID AND e.stroj.id = :strojID";
        
    	Query query = entityManager.createQuery(sql, ServisENTITY.class);
    	query.setParameter("korisnikID", korisnikID);
        query.setParameter("strojID", strojID);
        
        return (List<ServisENTITY>) query.getResultList();
    }
    
    public boolean insertJobServis(int korisnikID, int posaoID) {
    	try {
            String sql = "INSERT INTO " + "posao_servis" + " (korisnikid, posaoid, trošak) "
            		+ "values (?, ?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, korisnikID)
            			.setParameter(2, posaoID)
            			.setParameter(3, 0)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
    	
    	return false;
    }
    
    public boolean setJobServisCost(int korisnikID, int posaoID, double trošak) {
		try {
            String sql = "UPDATE " + PosaoServisENTITY.class.getName() +
            		" e SET e.trošak = :trošak " +
            		" WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID";
            
            entityManager.createQuery(sql)
            			.setParameter("korisnikID", korisnikID)
            			.setParameter("posaoID", posaoID)
            			.setParameter("trošak", trošak)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
    
    public PosaoServisENTITY jobService(int korisnikID, int posaoID) {
		try {
            String sql = "SELECT e FROM " + PosaoServisENTITY.class.getName() + " e " //
                    + " WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID";

            Query query = entityManager.createQuery(sql, PosaoServisENTITY.class);
            query.setParameter("korisnikID", korisnikID);
            query.setParameter("posaoID", posaoID);
            
            return (PosaoServisENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
}