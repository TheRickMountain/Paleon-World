package com.wfe.inventorySystem;

import java.util.ArrayList;
import java.util.List;

import com.wfe.behaviours.Behaviour;
import com.wfe.components.Text;
import com.wfe.core.Display;
import com.wfe.core.ResourceManager;
import com.wfe.graph.Texture;
import com.wfe.graph.render.GUIRenderer;
import com.wfe.input.Key;
import com.wfe.input.Keyboard;
import com.wfe.input.Mouse;
import com.wfe.scenes.Game;
import com.wfe.utils.Color;
import com.wfe.utils.Rect;

public class Inventory extends Behaviour {

	public int slotsX = 4, slotsY = 5;
	public List<Item> inventory = new ArrayList<Item>();
	public List<Item> slots = new ArrayList<Item>();
	private boolean showInventory;
	private boolean showTooltip;
	
	private Text tooltipText;
	private String tooltip;
	private Text foodText;
	private String food;
	private Text weightText;
	private String weight;
	
	private Texture slotSkin = ResourceManager.getTexture("ui_slot");
	
	private boolean draggingItem;
	private Text draggedItemCount;
	
	private Item draggedItem;
	private int draggedIndex;
	
	public boolean action = false;
	
	private Rect slotsRect;
	
	private Text stackText;
	
	@Override
	public void start() {	
		ItemDatabase.init();
		
		for(int i = 0; i < (slotsX * slotsY); i++) 
		{
			slots.add(new Item());
			inventory.add(new Item());
		}
		
		addItem(ItemDatabase.APPLE);
		addItem(ItemDatabase.APPLE);
		addItem(ItemDatabase.FLINT);
		addItem(ItemDatabase.FLINT);
		
		slotsRect = new Rect();
		
		draggedItemCount = new Text("2", GUIRenderer.primitiveFont, 1.25f,
				Color.WHITE);
		
		tooltipText = new Text("test", GUIRenderer.primitiveFont, 1.25f, Color.BLACK);
		foodText = new Text("test", GUIRenderer.primitiveFont, 1f, Color.BLUE);
		weightText = new Text("test", GUIRenderer.primitiveFont, 1f, Color.PURPLE);
		stackText = new Text("test", GUIRenderer.primitiveFont, 1.25f,
				Color.WHITE);
	}
	
	public void updateOwn() {
		if(showInventory) 
		{
			Game.state = Game.State.INVENTORY;
		} 
		else 
		{
			Game.state = Game.State.GAME;
			showTooltip = false;
		}
	}
	
	@Override
	public void update(float deltaTime) {
		if(Keyboard.isKeyDown(Key.TAB)) {
			showInventory = !showInventory;
			
			updateOwn();
		}
	}

	@Override
	public void onGUI() {
		tooltip = "";
		if(showInventory) 
		{
			action = false;
			GUIRenderer.render(new Rect(Display.getWidth() / 2, Display.getHeight() / 3 - 10, 250, 310), slotSkin);
			drawInventory();
			if(showTooltip) 
			{
				GUIRenderer.render(Mouse.getX() + 15f, Mouse.getY(), 200, 200, slotSkin);
				tooltipText.setText(tooltip);
				GUIRenderer.render(Mouse.getX() + 25f, Mouse.getY() + 10, tooltipText);
				foodText.setText(food);
				GUIRenderer.render(Mouse.getX() + 25f, Mouse.getY() + 50, foodText);
				weightText.setText(weight);
				GUIRenderer.render(Mouse.getX() + 25f, Mouse.getY() + 70, weightText);
			}
		}
		
		if(draggingItem)
		{
			GUIRenderer.render(Mouse.getX() - 25f, Mouse.getY() - 25f, 50, 50, draggedItem.itemIcon);
			draggedItemCount.setText(String.valueOf(draggedItem.stack));
			GUIRenderer.render(Mouse.getX() - 25f, Mouse.getY() - 25f, draggedItemCount);
		}
	}
	
