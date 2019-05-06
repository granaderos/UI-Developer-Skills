package com.devs.rest.domain;

import java.util.List;

public class SkillAssessment {
	private int developerId;
	private int skillId;
	
	private int monthsOfExperience;
	private String skillLevel;
	
	// used for viewing / retrieving
	private Developer developer;
	private List<Skill> skill;
	
	public int getDeveloperId() {
		return developerId;
	}
	public void setDeveloperId(int developerId) {
		this.developerId = developerId;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
	public Developer getDeveloper() {
		return developer;
	}
	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
	public List<Skill> getSkill() {
		return skill;
	}
	public void setSkill(List<Skill> skill) {
		this.skill = skill;
	}
	
	
	public int getMonthsOfExperience() {
		return monthsOfExperience;
	}
	public void setMonthsOfExperience(int monthsOfExperience) {
		this.monthsOfExperience = monthsOfExperience;
	}
	public String getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}
	
}
