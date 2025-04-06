package edu.icet.ecom.repository.custom.impl.security;

import edu.icet.ecom.entity.security.UserEntity;
import edu.icet.ecom.repository.custom.security.UserRepository;
import edu.icet.ecom.util.*;
import edu.icet.ecom.util.enumaration.ResponseType;
import edu.icet.ecom.util.enumaration.UserRole;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	private Response<UserEntity> getByFieldName (String fieldName, Object identifier) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT employee_id, username, password, created_at, updated_at, last_login, role
			FROM `user`
			WHERE is_deleted = FALSE AND %s = ?
			""".formatted(fieldName), identifier)) {
			return resultSet.next() ?
				new Response<>(UserEntity.builder()
					.employeeId(resultSet.getLong(1))
					.username(resultSet.getString(2))
					.password(resultSet.getString(3))
					.createdAt(DateTimeUtil.parseDateTime(resultSet.getString(4)))
					.updatedAt(DateTimeUtil.parseDateTime(resultSet.getString(5)))
					.lastLogin(DateTimeUtil.parseDateTime(resultSet.getString(6)))
					.role(UserRole.fromName(resultSet.getString(7)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (Exception exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	private Response<Boolean> isExistsByFieldName (String fieldName, Object identifier) {
		try {
			return ((ResultSet) this.crudUtil.execute("SELECT 1 FROM `user` WHERE is_deleted = FALSE AND " + fieldName + " = ?", identifier)).next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<UserEntity> getByUserName (String username) {
		return this.getByFieldName("username", username);
	}

	@Override
	public Response<Boolean> isUsernameExist (String username) {
		return this.isExistsByFieldName("username", username);
	}

	@Override
	public Response<Boolean> isEmployeeExistById (Long employeeId) {
		try {
			return ((ResultSet) this.crudUtil.execute("SELECT 1 FROM employee WHERE id = ?", employeeId)).next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isEmployeeAlreadyUser (Long employeeId) {
		return this.isExistsByFieldName("employee_id", employeeId);
	}

	@Override
	public Response<UserEntity> add (UserEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"""
				INSERT INTO `user` (employee_id, username, password, role)
				VALUES (?, ?, ?, ?)
				""",
				entity.getEmployeeId(),
				entity.getUsername(),
				entity.getPassword(),
				entity.getRole().name()
			) == 0 ?
				new Response<>(null, ResponseType.NOT_CREATED) :
				new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<UserEntity> update (UserEntity entity) {
		final Connection connection = this.crudUtil.getDbConnection().getConnection();

		if (connection == null) return new Response<>(null, ResponseType.SERVER_ERROR);

		try {
			connection.setAutoCommit(false);

			final String password = entity.getPassword();
			final boolean isUpdated = (password == null ?
				(Integer) this.crudUtil.execute(
					"""
					UPDATE `user`
					SET updated_at = ?, role = ?
					WHERE is_deleted = FALSE AND employee_id = ?
					""",
					DateTimeUtil.getCurrentDateTime(),
					entity.getRole().name(),
					entity.getEmployeeId()) :
				(Integer) this.crudUtil.execute(
					"""
					UPDATE `user`
					SET password = ?, updated_at = ?, role = ?
					WHERE is_deleted = FALSE AND employee_id = ?
					""",
					password,
					DateTimeUtil.getCurrentDateTime(),
					entity.getRole().name(),
					entity.getEmployeeId())
			) != 0;

			if (!isUpdated) {
				connection.rollback();
				return new Response<>(null, ResponseType.NOT_UPDATED);
			}

			try (final ResultSet userNameResultSet = this.crudUtil.execute("""
				SELECT username FROM `user`
				WHERE employee_id = ?
				""", entity.getEmployeeId())) {
				if (userNameResultSet.next()) {
					connection.commit();
					entity.setUsername(userNameResultSet.getString(1));
					return new Response<>(entity, ResponseType.UPDATED);
				}
			}

			connection.rollback();
			return new Response<>(null, ResponseType.NOT_UPDATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());

			try {
				connection.rollback();
			} catch (SQLException rollbackException) {
				this.logger.error(rollbackException.getMessage());
			}

			return new Response<>(null, ResponseType.SERVER_ERROR);
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException exception) {
				this.logger.error(exception.getMessage());
			}
		}
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute(
				"""
				UPDATE `user`
				SET deleted_at = ?, is_deleted = TRUE
				WHERE is_deleted = FALSE AND employee_id = ?
				""",
				DateTimeUtil.getCurrentDateTime(),
				id
			) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<UserEntity> get (Long id) {
		return this.getByFieldName("employee_id", id);
	}

	@Override
	public Response<List<UserEntity>> getAll () {
		return new Response<>(null, ResponseType.NOT_FOUND);
	}
}
