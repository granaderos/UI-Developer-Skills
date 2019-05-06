package com.devs.rest.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.devs.rest.domain.Developer;
import com.devs.rest.domain.SkillAssessment;
import com.devs.rest.service.DeveloperService;

@Path("/developers")
public class DeveloperController {
	private DeveloperService devService;
	
	public DeveloperController() {
		devService = new DeveloperService();
	}
	
	@GET
	public String getDevelopers() {
		return "Devsss";
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SkillAssessment> searchDevelopers(
			@QueryParam("skill") String skill, 
			@QueryParam("skillLevel") String skillLevel,
			@QueryParam("firstName") String firstName, 
			@QueryParam("lastName") String lastName,
			@QueryParam("monthsOfExperience") String monthsOfExperience 
		) {
		return devService.searchDevelopers(skill, skillLevel, firstName, lastName, monthsOfExperience);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/add")
	public String addDeveloper(Developer dev) {

		try {
			String message = devService.addDeveloper(dev);
			return message;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}

	}
}
