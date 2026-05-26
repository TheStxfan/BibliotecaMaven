package com.biblioteca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {

	private static final Dotenv dotenv = Dotenv.load();
	static String url = dotenv.get("DB_URL");
	static String username = dotenv.get("DB_USERNAME");
	static String password = dotenv.get("DB_PASSWORD");

	public static Connection getConnessione() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(url, username, password);
			return connection;
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver JDBC non trovato.");
			e.printStackTrace();
			return null;
			
		} catch (SQLException e) {
			System.out.println("Errore nella connessione al database.");
			e.printStackTrace();
			return null;
		}

	}

}