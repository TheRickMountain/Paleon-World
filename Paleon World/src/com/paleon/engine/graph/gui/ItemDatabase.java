package com.paleon.engine.graph.gui;

import java.util.ArrayList;
import java.util.List;

public class ItemDatabase {

	public static final int SHROOM = 1;
	public static final int STICK = 2;
	public static final int WHEAT = 3;
	public static final int FLINT = 4;
	public static final int SHARP_FLINT = 5;
	public static final int APPLE = 6;
	public static final int FLOUR = 7;
	public static final int ROPE = 8;
	public static final int BOW = 9;
	public static final int STONE_AXE = 10;
	public static final int LEATHER = 11;
	public static final int FISH = 12;
	public static final int LOGS = 13;
	public static final int FIREWOOD = 14;
	
	public static List<Item> items = new ArrayList<Item>();
	
	public static void init() {
		items.add(new Item("", 0, 0));
		// Items
		items.add(new Item("shroom", SHROOM, 1));
		items.add(new Item("stick", STICK, 1));
		items.add(new Item("wheat", WHEAT, 1));
		items.add(new Item("flint", FLINT, 1));
		items.add(new Item("sharp stone", SHARP_FLINT, 1));
		items.add(new Item("apple", APPLE, 1));
		items.add(new Item("flour", FLOUR, 1));
		items.add(new Item("rope", ROPE, 1));
		items.add(new Item("bow", BOW, 1));
		items.add(new Item("stone axe", STONE_AXE, 1));
		items.add(new Item("leather", LEATHER, 1));
		items.add(new Item("fish", FISH, 1));
		items.add(new Item("logs", LOGS, 1));
		items.add(new Item("firewood", FIREWOOD, 1));
	}
	
}
