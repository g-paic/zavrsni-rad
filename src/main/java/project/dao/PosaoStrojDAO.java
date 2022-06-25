package project.dao;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.entities.PosaoENTITY;
import project.entities.PosaoStrojENTITY;
import project.entities.ServisENTITY;
import project.entities.StrojENTITY;

@Repository
@Transactional
public class PosaoStrojDAO {
	@Autowired
    private EntityManager entityManager;
	
	private static String tableName = "PosaoStroj";
	
	public List<StrojENTITY> choices(int korisnikID, PosaoENTITY job, List<PosaoENTITY> jobs, List<StrojENTITY> machines) {
		String sql = "SELECT e FROM " + PosaoStrojENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :ID";
		
		Query query = entityManager.createQuery(sql, PosaoStrojENTITY.class);
        query.setParameter("ID", korisnikID);
		List<PosaoStrojENTITY> list = (List<PosaoStrojENTITY>) query.getResultList();
		
		String sql2 = "SELECT e FROM " + ServisENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :ID";
		
		Query query2 = entityManager.createQuery(sql2, ServisENTITY.class);
        query2.setParameter("ID", korisnikID);
		List<ServisENTITY> list2 = (List<ServisENTITY>) query2.getResultList();
        
        List<StrojENTITY> machinesReady = new LinkedList<>();
        
        for(StrojENTITY stroj: machines) {
        	boolean f1 = false;
        	boolean f2 = false;
        	boolean f3 = false;
        	for(PosaoStrojENTITY posaoStroj: list) {
        		if(posaoStroj.getStroj().getID() == stroj.getID()) {
        			f1 = true;
        			for(PosaoENTITY posao: jobs) {
        				if(posao.getID() == posaoStroj.getPosao().getID()) {
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
        	
        	for(ServisENTITY servis: list2) {
        		if(servis.getStroj().getID() == stroj.getID()) {
        			long a1 = job.getDatumPocetka().toEpochDay();
					long a2 = job.getDatumKraja().toEpochDay();
					
					long b1 = servis.getDatumPočetka().toEpochDay();
					long b2 = servis.getDatumKraja().toEpochDay();
					
					if((a1 >= b1 && a1 <= b2) || (a2 >= b1 && a2 <= b2)) {
						f3 = true;
						break;
					}
					
					if((b1 >= a1 && b1 <= a2) || (b2 >= a1 && b2 <= a2)) {
						f3 = true;
						break;
					}
					
					if(b1 <= a1 && b2 >= a2) {
						f3 = true;
						break;
					}
					
					if(a1 <= b1 && a2 >= b2) {
						f3 = true;
						break;
					}
        		}
        		
        		if(f3) {
    				break;
    			}
        	}
        	
        	if((!f1 && !f3) || (!f2 && !f3)) {
				machinesReady.add(stroj);
			}
        }
		
		return machinesReady;
	}
	
	public List<StrojENTITY> showMachinesForJob(int korisnikID, int posaoID, List<StrojENTITY> machines) {
		String sql = "SELECT e FROM " + PosaoStrojENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID";
		
		Query query = entityManager.createQuery(sql, PosaoStrojENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("posaoID", posaoID);
		List<PosaoStrojENTITY> list = (List<PosaoStrojENTITY>) query.getResultList();
		
		List<StrojENTITY> machinesReady = new LinkedList<>();
        for(StrojENTITY stroj: machines) {
        	for(PosaoStrojENTITY posaoStroj: list) {
        		if(posaoStroj.getStroj().getID() == stroj.getID()) {
        			machinesReady.add(stroj);
        		}
        	}
        }
        
		return machinesReady;
	}
	
	public boolean setMachinesForJob(int korisnikID, int posaoID, int strojID) {
		try {
            String sql = "insert into " + "posao_stroj" + " (korisnikid, posaoid, strojid) values (?, ?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, korisnikID)
            			.setParameter(2, posaoID)
            			.setParameter(3, strojID)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean removeMachinesForJob(int korisnikID, int posaoID, int strojID) {
		try {
			String sql = "DELETE FROM "  + PosaoStrojENTITY.class.getSimpleName() + " e "
					+ "WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID AND e.stroj.id = :strojID";
			entityManager.createQuery(sql)
					.setParameter("korisnikID", korisnikID)
					.setParameter("posaoID", posaoID)
					.setParameter("strojID", strojID)
					.executeUpdate();
		} catch(NoResultException e) {
			return false;
		}
		
		return false;
	}
	
	public List<PosaoStrojENTITY> machineForJobs(int korisnikID, int strojID) {
		String sql = "SELECT e FROM " + PosaoStrojENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :korisnikID AND e.stroj.id = :strojID";
		
		Query query = entityManager.createQuery(sql, PosaoStrojENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("strojID", strojID);
        
		return (List<PosaoStrojENTITY>) query.getResultList();
	}
}
