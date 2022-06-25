package project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.entities.MaterijalENTITY;

@Repository
@Transactional
public class MaterijalDAO {
	@Autowired
    private EntityManager entityManager;
	
	public List<MaterijalENTITY> materials() {
		String sql = "SELECT e FROM " + MaterijalENTITY.class.getName() + " e ";
		
		Query query = entityManager.createQuery(sql, MaterijalENTITY.class);
		List<MaterijalENTITY> list = (List<MaterijalENTITY>) query.getResultList();
		
		return list;
	}
}