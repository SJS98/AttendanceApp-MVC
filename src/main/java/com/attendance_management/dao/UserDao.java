package com.attendance_management.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.attendance_management.entity.Batch;
import com.attendance_management.entity.User;

@Component
public class UserDao {

	EntityManager em;
	EntityTransaction et;

	@Autowired
	public UserDao(@Qualifier("em") EntityManager em) {
		this.em = em;
		this.et = em.getTransaction();
	}

	public User findUserByEmailAndPass(String email, String pass) {
		Query q = em.createQuery("select u from User u where u.email=:email and u.password=:password");
		q.setParameter("email", email);
		q.setParameter("password", pass);
		try {
			return (User) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public String getNewEmployId() {

		Query q = em.createQuery("select u.employId from User u order by u.employId desc");
		List<String> list = q.getResultList();
		
		if(list.isEmpty()) {
			return "TYSS00101";
		}
		
		String s = list.get(0);
		String empId = s.replace("TYSS", "");

		int empNum = Integer.parseInt(empId);

		return empId = "TYSS" + (empNum + 1);
	}

	public User saveUser(User user) {

		user.setEmployId(getNewEmployId());
		user.setBatches(new ArrayList<>());

		et.begin();
		em.persist(user);
		et.commit();
		return user;
	}

	public User updateUser(User user) {
		et.begin();
		em.merge(user);
		et.commit();
		return user;
	}

	public List<User> findAllUsers() {
		String jpql = "from User";
		Query query = em.createQuery(jpql);
		List<User> users = query.getResultList();
		if (users != null && users.size() > 0) {
			return users;
		}
		return null;
	}

	public User findUserById(int id) {
		String jpql = "from User where id=:id";
		Query query = em.createQuery(jpql);
		query.setParameter("id", id);
		try {
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<User> findUsersByRole(String role) {
		String jpql = "from User where role=:role";
		Query query = em.createQuery(jpql);
		query.setParameter("role", role);
		return query.getResultList();
	}

	public User findUserByEmail(String email) {
		String jpql = "from User where email=:email";
		Query query = em.createQuery(jpql);
		query.setParameter("email", email);
		try {
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public User findUserByPhone(long phone) {
		String jpql = "from User where phone=:phone";
		Query query = em.createQuery(jpql);
		query.setParameter("phone", phone);
		try {
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public User findUserByEmpId(String empId) {
		String jpql = "from User where employId=:empId";
		Query query = em.createQuery(jpql);
		query.setParameter("empId", empId);
		try {
			return (User) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}