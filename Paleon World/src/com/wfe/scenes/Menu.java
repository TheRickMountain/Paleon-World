package com.wfe.scenes;

import com.wfe.behaviours.MainMenuBh;
import com.wfe.components.Text;
import com.wfe.core.Display;
import com.wfe.core.IScene;
import com.wfe.core.ResourceManager;
import com.wfe.entities.Button;
import com.wfe.graph.Camera;
import com.wfe.graph.render.GUIRenderer;
import com.wfe.graph.transform.Transform2D;
import com.wfe.math.Vector3f;
import com.wfe.scenegraph.Entity;
import com.wfe.scenegraph.World;
import com.wfe.utils.Color;

public class Menu implements IScene {

	private World world;
	
	@Override
	public void loadResources() {
		ResourceManager.loadTexture("rock", "rock");
	}

	@Override
	public void init() throws Exception {
		Camera camera = new Camera(new Vector3f());
		world = new World(camera);
		
		Entity mainMenu = new Entity(world, "Main Menu");
		mainMenu.setTransform(new Transform2D());
		
		Button newGameBtn = new Button(world, "New Game Button", ResourceManager.getTexture("rock"));
		newGameBtn.position.set(15, Display.getHeight() / 2);
		newGameBtn.scale.set(155, 35);
		mainMenu.addChild(newGameBtn);
		
		mainMenu.addBehaviour(new MainMenuBh());
		
		Entity newGameText = new Entity(world, "New Game");
		newGameText.addComponent(new Text("New Game", GUIRenderer.primitiveFont, 1.75f, Color.WHITE));
		newGameText.setTransform(new Transform2D());
		newGameText.position.set(20, Display.getHeight() / 2);
		
		Entity continueText = new Entity(world, "Continue");
		continueText.addComponent(new Text("Continue", GUIRenderer.primitiveFont, 1.75f, Color.WHITE));
		continueText.setTransform(new Transform2D());
		continueText.position.set(20, Display.getHeight() / 2 + 35);
		
		Entity optionsText = new Entity(world, "Options");
		optionsText.addComponent(new Text("Options", GUIRenderer.primitiveFont, 1.75f, Color.WHITE));
		optionsText.setTransform(new Transform2D());
		optionsText.position.set(20, Display.getHeight() / 2 + 70);
		
		Entity exitText = new Entity(world, "Exit");
		exitText.addComponent(new Text("Exit", GUIRenderer.primitiveFont, 1.75f, Color.WHITE));
		exitText.setTransform(new Transform2D());
		exitText.position.set(20, Display.getHeight() / 2 + 105);
	}

	@Override
	public void update(float dt) throws Exception {
		world.updateMenu(dt);
		world.renderMenu();
	}

	@Override
	public void cleanup() {
		world.cleanup();
	}

}
