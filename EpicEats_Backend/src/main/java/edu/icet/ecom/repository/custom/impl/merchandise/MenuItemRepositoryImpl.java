package edu.icet.ecom.repository.custom.impl.merchandise;

import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public class MenuItemRepositoryImpl implements MenuItemRepository {
	@Override
	public Response<MenuItemEntity> add (MenuItemEntity entity) {
		return null;
	}

	@Override
	public Response<MenuItemEntity> update (MenuItemEntity entity) {
		return null;
	}

	@Override
	public Response<Boolean> delete (Long id) {
		return null;
	}

	@Override
	public Response<MenuItemEntity> get (Long id) {
		return null;
	}

	@Override
	public Response<List<MenuItemEntity>> getAll () {
		return null;
	}
}
