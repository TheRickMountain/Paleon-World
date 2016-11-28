package com.wfe.inventorySystem;

import java.util.ArrayList;
import java.util.List;

import com.wfe.behaviours.Behaviour;
import com.wfe.core.ResourceManager;
import com.wfe.graph.Texture;
import com.wfe.graph.render.GUIRenderer;

public class Inventory extends Behaviour {

	public List<Item> inventory = new ArrayList<Item>();
	private ItemDatabase database;
	
	private Texture slotTexture;

	@Override
	public void start() {
		database = new ItemDatabase();
		
		System.out.println(inventory.size());
		
		inventory.add(database.items.get(0));
		inventory.add(database.items.get(1));
		inventory.add(database.items.get(0));
		inventory.add(database.items.get(1));
		inventory.add(database.items.get(0));
		inventory.add(database.items.get(1));
		
		System.out.println(inventory.size());
		
		slotTexture = ResourceManager.getTexture("slot_ui");
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void onGUI() {
		for(int i = 0; i < inventory.size(); i++)
			GUIRenderer.render(slotTexture, 10, i * 40, 0, 40, 40);
	}
	
}
