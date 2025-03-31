package edu.icet.ecom.repository;

import edu.icet.ecom.util.Response;

import java.util.List;

public interface FullDataRepository<E> extends CrudRepository<E> {
	Response<E> getFull (Long id);
	Response<List<E>> getAllFull ();
}
