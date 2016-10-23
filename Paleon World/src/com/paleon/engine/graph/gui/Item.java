package com.paleon.engine.graph.gui;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.graph.Texture2D;

public class Item {

	public String itemName;
	public int itemID;
	public String itemDesc;
	public Texture2D itemIcon;
	public int stack;
	
	public enum ItemType {
		WEAPON,
		CONSUMABLE,
		ITEM
	}
	
	public Item() {
		
	}

	public Item(String itemName, int itemId, int stackability) {
		this.itemName = itemName;
		this.itemID = itemId;
		if(!itemName.isEmpty()) {
			this.itemIcon = ResourceManager.getTexture("ui_" + itemName);
		}
		this.stack = stackability;
	}
	
}
