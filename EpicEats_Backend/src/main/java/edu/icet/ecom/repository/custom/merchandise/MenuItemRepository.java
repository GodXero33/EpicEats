package edu.icet.ecom.repository.custom.merchandise;

import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface MenuItemRepository extends CrudRepository<MenuItemEntity> {
	Response<Boolean> isExist (Long id);
	Response<Boolean> isAllMenuItemsExist (List<Long> ids);
	Response<List<MenuItemEntity>> getAllByIDs (List<Long> ids);
}
