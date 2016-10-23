package com.paleon.game.building;

import java.util.ArrayList;
import java.util.List;

import com.paleon.engine.Display;
import com.paleon.engine.ResourceManager;
import com.paleon.engine.behaviour.Behaviour;
import com.paleon.engine.behaviour.SettlerBh;
import com.paleon.engine.components.Image;
import com.paleon.engine.components.Text;
import com.paleon.engine.graph.Texture2D;
import com.paleon.engine.graph.gui.Button;
import com.paleon.engine.graph.systems.GUIRendererSystem;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.toolbox.Color;
import com.paleon.scenes.Game;

public class ProfessionGuiBh extends Behaviour {

	private boolean show = false;
	private boolean showSettlersList = false;
	private List<Entity> settlers = new ArrayList<Entity>();
	
	private Texture2D buttonSkin = ResourceManager.getTexture("ui_button");
	private Texture2D slotSkin = ResourceManager.getTexture("ui_slot");
	private Texture2D closeSkin = ResourceManager.getTexture("ui_close");
	private Texture2D questionSkin = ResourceManager.getTexture("ui_question");
	private Texture2D settlerSkin = ResourceManager.getTexture("ui_settler");
	private Texture2D stoneAxeSkin = ResourceManager.getTexture("ui_stone axe");
	
	private Button close;
	
	private Button worker[];
	
	private Entity background;
	
	private Text settlerName;
	
	public ProfessionGuiBh(int workersCount) {
		worker = new Button[workersCount];
	}
	
	@Override
	public void start() {
		close = new Button(20, 20, closeSkin, parent.getWorld());
		close.position.x = Display.getWidth() / 2 + 110;
		close.position.y = Display.getHeight() / 2 - 100;
		close.deactivate();
		parent.getWorld().addEntity(close);
		
		background = new Entity(parent.getWorld());
		background.position.x = Display.getWidth() / 2 - 110;
		background.position.y = Display.getHeight() / 2 - 80;
		background.scale.x = 220;
		background.scale.y = 160;
		background.addComponent(new Image(slotSkin, Color.WHITE));
		background.deactivate();
		parent.getWorld().addEntity(background);
		
		for(int i = 0; i < worker.length; i++) {
			worker[i] = new Button(40, 40, questionSkin, parent.getWorld());
			worker[i].position.x = (Display.getWidth() / 2 - 110) + i * 50 + 15;
			worker[i].position.y = (Display.getHeight() / 2 - 80) + 50;
			worker[i].deactivate();
			parent.getWorld().addEntity(worker[i]);
		}
		
		settlerName = new Text("test", GUIRendererSystem.primitiveFont, 1.25f, Color.WHITE, 1f, false);
	}

	@Override
	public void update(float deltaTime) {
		if(Mouse.isButtonDown(0)) {
			if(parent.getID() == parent.getWorld().COLOR_PICKING_ID) {
				show();
			}
		}
		
		if(show) {
			if(close.isPressedDown(0)) {
				hide();
			}
			
			for(int i = 0; i < worker.length; i++) {
				if(worker[i].isPressedDown(0)) {
					showSettlersList = true;
					settlers = parent.getWorld().getEntityListByName("Settler");
					/*for(Entity settler : settlers) {
						if(settler != null) {
							if(settler.getBehaviour(SettlerBh.class).getWorkPlace() == null) {
								settler.getBehaviour(SettlerBh.class).setWorkPlace(parent);
								worker[i].getComponent(Image.class).texture = settlerSkin;
								return;
							}
						}
					}*/
				}
			}
		}
	}

	@Override
	public void onGUI() {
		if(showSettlersList) {
			for(int i = 0; i < settlers.size(); i++) {
				if(Mouse.isButtonDown(0)) {
					if(Mouse.getX() >= background.position.x && Mouse.getX() <= background.position.x + 200 &&
							Mouse.getY() >= background.position.y + i * 50 && Mouse.getY() <= background.position.y + i * 50 + 50) {
						if(settlers.get(i).getBehaviour(SettlerBh.class).getWorkPlace() == null) {
							settlers.get(i).getBehaviour(SettlerBh.class).setWorkPlace(parent);
							worker[i].getComponent(Image.class).texture = settlerSkin;
							showSettlersList = false;
						}
					}
				}
				
				GUIRendererSystem.render(background.position.x, background.position.y + i * 50, 200, 50, buttonSkin);
				GUIRendererSystem.render(background.position.x, background.position.y + i * 50, 40, 40, settlerSkin);
				if(settlers.get(i).getBehaviour(SettlerBh.class).getWorkPlace() != null) {
					GUIRendererSystem.render(background.position.x + 100, background.position.y + i * 50, 30, 30, stoneAxeSkin);
				}
				settlerName.setText(settlers.get(i).getBehaviour(SettlerBh.class).flName);
				GUIRendererSystem.render(background.position.x + 50, background.position.y + i * 50, settlerName);
			}
		}
	}

	public void show() {
		show = true;
		close.activate();
		background.activate();
		
		for(int i = 0; i < worker.length; i++) {
			worker[i].activate();
		}
		
		Game.state = Game.State.INVENTORY;
	}
	
	public void hide() {
		show = false;
		close.deactivate();
		background.deactivate();
		
		for(int i = 0; i < worker.length; i++) {
			worker[i].deactivate();
		}
		
		Game.state = Game.State.GAME;
	}
	
}
