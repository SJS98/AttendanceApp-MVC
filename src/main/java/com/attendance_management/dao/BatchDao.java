package com.attendance_management.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.attendance_management.entity.Batch;
import com.attendance_management.entity.User;

@Component
public class BatchDao {

	EntityManager em;
	EntityTransaction et;

	@Autowired
	public BatchDao(@Qualifier("em") EntityManager em) {
		this.em = em;
		this.et = em.getTransaction();
	}

	public BatchDao() {
		em = Persistence.createEntityManagerFactory("attendance_management").createEntityManager();
		et = em.getTransaction();
	}
	
	public Batch saveBatch(Batch batch) {
		
		batch.setAttendances(new ArrayList<>());
		et.begin();
		em.persist(batch);
		et.commit();
		return batch;
	}

	public Batch updateBatch(Batch user) {
		et.begin();
		em.merge(user);
		et.commit();
		return user;
	}

	public List<Batch> findAllBatches() {
		String jpql = "from Batch";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	public Batch findById(int id) {
		String jpql = "from Batch where id=:id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", id);
		try {
			return (Batch) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Batch> findBatchesByUserId(int id) {
		User user = em.find(User.class, id);
		return user != null ? user.getBatches() : null;
	}
}
