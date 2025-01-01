package com.interland.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ParkingSlotDto {
	@NotBlank
	@Pattern(regexp = "[\\d]{3,5}",message="Id must be within 3 to 5 digits")
	private String bookId;
	@NotBlank
	@Pattern(regexp="[A-Z]{2}-[\\d]{2} [A-Z]{1,2}[\\d]{4}",message="Invalid Vehicle Number")
	private String vehicleNo;
	@NotBlank
	@Pattern(regexp = "[\\d]{10}",message="Phone Number must be 10 digits")
	private String phoneNo;
	@NotBlank
	@Pattern(regexp = "[a-zA-Z\\s]{3,25}",message="Name must be a valid Name")
	private String customerName;
	@NotBlank
	private String vehicleType;
	@NotNull
	@FutureOrPresent
	private LocalDate dateOfParking;
	@NotNull
	@Min(1)
	@Max(30)
	private int days;
	private double price;
	private String status;

}
