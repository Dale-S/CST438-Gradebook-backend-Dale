package com.cst438.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.lang.*;
import java.lang.Throwable;
import java.lang.Exception;
import java.sql.Date;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin 
public class AssignmentController {
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/assignment")
	public AssignmentDTO[] getAllAssignmentsForInstructor() {
		// get all assignments for this instructor
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		AssignmentDTO[] result = new AssignmentDTO[assignments.size()];
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			AssignmentDTO dto = new AssignmentDTO(
					as.getId(), 
					as.getName(), 
					as.getDueDate().toString(), 
					as.getCourse().getTitle(), 
					as.getCourse().getCourse_id());
			result[i]=dto;
		}
		return result;
	}
	
	// TODO create CRUD methods for Assignment
	@Autowired
	AssignmentGradeRepository agRepo;
	
	//Create assignment
	@PostMapping("/assignment/create")
	public int createAssignment(@RequestBody AssignmentDTO asDTO) {
		Assignment as = new Assignment();
		Course c = courseRepository.findById(asDTO.courseId()).orElse(null);
		if(c == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course with id " + asDTO.courseId() + " not found.");
		}
		else {
			as.setCourse(c);
		}
		//as.setId(asDTO.id());
		as.setName(asDTO.assignmentName());
		as.setDueDate(Date.valueOf(asDTO.dueDate()));
		assignmentRepository.save(as);
		return(as.getId());
	}
	
	//Read assignment
	@GetMapping("/assignment/{id}")
	public AssignmentDTO getAssignment(@PathVariable("id") int id) {
		Assignment found = assignmentRepository.findById(id).orElse(null);
		if(found == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignment with id " + id + " not found.");
		}
		int fId = found.getId(); 
		String fDueDate = found.getDueDate().toString();
		String fName = found.getName();
		int fCourseId = found.getCourse().getCourse_id();
		String fCourseTitle = found.getCourse().getTitle();
		AssignmentDTO newDTO = new AssignmentDTO(fId, fName, fDueDate, fCourseTitle, fCourseId);
		return newDTO;
	}
	
	//Update assignment
	/**
	 * @param asDTO
	 * @param id
	 */
	@PutMapping("/assignment/update/{id}")
	public void updateAssignment(@RequestBody AssignmentDTO asDTO, @PathVariable("id") int id) {
		Assignment found = assignmentRepository.findById(id).orElse(null);
		if(found == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignment with id " + id + " not found.");
		}
		found.setDueDate(Date.valueOf(asDTO.dueDate()));
		found.setName(asDTO.assignmentName());
		Course c = courseRepository.findById(asDTO.courseId()).orElse(null);
		if(c == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course with id " + asDTO.courseId() + " not found.");
		}
		found.setCourse(c);
		assignmentRepository.save(found);
	}
	
	
	//Delete assignment
	@DeleteMapping("/assignment/delete/{id}")
	public void deleteAssignment(@PathVariable("id") int id, @RequestParam("force") Optional<String> force) {
		Assignment found = assignmentRepository.findById(id).orElse(null);
		if(found == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignment with id " + id + " not found.");
		}
		if(agRepo.existsById(id) && force.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignment with id " + id + " has grades attached to it.");
		}
		assignmentRepository.delete(found);
	}
}
