package edu.icet.ecom.util;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
@Component
public class DBConnection {
	private final Connection connection;

	public DBConnection (Environment environment) throws SQLException {
		final String HOST = "localhost";
		final Integer PORT = 3306;
		final String DATABASE = environment.getProperty("spring.db.database");
		final String USERNAME = environment.getProperty("spring.db.username");
		final String PASSWORD = environment.getProperty("spring.db.password");

		this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE), USERNAME, PASSWORD);
	}
}
