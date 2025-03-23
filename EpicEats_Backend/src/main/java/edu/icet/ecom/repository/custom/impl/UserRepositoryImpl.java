package edu.icet.ecom.repository.custom.impl;

import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.repository.custom.UserRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.ResponseType;
import edu.icet.ecom.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	private LocalDateTime parseLocalDateTime (String str, DateTimeFormatter formatter) {
		return str == null || str.isEmpty() ? null : LocalDateTime.parse(str, formatter);
	}

	@Override
	public Response<UserEntity> getByUserName (String username) {
		try {
			final ResultSet resultSet = this.crudUtil.execute("SELECT employee_id, password, created_at, last_login, role FROM `user` WHERE is_deleted = FALSE AND username = ?", username);
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			return resultSet.next() ?
				new Response<>(UserEntity.builder()
					.employeeId(resultSet.getLong(1))
					.username(username)
					.password(resultSet.getString(2))
					.createdAt(this.parseLocalDateTime(resultSet.getString(3), formatter))
					.lastLogin(this.parseLocalDateTime(resultSet.getString(4), formatter))
					.role(UserRole.fromName(resultSet.getString(5)))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (Exception exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> deleteByUserName (String name) {
		return null;
	}

	@Override
	public Response<Boolean> isExistsByFieldName (String fieldName, Object value) {
		return null;
	}

	@Override
	public Response<Boolean> isUsernameExist (String username) {
		try {
			return ((ResultSet) this.crudUtil.execute("SELECT 1 FROM `user` WHERE is_deleted = FALSE AND username = ?", username)).next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isEmployeeExistById (Long employeeId) {
		try {
			return ((ResultSet) this.crudUtil.execute("SELECT 1 FROM employee WHERE is_deleted = FALSE AND id = ?", employeeId)).next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isEmployeeAlreadyUser (Long employeeId) {
		try {
			return ((ResultSet) this.crudUtil.execute("SELECT 1 FROM `user` WHERE is_deleted = FALSE AND employee_id = ?", employeeId)).next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<UserEntity> add (UserEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"INSERT INTO `user` (employee_id, username, password, role) VALUES (?, ?, ?, ?)",
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
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<UserEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<UserEntity>> getAll () {
		return null;
	}
}
