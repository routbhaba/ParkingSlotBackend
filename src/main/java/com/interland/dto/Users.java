package com.interland.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Users {
	@Id
	@Column(name="user_name")
	@NotBlank
	private String userName;
	
	private String password;
	
	private String email;
	 	
	

}
