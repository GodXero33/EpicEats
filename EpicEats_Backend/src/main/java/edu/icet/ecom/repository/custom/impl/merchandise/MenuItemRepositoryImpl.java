package edu.icet.ecom.repository.custom.impl.merchandise;

import edu.icet.ecom.entity.merchandise.MenuItemEntity;
import edu.icet.ecom.repository.custom.merchandise.MenuItemRepository;
import edu.icet.ecom.util.CrudUtil;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.MenuItemCategory;
import edu.icet.ecom.util.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuItemRepositoryImpl implements MenuItemRepository {
	private final Logger logger;
	private final CrudUtil crudUtil;

	@Override
	public Response<MenuItemEntity> add (MenuItemEntity entity) {
		try {
			final long generatedId = this.crudUtil.executeWithGeneratedKeys(
				"""
				INSERT INTO menu_item (name, price, img, category, quantity)
				VALUES (?, ?, ?, ?, ?)
				""",
				entity.getName(),
				entity.getPrice(),
				entity.getImg(),
				entity.getCategory().name(),
				entity.getQuantity()
			);

			entity.setId(generatedId);

			return new Response<>(entity, ResponseType.CREATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<MenuItemEntity> update (MenuItemEntity entity) {
		try {
			return (Integer) this.crudUtil.execute(
				"""
				UPDATE menu_item
				SET name = ?, price = ?, img = ?, category = ?, quantity = ?
				WHERE is_deleted = FALSE AND id = ?
				""",
				entity.getName(),
				entity.getPrice(),
				entity.getImg(),
				entity.getCategory().name(),
				entity.getQuantity(),
				entity.getId()
			) == 0 ?
				new Response<>(null, ResponseType.NOT_UPDATED) :
				new Response<>(entity, ResponseType.UPDATED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Object> delete (Long id) {
		try {
			return (Integer) this.crudUtil.execute("""
				UPDATE menu_item
				SET is_deleted = TRUE
				WHERE is_deleted = FALSE AND id = ?
				""", id) == 0 ?
				new Response<>(null, ResponseType.NOT_DELETED) :
				new Response<>(null, ResponseType.DELETED);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<MenuItemEntity> get (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT name, price, img, category, quantity
			FROM menu_item
			WHERE is_deleted = FALSE AND id = ?
			""", id)) {
			return resultSet.next() ?
				new Response<>(MenuItemEntity.builder()
					.id(id)
					.name(resultSet.getString(1))
					.price(resultSet.getDouble(2))
					.img(resultSet.getString(3))
					.category(MenuItemCategory.fromName(resultSet.getString(4)))
					.quantity(resultSet.getInt(5))
					.build(), ResponseType.FOUND) :
				new Response<>(null, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<MenuItemEntity>> getAll () {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, name, price, img, category, quantity
			FROM menu_item
			WHERE is_deleted = FALSE
			""")) {
			final List<MenuItemEntity> menuItemEntities = new ArrayList<>();

			while (resultSet.next()) menuItemEntities.add(MenuItemEntity.builder()
				.id(resultSet.getLong(1))
				.name(resultSet.getString(2))
				.price(resultSet.getDouble(3))
				.img(resultSet.getString(4))
				.category(MenuItemCategory.fromName(resultSet.getString(5)))
				.quantity(resultSet.getInt(6))
				.build());

			return new Response<>(menuItemEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isExist (Long id) {
		try (final ResultSet resultSet = this.crudUtil.execute("SELECT 1 FROM menu_item WHERE is_deleted = FALSE AND id = ?", id)) {
			return resultSet.next() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<Boolean> isAllMenuItemsExist (List<Long> ids) {
		final String query = """
			SELECT COUNT(*)
			FROM menu_item
			WHERE is_deleted = FALSE AND id IN (%s)
			""".formatted(String.join(", ", Collections.nCopies(ids.size(), "?")));

		try (final ResultSet resultSet = this.crudUtil.execute(query, ids.toArray())) {
			resultSet.next();

			return resultSet.getInt(1) == ids.size() ?
				new Response<>(true, ResponseType.FOUND) :
				new Response<>(false, ResponseType.NOT_FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(false, ResponseType.SERVER_ERROR);
		}
	}

	@Override
	public Response<List<MenuItemEntity>> getAllByIDs (List<Long> ids) {
		try (final ResultSet resultSet = this.crudUtil.execute("""
			SELECT id, name, price, img, category, quantity
			FROM menu_item
			WHERE is_deleted = FALSE AND id IN (%s)
			""".formatted(String.join(", ", Collections.nCopies(ids.size(), "?"))),
			ids
		)) {
			final List<MenuItemEntity> menuItemEntities = new ArrayList<>();

			while (resultSet.next()) menuItemEntities.add(MenuItemEntity.builder()
				.id(resultSet.getLong(1))
				.name(resultSet.getString(2))
				.price(resultSet.getDouble(3))
				.img(resultSet.getString(4))
				.category(MenuItemCategory.fromName(resultSet.getString(5)))
				.quantity(resultSet.getInt(6))
				.build());

			return new Response<>(menuItemEntities, ResponseType.FOUND);
		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			return new Response<>(null, ResponseType.SERVER_ERROR);
		}
	}
}
