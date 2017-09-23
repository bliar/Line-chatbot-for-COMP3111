package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		//Write your code here
		//return null;
		String result = null;
		Connection connection = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(
					"SELECT response FROM comp3111lab3 where keyword = ?");
			stmt.setString(1, text);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			log.info("Exception while reading data: {}", e.toString());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (connection != null)
					connection.close();
			} catch (Exception ex) {
				log.info("Exception while closing connection: {}", ex.toString());
			}
		}
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");	
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}
