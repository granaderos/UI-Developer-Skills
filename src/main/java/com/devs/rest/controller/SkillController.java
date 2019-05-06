package com.devs.rest.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.devs.rest.domain.Skill;
import com.devs.rest.service.SkillService;


@Path("/skills")
public class SkillController {
	
	private SkillService skillService;
	
	public SkillController() {
		skillService = new SkillService();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/add")
	public String addDeveloper(Skill skill) {

		try {
			String message = skillService.addSkill(skill);
			return message;
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}

	}

}
