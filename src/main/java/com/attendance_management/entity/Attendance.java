package com.attendance_management.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int numOfStudent;
	
	@CreationTimestamp
	private LocalDateTime createdDateTime;

	@OneToOne(cascade = CascadeType.ALL)
	private Image image;
}
