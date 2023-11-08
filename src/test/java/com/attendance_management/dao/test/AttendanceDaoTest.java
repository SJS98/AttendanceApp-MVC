package com.attendance_management.dao.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.attendance_management.dao.AttendanceDao;
import com.attendance_management.entity.Attendance;

class AttendanceDaoTest {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("attendance_management");
	EntityManager em = emf.createEntityManager();
	AttendanceDao dao = new AttendanceDao(em);

	@Test
	@Disabled
	void testSaveAttendance() {

		Attendance attendance = new Attendance();
		attendance.setNumOfStudent(50);
		Attendance savedAttendance = dao.saveAttendance(attendance);
		LocalDateTime createdDateTime = savedAttendance.getCreatedDateTime();

		assertNotNull(createdDateTime);
	}

	@Test
	void testFindAttendanceById() {

		int attendanceId = 6;
		int expectedImageId = 5;

		Attendance attendance = dao.findById(attendanceId);

		int actualImageId = attendance.getImage().getId();

		assertEquals(expectedImageId, actualImageId);
	}

	@Test
	void testFindAllBatches() {

		List<Attendance> attendances = dao.findAllAttendance();

		assertNotNull(attendances);

	}
}