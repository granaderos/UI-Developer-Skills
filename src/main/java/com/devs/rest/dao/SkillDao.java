package com.devs.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.devs.rest.domain.Skill;

public class SkillDao {
	DatabaseConnection dbConnection;
	
	public SkillDao() {
		dbConnection = new DatabaseConnection();
	}
	
	public boolean addSkill(Skill skill) {
		boolean added = false;
		
		String sql = "INSERT INTO skills VALUES (null, ?)";

		try (Connection connection = dbConnection.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, skill.getSkill());
			ps.executeUpdate();
			added = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return added;
	}
	
	public boolean isSkillIdExisting(int skillId) {
		boolean doesExists = false;
		
		String sql = "SELECT * FROM skills WHERE skillId = ?";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, skillId);
			
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
}
