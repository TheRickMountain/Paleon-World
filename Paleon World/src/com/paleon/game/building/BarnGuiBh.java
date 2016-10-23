package com.paleon.game.building;

import java.util.ArrayList;
import java.util.List;

import com.paleon.engine.Display;
import com.paleon.engine.ResourceManager;
import com.paleon.engine.behaviour.Behaviour;
import com.paleon.engine.components.Text;
import com.paleon.engine.graph.Texture2D;
import com.paleon.engine.graph.gui.Button;
import com.paleon.engine.graph.gui.Item;
import com.paleon.engine.graph.gui.ItemDatabase;
import com.paleon.engine.graph.systems.GUIRendererSystem;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.toolbox.Color;
import com.paleon.engine.toolbox.Rect;
import com.paleon.scenes.Game;

public class BarnGuiBh extends Behaviour {

	public int slotsX = 4, slotsY = 3;
	public List<Item> inventory = new ArrayList<Item>();
	public List<Item> slots = new ArrayList<Item>();
	private boolean showInventory;
	private Rect slotsRect;
	
	private Texture2D slotSkin = ResourceManager.getTexture("ui_slot");
	private Texture2D closeSkin = ResourceManager.getTexture("ui_close");
	
	private Text stackText;
	
	private Button close;
	
	@Override
	public void start() {
		
		for(int i = 0; i < (slotsX * slotsY); i++) 
		{
			slots.add(new Item());
			inventory.add(new Item());
		}
		
		slotsRect = new Rect();
		
		stackText = new Text("test", GUIRendererSystem.primitiveFont, 1.25f,
				Color.WHITE, 1f, false);
		
		close = new Button(20, 20, closeSkin, parent.getWorld());
		close.position.x = Display.getWidth() / 2 + 110;
		close.position.y = Display.getHeight() / 2 - 100;
		close.deactivate();
		parent.getWorld().addEntity(close);
	}
	
	@Override
	public void update(float deltaTime) {
		if(Mouse.isButtonDown(0)) {
			if(parent.getID() == parent.getWorld().COLOR_PICKING_ID) {
				show();
			}
		}
		
		if(showInventory) {
			if(close.isPressedDown(0)) {
				hide();
			}
		}
	}

	@Override
	public void onGUI() {
		if(showInventory) {
			GUIRendererSystem.render(new Rect(Display.getWidth() / 2 - 110, Display.getHeight() / 2 - 80, 220, 160), slotSkin);
			drawInventory();
		}
	}
	
	private void drawInventory() 
	{	
		int i = 0;
		for(int y = 0; y < slotsY; y++) 
		{
			for(int x = 0; x < slotsX; x++) 
			{
				slotsRect.x = (Display.getWidth() / 2 - 110) + (x * 50 + 10);
				slotsRect.y = (Display.getHeight() / 2 - 80)  + (y * 50 + 5);
				slotsRect.width = 50;
				slotsRect.height = 50;
				
				GUIRendererSystem.render(slotsRect, slotSkin);
				slots.set(i, inventory.get(i));
				if(slots.get(i).itemName != null) 
				{
					GUIRendererSystem.render(slotsRect, slots.get(i).itemIcon);
					
					stackText.setText("" + slots.get(i).stack);
					GUIRendererSystem.render(slotsRect, stackText);
				} 
				
				i++;
			}
		}
	}
	
	public void removeItem(int itemID) 
	{
		for(int i = 0; i < inventory.size(); i++) 
		{
			if(inventory.get(i).itemID == itemID) 
			{
				if(inventory.get(i).stack >= 2){
					inventory.get(i).stack--;
				} else {
					inventory.set(i, new Item());
				}
				return;
			}
		}
	}
	
	public int getItemCount(int itemID) {
		int count = 0;
		for(Item item : inventory) {
			if(item.itemID == itemID) {
				count += item.stack;
			}
		}
		
		return count;
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
	
	public void hide() {
		if(!showInventory)
			return;
		
		close.deactivate();
		showInventory = false;
		Game.state = Game.State.GAME;
	}
	
	public void show() {
		if(showInventory)
			return;
		
		close.activate();
		showInventory = true;
		Game.state = Game.State.INVENTORY;
	}

}
