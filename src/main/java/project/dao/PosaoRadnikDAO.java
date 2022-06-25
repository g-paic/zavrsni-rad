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
import project.entities.PosaoRadnikENTITY;
import project.entities.RadnikENTITY;
import project.models.Radnik;

@Repository
@Transactional
public class PosaoRadnikDAO {
	@Autowired
    private EntityManager entityManager;
	
	private static String tableName = "PosaoStroj";
	
	public List<RadnikENTITY> choices(int korisnikID, PosaoENTITY job, List<PosaoENTITY> jobs, List<RadnikENTITY> workers) {
		String sql = "SELECT e FROM " + PosaoRadnikENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :ID";
		
		Query query = entityManager.createQuery(sql, PosaoRadnikENTITY.class);
        query.setParameter("ID", korisnikID);
		List<PosaoRadnikENTITY> list = (List<PosaoRadnikENTITY>) query.getResultList();
        
        List<RadnikENTITY> workersReady = new LinkedList<>();
        
        for(RadnikENTITY radnik: workers) {
        	boolean f1 = false;
        	boolean f2 = false;
        	for(PosaoRadnikENTITY posaoRadnik: list) {
        		if(posaoRadnik.getRadnik().getID() == radnik.getID()) {
        			f1 = true;
        			for(PosaoENTITY posao: jobs) {
        				if(posao.getID() == posaoRadnik.getPosao().getID()) {
        					long a1 = job.getDatumPocetka().toEpochDay();
        					long a2 = job.getDatumKraja().toEpochDay();
        					
        					long b1 = posao.getDatumPocetka().toEpochDay();
        					long b2 = posao.getDatumKraja().toEpochDay();
        					
        					if((a1 >= b1 && a1 <= b2) || (a2 >= b1 && a2 <= b2)) {
        						f2 = true;
        						break;
        					}
        					
        					if((b1 >= a1 && b1 <= a2) || (b2 >= a1 && b2 <= a2)) {
        						f2 = true;
        						break;
        					}
        					
        					if(b1 <= a1 && b2 >= a2) {
        						f2 = true;
        						break;
        					}
        					
        					if(a1 <= b1 && a2 >= b2) {
        						f2 = true;
        						break;
        					}
        				}
        			}
        			
        			if(f2) {
        				break;
        			}
        		}
        	}
        	
        	if(!f1 || (f1 && !f2)) {
        		workersReady.add(radnik);
			}
        }
        
		return workersReady;
	}
	
	public List<Radnik> showWorkersForJob(int korisnikID, int posaoID, List<RadnikENTITY> workers) {
		String sql = "SELECT e FROM " + PosaoRadnikENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID";
		
		Query query = entityManager.createQuery(sql, PosaoRadnikENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("posaoID", posaoID);
		List<PosaoRadnikENTITY> list = (List<PosaoRadnikENTITY>) query.getResultList();
		
		//List<RadnikENTITY> workersReady = new LinkedList<>();
		List<Radnik> radnici = new LinkedList<>();
        for(RadnikENTITY radnik: workers) {
        	for(PosaoRadnikENTITY posaoRadnik: list) {
        		if(posaoRadnik.getRadnik().getID() == radnik.getID()) {
        			Radnik r = new Radnik(radnik.getID(), radnik.getIme(), radnik.getPrezime(), radnik.getKorisnickoIme(), posaoRadnik.getIsplata());
        			radnici.add(r);
        			//workersReady.add(radnik);
        		}
        	}
        }
        
		return radnici;
	}
	
	public boolean setWorkerForJob(int korisnikID, int posaoID, int radnikID, double isplata) {
		try {
            String sql = "INSERT INTO " + "posao_radnik" + " (korisnikid, posaoid, radnikid, isplata) VALUES (?, ?, ?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, korisnikID)
            			.setParameter(2, posaoID)
            			.setParameter(3, radnikID)
            			.setParameter(4, isplata)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
//	public boolean setNewIncomeForJob(int ID, double zarada) {
//		try {
//            String sql = "UPDATE " + PosaoENTITY.class.getName() +
//            		" e SET e.zarada = :zarada " +
//            		" WHERE e.id = :ID";
//            
//            entityManager.createQuery(sql)
//            			.setParameter("zarada", zarada)
//            			.setParameter("ID", ID)
//            			.executeUpdate();
//        } catch(NoResultException e) {
//            return false;
//        }
//		
//		return false;
//	}
//	
//	public boolean setNewCostForJob(int ID, double troškovi) {
//		try {
//            String sql = "UPDATE " + PosaoENTITY.class.getName() +
//            		" e SET e.troškovi = :troškovi " +
//            		" WHERE e.id = :ID";
//            
//            entityManager.createQuery(sql)
//            			.setParameter("troškovi", troškovi)
//            			.setParameter("ID", ID)
//            			.executeUpdate();
//        } catch(NoResultException e) {
//            return false;
//        }
//		
//		return false;
//	}
	
	public boolean removeWorkerForJob(int korisnikID, int posaoID, int radnikID) {
		try {
			String sql = "DELETE FROM "  + PosaoRadnikENTITY.class.getSimpleName() + " e "
					+ "WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID AND e.radnik.id = :radnikID";
			entityManager.createQuery(sql)
					.setParameter("korisnikID", korisnikID)
					.setParameter("posaoID", posaoID)
					.setParameter("radnikID", radnikID)
					.executeUpdate();
		} catch(NoResultException e) {
			return false;
		}
		
		return false;
	}
	
	public PosaoRadnikENTITY jobWorker(int korisnikID, int posaoID, int radnikID) {
		try {
            String sql = "SELECT e FROM " + PosaoRadnikENTITY.class.getName() + " e " //
                    + "WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID AND e.radnik.id = :radnikID";

            Query query = entityManager.createQuery(sql, PosaoRadnikENTITY.class);
            query.setParameter("korisnikID", korisnikID);
            query.setParameter("posaoID", posaoID);
            query.setParameter("radnikID", radnikID);
            
            return (PosaoRadnikENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
	
	@SuppressWarnings("unchecked")
	public List<PosaoRadnikENTITY> getJobWorkers(int korisnikID, int posaoID) {
		String sql = "SELECT e FROM " + PosaoRadnikENTITY.class.getName() + " e " //
                + "WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID";
        
    	Query query = entityManager.createQuery(sql, PosaoRadnikENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("posaoID", posaoID);
        
        return (List<PosaoRadnikENTITY>) query.getResultList();
    }
}