	private void drawInventory() 
	{	
		int i = 0;
		for(int y = 0; y < slotsY; y++) 
		{
			for(int x = 0; x < slotsX; x++) 
			{
				slotsRect.x = (Display.getWidth() / 2) + (x * 60 + 10);
				slotsRect.y = (Display.getHeight() / 3)  + (y * 60);
				slotsRect.width = 50;
				slotsRect.height = 50;
				
				GUIRenderer.render(slotsRect, slotSkin);
				slots.set(i, inventory.get(i));
				Item item = slots.get(i);
				if(slots.get(i).itemName != null) 
				{
					GUIRenderer.render(slotsRect, slots.get(i).itemIcon);
					
					stackText.setText("" + slots.get(i).stack);
					GUIRenderer.render(slotsRect, stackText);
					if(Mouse.getX() >= slotsRect.x && Mouse.getX() <= slotsRect.x + slotsRect.width &&
							Mouse.getY() >= slotsRect.y && Mouse.getY() <= slotsRect.y + slotsRect.height) 
					{
						generateTooltip(slots.get(i));
						showTooltip = true;
						if(Mouse.isButton(0) && !draggingItem)
						{
							draggingItem = true;
							draggedItem = item;
							inventory.set(i, new Item());
							draggedIndex = i;
							action = true;
						}
						if(Mouse.isButtonUp(0) && draggingItem)
						{
							inventory.set(draggedIndex, inventory.get(i));
							inventory.set(i, draggedItem);
							draggingItem = false;
							draggedItem = null;
							action = true;
						}
						if(Mouse.isButtonDown(1) && !draggingItem)
						{
							if(item.itemType == Item.ItemType.CONSUMABLE)
							{
								//useConsumable(slots.get(i), i);
								action = true;
							}
						}
					}
				} 
				else 
				{
					if(Mouse.getX() >= slotsRect.x && Mouse.getX() <= slotsRect.x + slotsRect.width &&
							Mouse.getY() >= slotsRect.y && Mouse.getY() <= slotsRect.y + slotsRect.height) 
					{
						if(Mouse.isButtonUp(0) && draggingItem)
						{
							inventory.set(i, draggedItem);
							draggingItem = false;
							draggedItem = null;
							action = true;
						}
					}
				}
				
				if(tooltip == "")
				{
					showTooltip = false;
				}
				
				i++;
			}
		}
	}
	
	public boolean addItem(int itemID) 
	{
		for(int i = 0; i < ItemDatabase.items.size(); i++) 
		{
			if(itemID == i)
			{
				for(int j = 0; j < slots.size(); j++)
				{
					if(inventory.get(j).itemID == itemID) {
						inventory.get(j).stack++;
						return true;
					}
				}
				
				for(int j = 0; j < slots.size(); j++)
				{
					if(inventory.get(j).itemName == null) {	
						inventory.set(j, ItemDatabase.items.get(i));
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void removeItem(int itemID) 
	{
		for(int i = 0; i < inventory.size(); i++) 
		{
			if(inventory.get(i).itemID == itemID) 
			{
				if(inventory.get(i).stack >= 2){
					inventory.get(i).stack--;
					action = true;
				} else {
					inventory.set(i, new Item());
					action = true;
				}
				break;
			}
		}
	}
	
	private void generateTooltip(Item item) 
	{
		tooltip = item.itemName;
		food = "Food: " + item.itemFood;
		weight = "Weight: " + item.itemWeight;
	}
	
	/*private void useConsumable(Item item, int slot)
	{
		boolean deleteItem = false;
		boolean decrease = false;
		switch(item.itemID)
		{
		case ItemDatabase.SHROOM:
			if(Game.gui.hungerBar.getCurrentValue() != Game.gui.hungerBar.getMaxValue()) 
			{
				Game.gui.hungerBar.increase(item.itemFood);
				if(item.stack >= 2) {
					decrease = true;
				} else {
					deleteItem = true;
				}
			}
			break;
		case ItemDatabase.APPLE:
			if(Game.gui.hungerBar.getCurrentValue() != Game.gui.hungerBar.getMaxValue()) 
			{
				Game.gui.hungerBar.increase(item.itemFood);
				if(item.stack >= 2) {
					decrease = true;
				} else {
					deleteItem = true;
				}
			}
			break;
		}
		
		if(deleteItem) {
			inventory.set(slot, new Item());
		} else if(decrease) {
			item.stack--;
		}
		action = true;
	}*/
	
	public int inventoryContainsCount(int id){
		int count = 0;
		
		for(Item item : inventory) 
		{
			if(item.itemID == id) {
				count += item.stack;
			}
		}
		
		return count;
	}
	
	public boolean inventoryContains(int id) 
	{
		for(Item item : inventory) 
		{
			if(item.itemID == id) return true;
		}
		return false;
	}
	
	/*public void hide() {
		if(!showInventory)
			return;
		
		showInventory = false;
		updateOwn();
	}
	
	public void show() {
		if(showInventory)
			return;
		
		showInventory = true;
		updateOwn();
	}*/
	
}
