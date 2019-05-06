package com.devs.rest.service;

import com.devs.rest.dao.SkillDao;
import com.devs.rest.domain.Skill;

public class SkillService {
	SkillDao skillDao;
	
	public SkillService() {
		skillDao = new SkillDao();
	}
	
	public String addSkill(Skill skill) {
		StringBuffer message = new StringBuffer();
		if(skill.getSkill().trim().equals("")) {
			message.append("Skill name cannot be blank.\n");
		} else if(skill.getSkill().length() < 2 
				|| skill.getSkill().length() > 255) {
			message.append("Skill name must be 2 - 255 characters.");
		}
		
		if(message.length() <= 0) { // meaning skill name is valid
			boolean added = skillDao.addSkill(skill); // add to database
			if(added) message.append("New skill has been successfully added.");
			else message.append("Something went wrong. Couldn't add new skill");
		}
		
		return message.toString();
	}
}
