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
import project.forms.PosaoForm;
import project.models.Korisnik;

@Repository
@Transactional
public class PosaoDAO {
	@Autowired
    private EntityManager entityManager;
	
    private static String tableName = "Posao";
    
    public PosaoENTITY job(int ID) {
		try {
            String sql = "SELECT e FROM " + PosaoENTITY.class.getName() + " e " //
                    + " WHERE e.id = :id ";

            Query query = entityManager.createQuery(sql, PosaoENTITY.class);
            query.setParameter("id", ID);
            
            return (PosaoENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
	public List<PosaoENTITY> getJobs(int ID) {
		String sql = "SELECT e FROM " + PosaoENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :ID";
        
    	Query query = entityManager.createQuery(sql, PosaoENTITY.class);
        query.setParameter("ID", ID);
        
        return (List<PosaoENTITY>) query.getResultList();
    }
    
    public boolean insertJob(PosaoForm form, Korisnik user) {
		try {
            String sql = "insert into " + tableName + " (naziv, datum_pocetka, datum_kraja, adresa, objekt, zarada, troškovi, korisnikid) "
            		+ "values (?, ?, ?, ?, ?, ?, ?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, form.getName())
            			.setParameter(2, LocalDate.parse(form.getDateStart()))
            			.setParameter(3, LocalDate.parse(form.getDateEnd()))
            			.setParameter(4, form.getAddress())
            			.setParameter(5, form.getObject())
            			.setParameter(6, form.getIncome())
            			.setParameter(7, 0)
            			.setParameter(8, user.getID())
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
    
    public boolean updateJob(int korisnikID, int posaoID, String naziv, String adresa, String objekt) {
		try {
            String sql = "UPDATE " + PosaoENTITY.class.getName() +
            		" e SET e.naziv = :naziv, e.adresa = :adresa, e.objekt = :objekt " +
            		" WHERE e.id = :posaoID AND e.korisnik.id = :korisnikID";
            
            entityManager.createQuery(sql)
            			.setParameter("korisnikID", korisnikID)
            			.setParameter("posaoID", posaoID)
            			.setParameter("naziv", naziv)
            			.setParameter("adresa", adresa)
            			.setParameter("objekt", objekt)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
    
    public boolean setNewIncomeForJob(int ID, double zarada) {
		try {
            String sql = "UPDATE " + PosaoENTITY.class.getName() +
            		" e SET e.zarada = :zarada " +
            		" WHERE e.id = :ID";
            
            entityManager.createQuery(sql)
            			.setParameter("zarada", zarada)
            			.setParameter("ID", ID)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean setNewCostForJob(int ID, double troškovi) {
		try {
            String sql = "UPDATE " + PosaoENTITY.class.getName() +
            		" e SET e.troškovi = :troškovi " +
            		" WHERE e.id = :ID";
            
            entityManager.createQuery(sql)
            			.setParameter("troškovi", troškovi)
            			.setParameter("ID", ID)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public List<PosaoENTITY> allJobs() {
		String sql = "SELECT e FROM " + PosaoENTITY.class.getName() + " e ";
    	Query query = entityManager.createQuery(sql, PosaoENTITY.class);
        
        return (List<PosaoENTITY>) query.getResultList();
    }
}