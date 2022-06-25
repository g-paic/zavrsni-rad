package project.dao;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.entities.PosaoENTITY;
import project.entities.PosaoRadnikENTITY;
import project.entities.RadnikENTITY;
import project.forms.RadnikForm;
import project.models.Korisnik;

@Repository
@Transactional
public class RadnikDAO {
	@Autowired
    private EntityManager entityManager;
	
	private static String tableName = "Radnik";
	
	public RadnikENTITY worker(int ID) {
		try {
            String sql = "SELECT e FROM " + RadnikENTITY.class.getName() + " e " //
                    + " WHERE e.id = :id ";

            Query query = entityManager.createQuery(sql, RadnikENTITY.class);
            query.setParameter("id", ID);
            
            return (RadnikENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
	
	@SuppressWarnings("unchecked")
	public List<RadnikENTITY> getWorkers(int ID) {
		String sql = "SELECT e FROM " + RadnikENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :ID";
        
    	Query query = entityManager.createQuery(sql, RadnikENTITY.class);
        query.setParameter("ID", ID);
        
        return (List<RadnikENTITY>) query.getResultList();
    }
	
	public boolean insertWorker(RadnikForm form, Korisnik user) {
		try {
            String sql = "insert into " + tableName + " (ime, prezime, šefid, korisnicko_ime) values (?, ?, ?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, form.getFirstName())
            			.setParameter(2, form.getLastName())
            			.setParameter(3, user.getID())
            			.setParameter(4, form.getUserName())
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean updateWorker(int korisnikID, int radnikID, String ime, String prezime, String korisnickoIme) {
		try {
            String sql = "UPDATE " + RadnikENTITY.class.getName() +
            		" e SET e.ime = :ime, e.prezime = :prezime, e.korisnickoIme = :korisnickoIme " +
            		" WHERE e.id = :radnikID AND e.korisnik.id = :korisnikID";
            
            entityManager.createQuery(sql)
            			.setParameter("korisnikID", korisnikID)
            			.setParameter("radnikID", radnikID)
            			.setParameter("ime", ime)
            			.setParameter("prezime", prezime)
            			.setParameter("korisnickoIme", korisnickoIme)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean deleteWorker(int radnikID, int korisnikID) {
		try {
            String sql = "DELETE FROM " + tableName + " e WHERE e.id = :radnikID AND e.šefid = :korisnikID";
            
            entityManager.createNativeQuery(sql)
            			.setParameter("radnikID", radnikID)
            			.setParameter("korisnikID", korisnikID)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public List<PosaoENTITY> jobList(int korisnikID, int radnikID, List<PosaoENTITY> jobs) {
		String sql = "SELECT e FROM " + PosaoRadnikENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :korisnikID AND e.radnik.id = :radnikID";
		
		Query query = entityManager.createQuery(sql, PosaoRadnikENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("radnikID", radnikID);
        
		List<PosaoRadnikENTITY> list = (List<PosaoRadnikENTITY>) query.getResultList();
		List<PosaoENTITY> list2 = new LinkedList<>();
		
		for(PosaoRadnikENTITY posaoRadnik: list) {
			for(PosaoENTITY posao: jobs) {
				if(posaoRadnik.getPosao().getID() == posao.getID()) {
					list2.add(posao);
					break;
				}
			}
		}
		
		return list2;
	}
}
