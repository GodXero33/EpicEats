package edu.icet.ecom.service;

import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@RequiredArgsConstructor
public class SuperServiceHandler<D, E> implements SuperService<D> {
	private final CrudRepository<E> repository;
	private final ModelMapper mapper;
	private final Class<D> dtoClass;
	private final Class<E> entityClass;

	@Override
	public Response<D> get (Long id) {
		final Response<E> response = this.repository.get(id);

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			this.mapper.map(response.getData(), this.dtoClass) :
			null
			, response.getStatus());
	}

	@Override
	public Response<List<D>> getAll () {
		final Response<List<E>> response = this.repository.getAll();

		return new Response<>(response.getStatus() == ResponseType.FOUND ?
			response.getData().stream().map(entity -> this.mapper.map(entity, this.dtoClass)).toList() :
			null
		, response.getStatus());
	}

	@Override
	public Response<D> add (D dto) {
		final Response<E> response = this.repository.add(this.mapper.map(dto, this.entityClass));

		return new Response<>(response.getStatus() == ResponseType.CREATED ?
			this.mapper.map(response.getData(), this.dtoClass) :
			null
		, response.getStatus());
	}

	@Override
	public Response<D> update (D dto) {
		final Response<E> response = this.repository.update(this.mapper.map(dto, this.entityClass));

		return new Response<>(response.getStatus() == ResponseType.UPDATED ?
			this.mapper.map(response.getData(), this.dtoClass) :
			null
			, response.getStatus());
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.repository.delete(id);
	}
}
