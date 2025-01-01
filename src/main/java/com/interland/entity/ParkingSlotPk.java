package com.interland.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Embeddable
public class ParkingSlotPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="bookId")
	private String bookId;
	@Column(name="vehicleNo")
	private String vehicleNo;
	@Column(name="phoneNO")
	private String phoneNo;

}
