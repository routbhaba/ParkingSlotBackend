package com.interland.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class ParkingSlot {
	@EmbeddedId
	private ParkingSlotPk parkingSlotPk;
	@Column(name="customerName")
	private String customerName;
	@Column(name="vehicleType")
	private String vehicleType;
	@Column(name="dateOfParking")
	private LocalDate dateOfParking;
	@Column(name="days")
	private int days;
	@Column(name="price")
	private double price;
	@Column(name="createdDate")
	private LocalDateTime createdDate;
	@Column(name="status")
	private String status;

}
