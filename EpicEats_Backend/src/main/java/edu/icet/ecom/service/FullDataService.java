package edu.icet.ecom.service;

import edu.icet.ecom.util.Response;

import java.util.List;

public interface FullDataService<D> extends SuperService<D> {
	Response<D> getFull (Long id);
	Response<List<D>> getAllFull ();
}
