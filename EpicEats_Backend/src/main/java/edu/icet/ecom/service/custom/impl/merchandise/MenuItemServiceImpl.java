package edu.icet.ecom.service.custom.impl.merchandise;

import edu.icet.ecom.dto.merchandise.MenuItem;
import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import edu.icet.ecom.service.custom.merchandise.MenuItemService;
import edu.icet.ecom.util.Response;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class MenuItemServiceImpl implements MenuItemService {
	private final SuperServiceHandler<MenuItem, MenuItemEntity> serviceHandler;
	private final MenuItemRepository menuItemRepository;

	public MenuItemServiceImpl (MenuItemRepository menuItemRepository, ModelMapper mapper) {
		this.serviceHandler = new SuperServiceHandler<>(menuItemRepository, mapper, MenuItem.class, MenuItemEntity.class);
		this.menuItemRepository = menuItemRepository;
	}

	@Override
	public Response<MenuItem> get (Long id) {
		return this.serviceHandler.get(id);
	}

	@Override
	public Response<List<MenuItem>> getAll () {
		return this.serviceHandler.getAll();
	}

	@Override
	public Response<MenuItem> add (MenuItem dto) {
		return this.serviceHandler.add(dto);
	}

	@Override
	public Response<MenuItem> update (MenuItem dto) {
		return this.serviceHandler.update(dto);
	}

	@Override
	public Response<Object> delete (Long id) {
		return this.serviceHandler.delete(id);
	}

	@Override
	public Response<Boolean> isAllMenuItemsExist (List<Long> ids) {
		return this.menuItemRepository.isAllMenuItemsExist(ids);
	}
}
