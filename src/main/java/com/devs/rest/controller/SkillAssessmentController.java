package com.devs.rest.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.devs.rest.domain.SkillAssessment;
import com.devs.rest.service.DeveloperService;

@Path("/skillAssessments")
public class SkillAssessmentController {
	
	private DeveloperService devService;
	
	public SkillAssessmentController() {
		devService = new DeveloperService();
	}
	
	@GET
	public String getSkillAssessments() {
		return "skill assessssss";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/add")
	public String addSkillAssessment(SkillAssessment skillAss) {

		try {
			String message = devService.addSkillAssessment(skillAss);
			return message;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}

	}

}
