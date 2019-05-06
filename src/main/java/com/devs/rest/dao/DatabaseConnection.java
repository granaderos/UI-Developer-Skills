package com.devs.rest.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	public Connection getConnection() {
		Connection connection = null;
		String host = "127.0.0.1";
		String port = "3306";
		String dbname = "developers";
		String username = "root";
		String password = "";
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mariadb://"+host+":"+port+"/"+dbname, username, password);
			System.out.println("Connected to the datanase.");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}
