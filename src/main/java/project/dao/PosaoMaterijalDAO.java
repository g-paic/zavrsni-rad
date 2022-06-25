package project.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.entities.MaterijalENTITY;
import project.entities.PosaoENTITY;
import project.entities.PosaoMaterijalENTITY;
import project.models.Materijal;

@Repository
@Transactional
public class PosaoMaterijalDAO {
	@Autowired
    private EntityManager entityManager;
	
	public List<MaterijalENTITY> materials() {
		String sql = "SELECT e FROM " + MaterijalENTITY.class.getName() + " e ";
		
		Query query = entityManager.createQuery(sql, MaterijalENTITY.class);
		List<MaterijalENTITY> list = (List<MaterijalENTITY>) query.getResultList();
		
		return list;
	}
	
	public int getMaterialID(String name, List<MaterijalENTITY> list) {
		for(MaterijalENTITY material: list) {
			if(material.getNaziv().equals(name)) {
				return material.getID();
			}
		}
		
		return 0;
	}
	
	public boolean insertMaterial(String name) {
		try {
            String sql = "INSERT INTO " + "Materijal" + " (naziv) VALUES (?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, name)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean setMaterialForJob(int korisnikID, int posaoID, int materijalID, double cijena) {
		try {
            String sql = "INSERT INTO " + "posao_materijal" + " (korisnikid, posaoid, materijalid, cijena) VALUES (?, ?, ?, ?)";
            
            entityManager.createNativeQuery(sql)
            			.setParameter(1, korisnikID)
            			.setParameter(2, posaoID)
            			.setParameter(3, materijalID)
            			.setParameter(4, cijena)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public boolean setNewIncomeForJob(int ID, double prihod) {
		try {
            String sql = "UPDATE " + PosaoENTITY.class.getName() +
            		" e SET e.prihod = :prihod " +
            		" WHERE e.id = :ID";
            
            entityManager.createQuery(sql)
            			.setParameter("prihod", prihod)
            			.setParameter("ID", ID)
            			.executeUpdate();
        } catch(NoResultException e) {
            return false;
        }
		
		return false;
	}
	
	public List<Materijal> showMaterialsForJob(int korisnikID, int posaoID, List<MaterijalENTITY> materials) {
		String sql = "SELECT e FROM " + PosaoMaterijalENTITY.class.getName() + " e " //
                + " WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID";
		
		Query query = entityManager.createQuery(sql, PosaoMaterijalENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("posaoID", posaoID);
		List<PosaoMaterijalENTITY> list = (List<PosaoMaterijalENTITY>) query.getResultList();
		
		//List<MaterijalENTITY> materialsReady = new LinkedList<>();
		List<Materijal> materijali = new LinkedList<>();
        for(MaterijalENTITY material: materials) {
        	for(PosaoMaterijalENTITY posaoMaterijal: list) {
        		if(posaoMaterijal.getMaterijal().getID() == material.getID()) {
        			Materijal m = new Materijal(material.getID(), material.getNaziv(), posaoMaterijal.getCijena());
        			materijali.add(m);
        			//materialsReady.add(material);
        		}
        	}
        }
        
		return materijali;
	}
	
	public PosaoMaterijalENTITY jobMaterial(int korisnikID, int posaoID, int materialID) {
		try {
            String sql = "SELECT e FROM " + PosaoMaterijalENTITY.class.getName() + " e " //
                    + "WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID AND e.materijal.id = :materialID";

            Query query = entityManager.createQuery(sql, PosaoMaterijalENTITY.class);
            query.setParameter("korisnikID", korisnikID);
            query.setParameter("posaoID", posaoID);
            query.setParameter("materialID", materialID);
            
            return (PosaoMaterijalENTITY) query.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
	
	public boolean removeMaterialForJob(int korisnikID, int posaoID, int materialID) {
		try {
			String sql = "DELETE FROM "  + PosaoMaterijalENTITY.class.getSimpleName() + " e "
					+ "WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID AND e.materijal.id = :materialID";
			entityManager.createQuery(sql)
					.setParameter("korisnikID", korisnikID)
					.setParameter("posaoID", posaoID)
					.setParameter("materialID", materialID)
					.executeUpdate();
		} catch(NoResultException e) {
			return false;
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<PosaoMaterijalENTITY> getJobMaterials(int korisnikID, int posaoID) {
		String sql = "SELECT e FROM " + PosaoMaterijalENTITY.class.getName() + " e " //
                + "WHERE e.korisnik.id = :korisnikID AND e.posao.id = :posaoID";
        
    	Query query = entityManager.createQuery(sql, PosaoMaterijalENTITY.class);
        query.setParameter("korisnikID", korisnikID);
        query.setParameter("posaoID", posaoID);
        
        return (List<PosaoMaterijalENTITY>) query.getResultList();
    }
}