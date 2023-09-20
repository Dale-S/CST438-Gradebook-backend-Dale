package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.json.JSONObject;
import org.junit.runners.MethodSorters;
import org.junit.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.AssignmentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JunitTestAssignment {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private AssignmentRepository asRepo;
	
	//test to see if you can create an assignment
	@Test
	@Order(1)
	public void createAssignment() throws Exception {
		MockHttpServletResponse response;
		AssignmentDTO asDTO;
		asDTO = new AssignmentDTO(3,"db design 2", "2023-9-19", "CST 363 - Introduction to Database Systems", 31045);
		//check to see if the first assignment has the title "db design"
		response = mvc.perform(MockMvcRequestBuilders.post("/assignment/create").accept(MediaType.APPLICATION_JSON).content(asJsonString(asDTO)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
	}
	
	//test to see if you can read the assignment created by the previous test case
	@Test
	@Order(2)
	public void getAssignment() throws Exception {
		MockHttpServletResponse response;
		
		response = mvc.perform(MockMvcRequestBuilders.get("/assignment/3").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
		assertEquals(200, response.getStatus()); //Check to see if status went through
		Assignment as = asRepo.findById(3).orElse(null);
		assertEquals("db design 2", as.getName());
	}
	
	//test to see if you can update the assignment from the last two test cases
	@Test
	@Order(3)
	public void updateAssignment() throws Exception {
		MockHttpServletResponse response;
		
		AssignmentDTO asDTO = new AssignmentDTO(3,"db design 2.75", "2023-9-25", "CST 363 - Introduction to Database Systems", 31045);
		
		response = mvc.perform(MockMvcRequestBuilders.put("/assignment/update/3").accept(MediaType.APPLICATION_JSON).content(asJsonString(asDTO)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
		assertEquals(200, response.getStatus()); //Check to see if status went through
		
		//check if assignment name got updated
		Assignment as = asRepo.findById(3).orElse(null);
		assertEquals("db design 2.75", as.getName());
	}
	
	//test to see if you can delete the assignment we created
	@Test
	@Order(4)
	public void deleteAssignment() throws Exception {
		MockHttpServletResponse response;
		
		response = mvc.perform(MockMvcRequestBuilders.delete("/assignment/delete/3?force=yes").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
		assertEquals(200, response.getStatus()); //Check to see if status went through
		
		//check if assignment got deleted
		Assignment as = asRepo.findById(3).orElse(null);
		assertEquals(null, as);
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static <T> T fromJsonString(String str, Class<T> valueType) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
