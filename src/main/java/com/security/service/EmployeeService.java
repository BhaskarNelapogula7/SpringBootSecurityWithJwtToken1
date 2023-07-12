package com.security.service;

import java.util.List;

import com.security.model.Employee;

public interface EmployeeService {

	public Employee saveEmployee(Employee empObj);

	public Employee getEmployeeById(Integer id);

	public List<Employee> getAllEmployees();

	public void deleteEmployee(Integer id);

	public Employee updateEmployee(Employee empObj);

}
