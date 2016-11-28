package com.wfe.inventorySystem;

import java.util.ArrayList;
import java.util.List;

import com.wfe.core.ResourceManager;

public class ItemDatabase {

	public List<Item> items = new ArrayList<Item>();
	
	public ItemDatabase() {
		items.add(new Item("Flint", 0, "Heavy flint", 
				ResourceManager.getTexture("flint_ui"), 0, 0, Item.ItemType.Quest));
		items.add(new Item("Apple", 1, "Sweet apple is so sweet", 
				ResourceManager.getTexture("apple_ui"), 0, 0, Item.ItemType.Consumable));
	}

}
