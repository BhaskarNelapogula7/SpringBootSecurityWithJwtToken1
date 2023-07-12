package com.security.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.model.Employee;
import com.security.repository.EmployeeRepository;
import com.security.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepo;

	@Override
	public Employee saveEmployee(Employee empObj) {

		return employeeRepo.save(empObj);
	}

	@Override
	public Employee getEmployeeById(Integer id) {

		Employee employee = employeeRepo.findById(id).get();
		return employee;
	}

	@Override
	public List<Employee> getAllEmployees() {

		return employeeRepo.findAll();

	}

	@Override
	public void deleteEmployee(Integer id) {

		employeeRepo.deleteById(id);
	}

	@Override
	public Employee updateEmployee(Employee empObj) {

		return employeeRepo.save(empObj);
	}

}
