package com.example.common.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TransactionManager {

	// HSQLDBの場合
	//	private final static String DRIVER_NAME = "org.hsqldb.jdbcDriver";
	//	private final static String JDBC_URL = "jdbc:hsqldb:hsql://localhost/";

	// MySQLの場合
	private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
	private final static String JDBC_URL = "jdbc:mysql://localhost:3306/warehouse?useSSL=false";
	//private final static String JDBC_USER = "sa";
	//private final static String JDBC_PASSWORD = "";

	private final static String JDBC_USER = "root";
	private final static String JDBC_PASSWORD = "root";

	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER_NAME);
		Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
		return connection;
	}

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection connection = createConnection();
		connection.setAutoCommit(false);
		return connection;
	}

	public static Connection getReadOnlyConnection() throws ClassNotFoundException, SQLException {
		Connection connection = createConnection();
		connection.setReadOnly(true);
		return connection;
	}

	public static void commit(Connection connection) {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollback(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
