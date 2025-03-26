package edu.icet.ecom.util.enumaration;

import java.util.Arrays;

public enum MenuItemCategory {
	BURGER, PIZZA, BEVERAGE, SIDES, DESSERT, SPECIALS;

	public static MenuItemCategory fromName (String name) {
		return name == null ? null : Arrays.stream(MenuItemCategory.values()).
			filter(menuItemCategory -> menuItemCategory.name().equalsIgnoreCase(name)).
			findFirst().
			orElse(null);
	}
}
