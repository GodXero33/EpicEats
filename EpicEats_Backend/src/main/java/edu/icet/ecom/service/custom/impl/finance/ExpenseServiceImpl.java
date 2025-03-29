package edu.icet.ecom.service.custom.impl.finance;

import edu.icet.ecom.dto.finance.Expense;
import edu.icet.ecom.entity.finance.ExpenseEntity;
import edu.icet.ecom.repository.custom.finance.ExpenseRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.finance.ExpenseService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ExpenseServiceImpl implements ExpenseService {
	private final SuperServiceHandler<Expense, ExpenseEntity> serviceHandler;

	public ExpenseServiceImpl (ExpenseRepository expenseRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(expenseRepository, mapper, Expense.class, ExpenseEntity.class);
	}

	@Override
	public Response<Expense> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<Expense>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<Expense> add (Expense dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<Expense> update (Expense dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}
}
