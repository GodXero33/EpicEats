package edu.icet.ecom.service.custom.merchandise;

import edu.icet.ecom.dto.merchandise.MenuItem;
import edu.icet.ecom.service.SuperService;
import edu.icet.ecom.util.Response;

import java.util.List;

public interface MenuItemService extends SuperService<MenuItem> {
	Response<Boolean> isExist (Long id);
	Response<Boolean> isAllMenuItemsExist (List<Long> ids);
}
