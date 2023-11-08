package com.attendance_management.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Batch {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@CreationTimestamp
	private LocalDateTime createdDateTime;
	
	private LocalDateTime completedDateTime;
	
	private boolean status;
	
	private String subject;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Autowired
	@Qualifier("emptyList")
	private List<Attendance> attendances;	
}
