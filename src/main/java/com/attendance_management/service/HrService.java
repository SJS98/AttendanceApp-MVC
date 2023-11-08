package com.attendance_management.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.attendance_management.dao.AttendanceDao;
import com.attendance_management.dao.BatchDao;
import com.attendance_management.dao.UserDao;
import com.attendance_management.entity.Attendance;
import com.attendance_management.entity.Batch;
import com.attendance_management.entity.User;

@Component
public class HrService {

	@Autowired
	private BatchDao brepository;

	@Autowired
	private UserDao urepository;

	@Autowired
	private AttendanceDao arepository;

	public void saveAttendance(Attendance attendance) {
		arepository.saveAttendance(attendance);
	}

	public User saveUser(User user) {
		return userFlag(user) ? urepository.saveUser(user) : null;
	}

	public User updateUser(User user) {
		return userFlag(user) ? urepository.updateUser(user) : null;
	}

	private boolean userFlag(User user) {
		System.out.println(user);
		if (user == null || user.getEmail() == null || user.getPassword() == null || user.getRole() == null
				|| user.getPhone() <= 999999999)
			return false;
		return true;
	}

	public Batch saveBatchByUserId(Batch batch, int userId) {

		User user = urepository.findUserById(userId);
		if (user == null)
			return null;

		batch.setAttendances(new ArrayList<>());

		batch = brepository.saveBatch(batch);

		List<Batch> batches = user.getBatches();
		batches.add(batch);
		user.setBatches(batches);

		urepository.updateUser(user);
		return batch;
	}

	public List<Batch> findBatchesByUserId(int userId) {
		User user = urepository.findUserById(userId);
		return user != null ? user.getBatches() : null;
	}

	public User findUserByEmailAndPassword(String email, String password) {
		return urepository.findUserByEmailAndPass(email, password);
	}

	public User findUserByEmail(String email) {
		return urepository.findUserByEmail(email);
	}

	public User findUserByPhone(long phone) {
		return urepository.findUserByPhone(phone);
	}

	public Batch saveBatch(String subject) {

		Batch batch = new Batch();
		batch.setSubject(subject);

		return brepository.saveBatch(batch);
	}

	public List<Batch> findAllBatches() {
		return brepository.findAllBatches();
	}

	public Batch updateBatch(Batch b) {
		return brepository.updateBatch(b);
	}

	public User findUserByEmployId(String empId) {
		return urepository.findUserByEmpId(empId);
	}

	public Batch findBatchById(int id) {
		return brepository.findById(id);
	}

	public Batch findNewBatchBySubjectAndNoDateTime(String subject) {
		return brepository.findById(0);
	}

	public List<User> findUserByEmployeeByRole(String role) {
		return urepository.findUsersByRole(role);
	}

}