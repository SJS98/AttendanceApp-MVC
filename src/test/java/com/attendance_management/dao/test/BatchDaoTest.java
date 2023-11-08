package com.attendance_management.dao.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.attendance_management.dao.BatchDao;
import com.attendance_management.entity.Batch;

class BatchDaoTest {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("attendance_management");
	EntityManager em = emf.createEntityManager();
	BatchDao dao = new BatchDao(em);

	@Test
	@Disabled
	void testBatchUser() {

		String newSubject = "JUnit5";
		Batch batch = new Batch(0, null, null, true, newSubject, null);

		Batch savedBatch = dao.saveBatch(batch);

		String currentSubject = savedBatch.getSubject();

		assertEquals(newSubject, currentSubject);
	}

	@Test
	void testUpdateBatch() {

		int batchId = 3;

		Batch existingBatch = dao.findById(batchId);

		boolean previousStatus = existingBatch.isStatus();
		boolean updatedStatus = true;
		existingBatch.setStatus(updatedStatus);

		dao.updateBatch(existingBatch);

		Batch updatedBatch = dao.findById(batchId);
		boolean currentStatus = updatedBatch.isStatus();

		assertNotEquals(currentStatus, previousStatus);

		assertEquals(updatedStatus, currentStatus);

	}

	@Test
	void testFindBatchByInvalidId() {

		int batchId = 3;

		Batch batch = dao.findById(batchId);

		assertNull(batch);
	}

	@Test
	void testFindBatchById() {

		int batchId = 3;
		String expectedSubjectName = "JavaScript";

		Batch batch = dao.findById(batchId);

		String actualSubjectName = batch.getSubject();

		assertEquals(expectedSubjectName, actualSubjectName);

	}

	@Test
	void testFindAllBatches() {

		List<Batch> batches = dao.findAllBatches();

		assertNotNull(batches);

	}

	@Test
	void testFindBatchesByUserId() {

		int TrainerId = 9;

		List<Batch> batches = dao.findBatchesByUserId(TrainerId);

		assertNotNull(batches);
	}
	

	@Test
	void testFindBatchesByInvalidUserId() {

		int TrainerId = 1010;

		List<Batch> batches = dao.findBatchesByUserId(TrainerId);

		assertThrowsExactly(IndexOutOfBoundsException.class, ()->batches.get(0));
	}

}