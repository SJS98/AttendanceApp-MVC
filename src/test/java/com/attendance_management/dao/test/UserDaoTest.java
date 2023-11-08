package com.attendance_management.dao.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.attendance_management.dao.UserDao;
import com.attendance_management.entity.User;

import java.util.*;

class UserDaoTest {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("attendance_management");
	EntityManager em = emf.createEntityManager();
	UserDao dao = new UserDao(em);

	@Test
	@Disabled
	void testSaveUser() {

		String email = "rahul@1234";
		String password = "123";

		User trainer = new User(0, null, "Rahul Kurdu", email, 8908908908l, "123", "Trainer", true, null, null);

		dao.saveUser(trainer);

		User employee = dao.findUserByEmailAndPass(email, password);

		assertNotNull(employee);

	}

	@Test
	@Disabled
	void testSaveUserByInvalidEmail() {

		String email = null;
		String password = "123";

		User trainer = new User(0, null, "Rahul Kurdu", email, 8908908903l, "123", "Trainer", true, null, null);

		dao.saveUser(trainer);

		User employee = dao.findUserByEmailAndPass(email, password);

		assertNotNull(employee);

	}

	@Test
	@Disabled
	void testSaveUserByRepeatedEmail() {

		String email = "rahul@123";
		String password = "123";

		User trainer = new User(0, null, "Rahul Kurdu", email, 8908908905l, "123", "Trainer", true, null, null);

		dao.saveUser(trainer);

		User employee = dao.findUserByEmailAndPass(email, password);

		assertNotNull(employee);

	}

	@Test
	@Disabled
	void testUpdateUser() {

		String email = "rahul@123";
		String password = "123";

		User existingTrainer = dao.findUserByEmailAndPass(email, password);

		String previousName = existingTrainer.getName();
		String updatedName = "Virat";
		existingTrainer.setName(updatedName);

		dao.updateUser(existingTrainer);

		User employee = dao.findUserByEmailAndPass(email, password);
		String currentName = employee.getName();

		assertNotEquals(currentName, previousName);

		assertEquals(updatedName, currentName);

	}

	@Test
	void testFindUserByEmpId() {

		String empId = "TYSS00101";
		String expectedName = "Sabir Ali Jafar Shaikh";

		User employee = dao.findUserByEmpId(empId);
		String actualName = employee.getName();

		assertEquals(expectedName, actualName);

	}

	@Test
	void testFindUserByInvalidEmpId() {

		String empId = "TY00100";

		User employee = dao.findUserByEmpId(empId);

		assertNull(employee);

	}

	@Test
	void testFindUserById() {

		User employee = dao.findUserById(9);

		assertNotNull(employee);

		String expectedName = "Rahul";

		String actualName = employee.getName();

		assertEquals(expectedName, actualName);
	}
	
	@Test
	void testFindUserByInvalidId() {

		User employee = dao.findUserById(-1);

		assertNull(employee);
	}

	@Test
	void testFindAllUser() {

		List<User> employees = dao.findAllUsers();

		assertNotNull(employees);

	}

	@Test
	void testFindUsersByRole() {

		List<User> employees = dao.findUsersByRole("Trainer");
		assertNotNull(employees);

	}

	@Test
	void testFindUsersByInvalidRole() {

		List<User> employees = dao.findUsersByRole("Employee");

		assertThrowsExactly(IndexOutOfBoundsException.class, () -> employees.get(0));

	}

	@Test
	void testFindUserByEmail() {
		String email = "rahul@123";
		String expectedName = "Rahul";

		User employee = dao.findUserByEmail(email);
		String actualName = employee.getName();

		assertEquals(expectedName, actualName);
	}

	@Test
	void testFindUserByPhone() {
		long phone = 918356059202l;
		String expectedName = "Sabir Ali Jafar Shaikh";

		User employee = dao.findUserByPhone(phone);
		String actualName = employee.getName();

		assertEquals(expectedName, actualName);
	}
}