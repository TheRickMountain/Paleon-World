package com.paleon.engine.graph.gui;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.behaviour.ButtonBh;
import com.paleon.engine.components.Image;
import com.paleon.engine.components.Text;
import com.paleon.engine.graph.Texture;
import com.paleon.engine.graph.systems.GUIRendererSystem;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.toolbox.Color;

public class Button extends Entity {

	private ButtonBh buttonBh;
	private int width, height;
	
	public Button(int width, int height, String text, World world) {
		super(world);
		this.width = width;
		this.height = height;
		Entity background = new Entity(world);
		background.addComponent(new Image(ResourceManager.getTexture("ui_button"), new Color(1f, 1f, 1f)));
		background.localScale.x = width;
		background.localScale.y = height;
		background.name = "Background";
		addChild(background);
		
		if(!text.isEmpty()) {
			Entity elementName = new Entity(world);
			elementName.addComponent(new Text(text, GUIRendererSystem.primitiveFont, 0.9f, new Color(1f, 1f, 1f), 1f, false));
			elementName.localPosition.x = 25;
			elementName.localPosition.y = 5;
			addChild(elementName);
		}
		
		buttonBh = new ButtonBh();
		addBehaviour(buttonBh);
	}
	
	public Button(int width, int height, Texture texture, World world) {
		super(world);
		this.width = width;
		this.height = height;
		addComponent(new Image(texture, new Color(1f, 1f, 1f)));
		this.scale.x = width;
		this.scale.y = height;
		
		buttonBh = new ButtonBh();
		addBehaviour(buttonBh);
	}
	
	public void isOverMouse() {
		buttonBh.isOverMouse();
	}
	
	public boolean isPressedDown(int button) {
		return buttonBh.isPressedDown(button);
	}
	
	public boolean isPressed(int button) {
		return buttonBh.isPressed(button);
	}
	
	public boolean isPressedUp(int button) {
		return buttonBh.isPressedUp(button);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}
