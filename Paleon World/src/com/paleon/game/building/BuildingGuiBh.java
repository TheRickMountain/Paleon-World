package com.paleon.game.building;

import java.util.ArrayList;
import java.util.List;

import com.paleon.engine.Display;
import com.paleon.engine.ResourceManager;
import com.paleon.engine.behaviour.Behaviour;
import com.paleon.engine.components.Image;
import com.paleon.engine.graph.gui.Button;
import com.paleon.engine.input.Key;
import com.paleon.engine.input.Keyboard;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.toolbox.Color;
import com.paleon.engine.toolbox.MousePicker;

public class BuildingGuiBh extends Behaviour {

	Button buildingButton;
	
	World world;
	
	boolean show = false;
	
	public Entity background;
	
	private List<Button> buildingList;
	
	private Entity currentHouse;
	
	public BuildingGuiBh(World world) {
		this.world = world;
	}
	
	@Override
	public void start() {
		buildingList = new ArrayList<Button>();
		
		buildingButton = new Button(80, 80, ResourceManager.getTexture("ui_building"), world);
		buildingButton.position.set(Display.getWidth() - 80, Display.getHeight() - 80);
		world.addEntity(buildingButton);
		
		background = new Entity(world);
		background.addComponent(new Image(ResourceManager.getTexture("ui_slot"), new Color(1f, 1f, 1f)));
		background.position.x = Display.getWidth() / 2 - 200;
		background.position.y = Display.getHeight() / 2 - 150;
		background.scale.x = 400;
		background.scale.y = 300;
		background.getComponent(Image.class).enabled = false;
		world.addEntity(background);
	}

	public void updateOwn() {
		if(show) {
			addBuilding("House", "house_icon", 0, 0);
			addBuilding("Barn", "barn_icon", 1, 0);
			addBuilding("Forester", "forester_icon", 2, 0);
			addBuilding("Fishing Dock", "fishingDock_icon", 3, 0);
			
			addBuilding("Woodcutter", "woodcutter_icon", 0, 1);
			
			for(Button button : buildingList) {
				world.addEntity(button);
			}
		} else {
			for(Button b : buildingList) {
				b.remove();
			}
			buildingList.clear();
		}
	}
	
	public void addBuilding(String name, String iconName, int x, int y) {
		Button button = new Button(95, 95, ResourceManager.getTexture(iconName), world);
		button.name = name;
		button.position.x = background.position.x + 10 + x * button.getWidth(); 
		button.position.y = background.position.y + 10 + y * button.getHeight();
		buildingList.add(button);
	}
	
	@Override
	public void update(float dt) {
		if(buildingButton.isPressedDown(0)) {
			
			show = !show;
			
			if(show)
				show();
			else
				hide();
		}
		
		if(show) {
			for(Button button : buildingList) {
				if(button.isPressedDown(0)){
					switch(button.name) {
					case "House":
						currentHouse = new House(world);
						break;
					case "Barn":
						currentHouse = new Barn(world);
						break;
					case "Forester":
						currentHouse = new Forester(world);
						break;
					case "Fishing Dock":
						currentHouse = new FishingDock(world);
						break;
					case "Woodcutter":
						currentHouse = new Woodcutter(world);
						break;
					}
					
					world.addEntity(currentHouse);
					hide();
					return;
				}
			}
		}
		
		if(currentHouse != null) {
			currentHouse.position.set(MousePicker.getCurrentTerrainPoint());
			
			if(Keyboard.isKey(Key.Q)) {
				currentHouse.rotation.y -= 100 * dt;
			} else if(Keyboard.isKey(Key.E)) {
				currentHouse.rotation.y += 100 * dt;
			}
			
			if(Mouse.isButtonDown(1)) {
				currentHouse = null;
			}
		}
	}

	@Override
	public void onGUI() {
		
	}
	
	public void show() {	
		show = true;
		background.getComponent(Image.class).enabled = show;
		updateOwn();
	}
	
	public void hide() {
		show = false;
		background.getComponent(Image.class).enabled = show;
		updateOwn();
	}

}
