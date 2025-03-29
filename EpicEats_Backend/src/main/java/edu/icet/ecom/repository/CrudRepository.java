package edu.icet.ecom.repository;

import edu.icet.ecom.util.Response;

import java.util.List;

public interface CrudRepository<T> {
	Response<T> add (T entity);
	Response<T> update (T entity);
	Response<Object> delete (Long id);
	Response<T> get (Long id);
	Response<List<T>> getAll ();
}
