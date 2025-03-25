package edu.icet.ecom.repository.custom.impl.finance;

import edu.icet.ecom.entity.finance.ExpenseEntity;
import edu.icet.ecom.repository.custom.finance.ExpenseRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class ExpenseRepositoryImpl implements ExpenseRepository {
	@Override
	public Response<ExpenseEntity> add (ExpenseEntity entity) {
		return null;
	}

	@Override
	public Response<ExpenseEntity> update (ExpenseEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<ExpenseEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<ExpenseEntity>> getAll () {
		return null;
	}
}
