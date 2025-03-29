package edu.icet.ecom.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Getter
@Component
@RequiredArgsConstructor
public class CrudUtil {
	private final DBConnection dbConnection;

	@SuppressWarnings({ "unchecked", "resource" })
	public <T> T execute (final String query, Object ...binds) throws SQLException {
		final PreparedStatement preparedStatement = this.dbConnection.getConnection().prepareStatement(query);
		final int dataLength = binds.length;

		for (int a = 0; a < dataLength; a++) {
			final Object data = binds[a];

			if (data == null) {
				preparedStatement.setNull(a + 1, Types.NULL);
			} else {
				preparedStatement.setObject(a + 1, data);
			}
		}

		if (query.matches("(?i)^select.*")) return (T) preparedStatement.executeQuery();

		return (T) ((Integer) preparedStatement.executeUpdate());
	}

	@SuppressWarnings({ "unchecked", "resource" })
	public <T, U> T execute (final String query, List<U> binds) throws SQLException {
		final PreparedStatement preparedStatement = this.dbConnection.getConnection().prepareStatement(query);
		final int dataLength = binds.size();

		for (int a = 0; a < dataLength; a++) {
			final Object data = binds.get(a);

			if (data == null) {
				preparedStatement.setNull(a + 1, Types.NULL);
			} else {
				preparedStatement.setObject(a + 1, data);
			}
		}

		if (query.matches("(?i)^select.*")) return (T) preparedStatement.executeQuery();

		return (T) ((Integer) preparedStatement.executeUpdate());
	}

	@SuppressWarnings("resource")
	public long executeWithGeneratedKeys (String query, Object... binds) throws SQLException {
		final PreparedStatement preparedStatement = this.dbConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		final int dataLength = binds.length;

		for (int a = 0; a < dataLength; a++) {
			final Object data = binds[a];

			if (data == null) {
				preparedStatement.setNull(a + 1, Types.NULL);
			} else {
				preparedStatement.setObject(a + 1, data);
			}
		}

		final int affectedRows = preparedStatement.executeUpdate();

		if (affectedRows == 0) throw new SQLException("No rows affected.");

		final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

		if (!generatedKeys.next()) throw new SQLException("No ID obtained.");

		return generatedKeys.getLong(1);
	}
}
