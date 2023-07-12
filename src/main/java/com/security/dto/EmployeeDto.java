package com.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

	private Integer empId;
	private String empName;
	private String empEmail;
	private String empPhNum;
	private int empSal;
	private String empGender;
	
}