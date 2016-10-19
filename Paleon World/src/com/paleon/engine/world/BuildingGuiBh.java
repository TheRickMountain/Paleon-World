package com.paleon.engine.world;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.core.Display;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.scenegraph.World;

/**
 * Created by Rick on 17.10.2016.
 */

public class BuildingGuiBh extends Behaviour {

	public enum Craft {
		BUILDING,
		AGRICULTURE,
		GATHERING,
		NONE
	}
	
    private Button barn_ui;
    private Button wheat_ui;
    private Button gathering_ui;
    
    private Craft craft = Craft.NONE;
    
    public BuildingGuiBh() {
    	
    }

    private World world;

    @Override
    public void start() {
    	this.world = parent.getWorld();
    	
    	barn_ui = new Button(world, "Barn Button", ResourceManager.getTexture("barn_ui"));
        barn_ui.scale.x = 48;
        barn_ui.scale.y = 48;
        barn_ui.position.x = Display.getWidth() - barn_ui.scale.x - 5;
        barn_ui.position.y = Display.getHeight() - barn_ui.scale.y - 5;
        
        wheat_ui = new Button(world, "Wheat Button", ResourceManager.getTexture("wheat_ui"));
        wheat_ui.scale.x = 48;
        wheat_ui.scale.y = 48;
        wheat_ui.position.x = barn_ui.position.x - wheat_ui.scale.x - 5;
        wheat_ui.position.y = barn_ui.position.y;
        
        gathering_ui = new Button(world, "Gathering Button", ResourceManager.getTexture("gathering_ui"));
        gathering_ui.scale.x = 48;
        gathering_ui.scale.y = 48;
        gathering_ui.position.x = wheat_ui.position.x - gathering_ui.scale.x - 5;
        gathering_ui.position.y = wheat_ui.position.y;
    }

    @Override
    public void update(float deltaTime) {
        if(barn_ui.isOverMouse()) {
            world.onGuiLayer = true;
        } else if(wheat_ui.isOverMouse()) {
        	world.onGuiLayer = true;
        } else if(gathering_ui.isOverMouse()) {
        	world.onGuiLayer = true;
        } else {
            world.onGuiLayer = false;
        }
        
        if(barn_ui.isPressedDown(0)) {
        	craft = Craft.BUILDING;
        }
        
        if(wheat_ui.isPressedDown(0)) {
        	craft = Craft.AGRICULTURE;
        }
        
        if(gathering_ui.isPressedDown(0)) {
        	craft = Craft.GATHERING;
        }

        if(craft.equals(Craft.BUILDING)) {
        	building();    
        }
        
        if(craft.equals(Craft.AGRICULTURE)) {
        	agriculture();
        }
        
        if(craft.equals(Craft.GATHERING)) {
        	gathering();
        }
    }
    
    private void building() {
    	
    }
    
    private void agriculture() {
    	
    }
    
    private void gathering() {
    	
    }

    @Override
    public void onGUI() {

    }

}
