package com.paleon.engine.world;

import org.joml.Vector2i;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.core.Display;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.utils.MousePicker;

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
    
    private Vector2i[][] positions;
    
    private int lastX;
    private int lastY;
    
    private int firstX;
    private int firstY;
    
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

        switch(craft) {
	        case BUILDING:
	        	if(!world.onGuiLayer) {
	        		building();    
	        	}
	        	break;
	        case AGRICULTURE:
	        	if(!world.onGuiLayer) {
	        		agriculture();    
	        	}
	        	break;
	        case GATHERING:
	        	if(!world.onGuiLayer) {
	        		gathering();    
	        	}
	        	break;
			default:
				break;
        }
    }
    
    private void building() {
    	
    }
    
    private void agriculture() {
    	
    }
    
    private void gathering() {
    	Vector2i currentCell = MousePicker.getGridPoint();
    	
    	if(Mouse.isButtonDown(0)) {
	    	firstX = currentCell.x;
	    	firstY = currentCell.y;
    	}
    	
    	if(Mouse.isButton(0)) {
    		int currX = currentCell.x;
    		int currY = currentCell.y;
    		
    		if(lastX != currX || lastY != currY) {
    			lastX = currX;
    			lastY = currY;
    			
	    		if(positions != null) {
	    			for(int x = 0; x < positions.length; x++) {
	        			for(int y = 0; y <  positions[0].length; y++) {
	        				world.cells.get(positions[x][y].x + " " + positions[x][y].y).hide();
	        			}
	        		}
	    		}
	    		
	    		positions = new Vector2i[Math.abs(firstX - currX) + 1][Math.abs(firstY - currY) + 1];
	    		
	    		for(int x = 0; x < positions.length; x++) {
	    			for(int y = 0; y <  positions[0].length; y++) {
	    				positions[x][y] = new Vector2i();
	    				
	    				if(currX <  firstX)
	    					positions[x][y].x = firstX - x;
	    				else
	    					positions[x][y].x = firstX + x;
	    				   				
	    				if(currY <  firstY)
	    					positions[x][y].y = firstY - y;
	    				else
	    					positions[x][y].y = firstY + y;
	    			}
	    		}
	    		
	    		for(int x = 0; x < positions.length; x++) {
	    			for(int y = 0; y <  positions[0].length; y++) {
	    				world.cells.get(positions[x][y].x + " " + positions[x][y].y).show();
	    			}
	    		}
    		}
    	}
    	
    	if(Mouse.isButtonUp(0)) {
    		for(int x = 0; x < positions.length; x++) {
    			for(int y = 0; y <  positions[0].length; y++) {
    				if(world.cells.get(positions[x][y].x + " " + positions[x][y].y).getState() != 2) {
    					world.cells.get(positions[x][y].x + " " + positions[x][y].y).hide();
    				}
    			}
    		}
    	}
    }

    @Override
    public void onGUI() {

    }

}
