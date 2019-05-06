package com.devs.rest.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.devs.rest.dao.DeveloperDao;
import com.devs.rest.dao.SkillDao;
import com.devs.rest.domain.Developer;
import com.devs.rest.domain.SkillAssessment;

public class DeveloperService {
	DeveloperDao devDao;
	SkillDao skillDao;
	
	public DeveloperService() {
		devDao = new DeveloperDao();
		skillDao = new SkillDao();
	}
	
	public String addDeveloper(Developer dev) {
		StringBuffer message = new StringBuffer();
		
		// generate developer Id
		dev.setDeveloperId(getNewDevId());
		
		if(dev.getFirstName().trim().length() <= 0) {
			message.append("First name cannot be blank.\n");
		} else if(dev.getFirstName().length() < 2 
				|| dev.getFirstName().length() > 255) {
			message.append("First name can only be 2 - 255 characters.");
		}
		
		if(dev.getLastName().trim().length() <= 0) {
			message.append("Last name cannot be blank.\n");
		}  else if(dev.getLastName().length() < 2 
				|| dev.getLastName().length() > 255) {
			message.append("Last name can only be 2 - 255 characters.");
		} 
		
		if(dev.getPosition().trim().length() <= 0) {
			message.append("Position cannot be blank.\n");
		}  else if(dev.getPosition().length() < 2 
				|| dev.getPosition().length() > 255) {
			message.append("Position can only be 2 - 255 characters.");
		}
		
//		try {
//			dev.getBirthDate();
//		} catch(Exception e) {
//			// meaning invalid date format
//			message.append("Birth date must be in YYYY-MM-DD format.");
//		}
		
		if(message.length() <= 0) {
			// meaning everything valid
			Boolean added = devDao.addDeveloper(dev);
			if(added) {
				message.append("New developer has been successfully added.");
			} else message.append("Something went wrong. Couldn't add developer.");
		}
		
		return message.toString();
	}
	
	public String addSkillAssessment(SkillAssessment skillAss) {
		StringBuffer message = new StringBuffer();
		
		if(!devDao.isDeveloperIdExisting(skillAss.getDeveloperId())) {
			message.append("Unrecognised developer ID.\n");
		}
		if(!skillDao.isSkillIdExisting(skillAss.getSkillId())) {
			message.append("Unrecognised skill ID.\n");
		}
		
		if(message.length() <= 0) {
			Boolean added = devDao.addSkillAssessment(skillAss);
			if(added) message.append("New skill assessment has been successfuly added.");
			else message.append("Something went wrong. Couldn't add new skill assessment.");
		}
		
		return message.toString();
	}
	
	public List<SkillAssessment> searchDevelopers(
			String skill, 
			String skillLevel, 
			String firstName,
			String lastName,
			String monthsOfExperience) {
		
		skill = createSearchValue(skill);
		skillLevel = createSearchValue(skillLevel);
		firstName = createSearchValue(firstName);
		lastName = createSearchValue(lastName);
		
		List<SkillAssessment> results;
		
		System.out.println("VALLLLLLLLL = " + StringUtils.isBlank(String.valueOf(monthsOfExperience)));
		if(StringUtils.isBlank(monthsOfExperience)) {
			// call query  function that does not include months of experience
			System.out.println("without months of experience");
			results = devDao.searchDevelopers(skill, skillLevel, firstName, lastName);
		} else {
			// call query that includes all attributes above
			System.out.println("With months of experienc");
			results = devDao.searchDevelopers(skill, skillLevel, firstName, lastName, Integer.parseInt(monthsOfExperience));
		}

		return results;
	}
	
	private String createSearchValue(String string) {
		String value;
		
		if (StringUtils.isBlank(string)) {
			value = "%";
		} else {
			value = "%"+string+"%";
		}
		return value;
	}
	
	@SuppressWarnings("deprecation")
	public int getNewDevId() {
		Date date = new Date();
		String random = get4DigitRandom();
		String id = random + date.getMonth() + date.getYear() + date.getDay();
		
		return Integer.parseInt(id);
	}
	
	public String get4DigitRandom() {
		Random random = new Random();
		return String.format("%04d", random.nextInt(10000));
	}
}
