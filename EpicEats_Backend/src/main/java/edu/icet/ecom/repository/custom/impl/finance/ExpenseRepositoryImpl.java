package edu.icet.ecom.repository.custom.impl.finance;

import edu.icet.ecom.entity.finance.ExpenseEntity;
import edu.icet.ecom.repository.custom.finance.ExpenseRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.DateTimeUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ExpenseType;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<ExpenseEntity> add (ExpenseEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"INSERT INTO expense (expense_type, amount, expense_date, description) VALUES (?, ?, ?, ?)",
				entity.getExpenseType().name(),
				entity.getAmount(),
				entity.getExpenseDate(),
				entity.getDescription()
			);

			entity.setId(generatedId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<ExpenseEntity> update (ExpenseEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"UPDATE expense SET expense_type = ?, amount = ?, expense_date = ?, description = ? WHERE is_deleted = FALSE AND id = ?",
				entity.getExpenseType().name(),
				entity.getAmount(),
				entity.getExpenseDate(),
				entity.getDescription(),
				entity.getId()
			) == 0 ?
				new Response<>(null, ResponseType.NOT_UPDATED) :
				new Response<>(entity, ResponseType.UPDATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("UPDATE expense SET is_deleted = TRUE WHERE is_deleted = FALSE AND id = ?", id) == 0 ?
				new Response<>(false, ResponseType.NOT_DELETED) :
				new Response<>(true, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<ExpenseEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT expense_type, amount, expense_date, description FROM expense WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(ExpenseEntity.builder()
					.id(id)
					.expenseType(ExpenseType.fromName(resultSet.getString(1)))
					.amount(resultSet.getDouble(2))
					.expenseDate(DateTimeUtil.parseDate(resultSet.getString(3)))
					.description(resultSet.getString(4))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<ExpenseEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT id, expense_type, amount, expense_date, description FROM expense WHERE is_deleted = FALSE")) {
			final List<ExpenseEntity> expenseEntities = new ArrayList<>();

			while (resultSet.next()) expenseEntities.add(ExpenseEntity.builder()
				.id(resultSet.getLong(1))
				.expenseType(ExpenseType.fromName(resultSet.getString(2)))
				.amount(resultSet.getDouble(3))
				.expenseDate(DateTimeUtil.parseDate(resultSet.getString(4)))
				.description(resultSet.getString(5))
				.build());

			return new Response<>(expenseEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
