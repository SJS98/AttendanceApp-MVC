package com.attendance_management.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.attendance_management.entity.Attendance;

@Component
public class AttendanceDao {

	EntityManager em;
	EntityTransaction et;

	@Autowired
	public AttendanceDao(@Qualifier("em") EntityManager em) {
		this.em = em;
		this.et = em.getTransaction();
	}

	public Attendance saveAttendance(Attendance user) {
		et.begin();
		em.persist(user);
		et.commit();
		return user;
	}

	public List<Attendance> findAllAttendance() {
		String jpql = "from Attendance";
		Query query = em.createQuery(jpql);
		return query.getResultList();
	}

	public Attendance findById(int id) {
		String jpql = "from Attendance where id=:id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", id);

		try {
			return (Attendance) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
