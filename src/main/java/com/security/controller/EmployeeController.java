package com.security.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.security.dto.EmployeeDto;
import com.security.exception.EmployeeNotFoundException;
import com.security.exception.InvalidTokenException;
import com.security.exception.RoleMismatchException;
import com.security.model.Employee;
import com.security.serviceImpl.EmployeeServiceImpl;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeServiceImpl empService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/addemployee")
	@PreAuthorize("hasRole('ROLE_HR')") // Restrict access to the "HR" role
	public ResponseEntity<Employee> saveEmployee(@RequestBody @Valid EmployeeDto empDto, HttpServletRequest request) {
		try {	
			// Process the request
			Employee emp = modelMapper.map(empDto, Employee.class);
			Employee savedEmployee = empService.saveEmployee(emp);
			return ResponseEntity.ok(savedEmployee);
		} catch (RoleMismatchException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		} catch (InvalidTokenException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		} catch (EmployeeNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save employee");
		}
	}
	@GetMapping("/employee/{id}")
	@PreAuthorize("hasRole('ROLE_HR')")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Integer id) throws EmployeeNotFoundException {

		Employee employee = empService.getEmployeeById(id);

		EmployeeDto empDto = modelMapper.map(employee, EmployeeDto.class);

		if (employee != null) {

			return new ResponseEntity<EmployeeDto>(empDto, HttpStatus.OK);

		} else {

			throw new EmployeeNotFoundException("Employee id not found in database please check once");
		}
	}

	@GetMapping("/employee")
	@PreAuthorize("hasRole('ROLE_HR')")
	public ResponseEntity<?> getAllEmployees() {

		List<Employee> allEmployees = empService.getAllEmployees();
		List<EmployeeDto> employeeDto = new ArrayList<>();
		EmployeeDto empDto = modelMapper.map(allEmployees, EmployeeDto.class);
		if (!allEmployees.isEmpty()) {
			for (Employee emp : allEmployees) {
				employeeDto.add(modelMapper.map(emp, EmployeeDto.class));
			}

		}
		return new ResponseEntity<>(employeeDto, HttpStatus.OK);
	}

	@DeleteMapping("/employee/{id}")
	@PreAuthorize("hasRole('ROLE_HR')")
	public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) throws EmployeeNotFoundException {
		ResponseEntity<?> resp = null;
		try {
			empService.deleteEmployee(id);
			resp = new ResponseEntity<>("Deleted employee id=" + id + " successfully", HttpStatus.OK);
		} catch (Exception e) {
			throw new EmployeeNotFoundException("Employee id not found in database");
		}
		return resp;
	}

	@PutMapping(value = "/employee/{id}")
	@PreAuthorize("hasRole('ROLE_HR')")
	public ResponseEntity<?> updateEmployee(@PathVariable Integer id, @RequestBody EmployeeDto employeeDto)
			throws EmployeeNotFoundException {

		Employee emp = empService.getEmployeeById(id);
		if (emp != null) {
			emp.setEmpName(employeeDto.getEmpName());
			emp.setEmpEmail(employeeDto.getEmpEmail());
			emp.setEmpPhNum(employeeDto.getEmpPhNum());
			emp.setEmpSal(employeeDto.getEmpSal());
			emp.setEmpGender(employeeDto.getEmpGender());
			empService.updateEmployee(emp);
			return new ResponseEntity<>(emp, HttpStatus.OK);
		} else {
			throw new EmployeeNotFoundException("Employee id not found in database");
		}
	}
}