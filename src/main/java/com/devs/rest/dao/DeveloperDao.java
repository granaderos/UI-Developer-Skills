package com.devs.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.devs.rest.domain.Developer;
import com.devs.rest.domain.Skill;
import com.devs.rest.domain.SkillAssessment;
import com.devs.rest.domain.Song;

public class DeveloperDao {
	DatabaseConnection dbConnection;
	
	public DeveloperDao() {
		dbConnection = new DatabaseConnection();
	}
	
	public boolean addDeveloper(Developer dev) {
		boolean added = false;
		
		String sql = "INSERT INTO developers VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = dbConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, dev.getDeveloperId());
			ps.setString(2, dev.getFirstName());
			ps.setString(3, dev.getMiddleName());
			ps.setString(4, dev.getLastName());
			ps.setDate(5, dev.getBirthDate());
			ps.setString(6, dev.getPosition());
			ps.executeUpdate();
			
			added = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return added;
	}
	
	public boolean isDeveloperIdExisting(int developerId) {
		boolean doesExists = false;
		
		String sql = "SELECT * FROM developers WHERE developerId = ?";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, developerId);
			
			ResultSet results = ps.executeQuery();

			while (results.next()) {
				doesExists = true;
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return doesExists;
	}
	
	public boolean addSkillAssessment(SkillAssessment skillAss) {
		boolean added = false;
		
		String sql = "INSERT INTO skill_assessments VALUES (null, ?, ?, ?, ?)";

		try (Connection connection = dbConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, skillAss.getDeveloperId());
			ps.setInt(2, skillAss.getSkillId());
			ps.setInt(3, skillAss.getMonthsOfExperience());
			ps.setInt(4, getSkillLevelId(skillAss.getSkillLevel()));
			ps.executeUpdate();
			
			added = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return added;
	}
	
	public int getSkillLevelId(String skillLevel) {
		int skillLevelId = -1;
		String sql = "SELECT skillLevelId FROM skill_levels WHERE level = ?";
		
		try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, skillLevel);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				skillLevelId = rs.getInt("skillLevelId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return skillLevelId;
		
	}
	
	public List<SkillAssessment> searchDevelopers(String skill, String skillLevel, String firstName, String lastName) {
		List<SkillAssessment> skillAsss = new ArrayList<>();
		System.out.println("skill = " + skill + " skillLevel = " + skillLevel + " firstName = " + firstName + " lastName = " + lastName);
		String sql = "SELECT d.*, s.*, sa.*, sl.* FROM developers d, skills s, skill_assessments sa, skill_levels sl "
				+ " WHERE s.skill LIKE ? AND s.skillId = sa.skillId AND sl.level LIKE ? AND sl.skillLevelId = sa.skillLevelId "
				+ " AND d.firstName LIKE ? AND d.lastName LIKE ? AND d.developerId = sa.developerId "
				+ " GROUP BY d.developerId";

		System.out.println("QUERY = " + sql);

		try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, skill);
			ps.setString(2, skillLevel);
			ps.setString(3, firstName);
			ps.setString(4, lastName);
//			
			ResultSet rs = ps.executeQuery();
			Developer dev = null;
			Skill skillObj = null;
			SkillAssessment skillAss = null;
			while (rs.next()) {
				dev = new Developer();
				dev.setDeveloperId(rs.getInt("developerId"));
				dev.setFirstName(rs.getString("firstName"));
				dev.setMiddleName(rs.getString("middleName"));
				dev.setLastName(rs.getString("lastName"));
				dev.setBirthDate(rs.getDate("birthDate"));
				dev.setPosition(rs.getString("position"));
				
				String skillSql = "SELECT s.* FROM skills s, skill_assessments sa "
						+ " WHERE s.skillId = sa.skillId AND sa.developerId = ? "
						+ " GROUP BY sa.skillId";
				PreparedStatement psSkills = conn.prepareStatement(skillSql);
				psSkills.setInt(1, rs.getInt("developerId"));
				ResultSet rsSkills = psSkills.executeQuery();
				
				List<Skill> skills = new ArrayList<Skill>();
				
				while(rsSkills.next()) {
					skillObj = new Skill();
					skillObj.setSkillId(rsSkills.getInt("skillId"));
					skillObj.setSkill(rsSkills.getString("skill"));
					skills.add(skillObj);
				}
				
				skillAss = new SkillAssessment();
				skillAss.setDeveloper(dev);
				skillAss.setSkill(skills);
				skillAss.setMonthsOfExperience(rs.getInt("monthsOfExperience"));
				skillAss.setSkillLevel(rs.getString("level"));
				
				skillAsss.add(skillAss);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return skillAsss;
	}
	
	public List<SkillAssessment> searchDevelopers(String skill, String skillLevel, String firstName, String lastName, int monthsOfExperience) {
		List<SkillAssessment> skillAsss = new ArrayList<>();

		String sql = "SELECT d.*, s.*, sa.*, sl.* FROM developers d, skills s, skill_assessments sa, skill_levels sl "
				+ " WHERE s.skill LIKE ? AND s.skillId = sa.skillId AND sl.level LIKE ? AND sl.skillLevelId = sa.skillLevelId "
				+ " AND d.firstName LIKE ? AND d.lastName LIKE ? AND d.developerId = sa.developerId AND sa.monthsOfExperience = ? "
				+ " GROUP BY d.developerId";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, skill);
			ps.setString(2, skillLevel);
			ps.setString(3, firstName);
			ps.setString(4, lastName);
			ps.setInt(5, monthsOfExperience);

			ResultSet rs = ps.executeQuery();
			Developer dev = null;
			Skill skillObj = null;
			SkillAssessment skillAss = null;
			while (rs.next()) {
				dev = new Developer();
				dev.setDeveloperId(rs.getInt("developerId"));
				dev.setFirstName(rs.getString("firstName"));
				dev.setMiddleName(rs.getString("middleName"));
				dev.setLastName(rs.getString("lastName"));
				dev.setBirthDate(rs.getDate("birthDate"));
				dev.setPosition(rs.getString("position"));
				
				String skillSql = "SELECT s.skillId, s.skill FROM skills s, skill_assessments sa "
						+ "WHERE s.skillId = sa.skillId AND sa.developerId = ? GROUP BY s.skillId";
				PreparedStatement psSkills = conn.prepareStatement(skillSql);
				psSkills.setInt(1, rs.getInt("developerId"));
				ResultSet rsSkills = psSkills.executeQuery();
				
				List<Skill> skills = new ArrayList<Skill>();
				
				while(rsSkills.next()) {
					skillObj = new Skill();
					skillObj.setSkillId(rs.getInt("skillId"));
					skillObj.setSkill(rs.getString("skill"));
					skills.add(skillObj);
				}
				
				skillAss = new SkillAssessment();
				skillAss.setDeveloper(dev);
				skillAss.setSkill(skills);
				skillAss.setMonthsOfExperience(rs.getInt("monthsOfExperience"));
				skillAss.setSkillLevel(rs.getString("level"));
				
				skillAsss.add(skillAss);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return skillAsss;
	}
}
