package com.wfe.inventorySystem;

import java.io.Serializable;

import com.wfe.graph.Texture;

public class Item implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String itemName;
	public int itemID;
	public String itemDesc;
	public Texture itemIcon;
	public int itemPower;
	public int itemSpeed;
	public ItemType itemType;
	
	public enum ItemType {
		Weapon,
		Consumable,
		Quest
	}
	
	public Item(String itemName, int itemID, String itemDesc, Texture itemIcon, int itemPower, int itemSpeed,
			ItemType itemType) {
		this.itemName = itemName;
		this.itemID = itemID;
		this.itemDesc = itemDesc;
		this.itemIcon = itemIcon;
		this.itemPower = itemPower;
		this.itemSpeed = itemSpeed;
		this.itemType = itemType;
	}

}
