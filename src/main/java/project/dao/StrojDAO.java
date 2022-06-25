package project.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.entities.PosaoENTITY;
import project.entities.PosaoStrojENTITY;
import project.entities.StrojENTITY;
import project.models.Korisnik;

@Repository
@Transactional
public class StrojDAO {
	@Autowired
    private EntityManager entityManager;
	
	private static String tableName = "Stroj";
	
	public StrojENTITY machine(int ID) {
		try {
            String sql = "SELECT e FROM " + StrojENTITY.class.getName() + " e " //
                    + " WHERE e.id = :id ";

            Query query = entityManager.createQuery(sql, StrojENTITY.class);
            query.setParameter("id", ID);
            
            return (StrojENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
	
	@SuppressWarnings("unchecked")
	public List<StrojENTITY> getMachines(int ID) {
		String sql = "SELECT e FROM " + StrojENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :ID";
        
    	Query query = entityManager.createQuery(sql, StrojENTITY.class);
        query.setParameter("ID", ID);
        
        return (List<StrojENTITY>) query.getResultList();
    }
	
	public boolean insertMachine(String name, Korisnik user) {
		try {
            String sql = "insert into " + tableName + " (naziv, korisnikid) values (?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, name)
            			.setParameter(2, user.getID())
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean deleteMachine(int strojID, int korisnikID) {
		try {
            String sql = "DELETE FROM " + tableName + " e WHERE e.id = :strojID AND e.korisnikid = :korisnikID";
            
            entityManager.createNativeQuery(sql)
            			.setParameter("strojID", strojID)
            			.setParameter("korisnikID", korisnikID)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean updateMachine(int strojID, int korisnikID, String naziv) {
		try {
            String sql = "UPDATE " + StrojENTITY.class.getName() +
            		" e SET e.naziv = :naziv " +
            		" WHERE e.id = :strojID AND e.korisnik.id = :korisnikID";
            
            entityManager.createQuery(sql)
            			.setParameter("korisnikID", korisnikID)
            			.setParameter("strojID", strojID)
            			.setParameter("naziv", naziv)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public List<PosaoENTITY> jobList(int korisnikID, int strojID, List<PosaoENTITY> jobs) {
		String sql = "SELECT e FROM " + PosaoStrojENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :korisnikID AND e.stroj.id = :strojID";
		
		Query query = entityManager.createQuery(sql, PosaoStrojENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("strojID", strojID);
        
		List<PosaoStrojENTITY> list = (List<PosaoStrojENTITY>) query.getResultList();
		List<PosaoENTITY> list2 = new LinkedList<>();
		
		for(PosaoStrojENTITY posaoStroj: list) {
			for(PosaoENTITY posao: jobs) {
				if(posaoStroj.getPosao().getID() == posao.getID()) {
					list2.add(posao);
					break;
				}
			}
		}
		
		return list2;
	}
}