package com.paleon.engine.world;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.components.Material;
import com.paleon.engine.core.Display;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.input.Key;
import com.paleon.engine.input.Keyboard;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.utils.MousePicker;
import org.joml.Vector3f;

/**
 * Created by Rick on 17.10.2016.
 */

public class BuildingGuiBh extends Behaviour {

	public enum Craft {
		BUILDING,
		AGRICULTURE,
		NONE
	}
	
    private Button house_ui;
    private Button wheat_ui;

    private Entity building;
    
    private Craft craft = Craft.NONE;

    private Plane[][] cellSize;
    
    private Vector3f firstCell;
    private int lastCCX = 0;
    private int lastCCZ = 0;
    
    
    public BuildingGuiBh() {
    	
    }

    private World world;

    @Override
    public void start() {
    	this.world = parent.getWorld();
    	
    	house_ui = new Button(world, "House Button", ResourceManager.getTexture("house_ui"));
        house_ui.scale.x = 48;
        house_ui.scale.y = 48;
        house_ui.position.x = Display.getWidth() - house_ui.scale.x - 5;
        house_ui.position.y = Display.getHeight() - house_ui.scale.y - 5;
        
        wheat_ui = new Button(world, "Wheat Button", ResourceManager.getTexture("wheat_ui"));
        wheat_ui.scale.x = 48;
        wheat_ui.scale.y = 48;
        wheat_ui.position.x = house_ui.position.x - wheat_ui.scale.x - 5;
        wheat_ui.position.y = house_ui.position.y;
    }

    @Override
    public void update(float deltaTime) {
        if(house_ui.isOverMouse()) {
            world.onGuiLayer = true;
        } else if(wheat_ui.isOverMouse()) {
        	world.onGuiLayer = true;
        } else {
            world.onGuiLayer = false;
        }
        
        if(house_ui.isPressedDown(0)) {
        	craft = Craft.BUILDING;
            building = new House(world);
            cellSize = new Plane[7][7];

            for(int x = 0; x < cellSize.length; x++) {
                for(int z = 0; z < cellSize[0].length; z++) {
                    cellSize[x][z] = new Plane(parent.getWorld());
                }
            }
        }
        
        if(wheat_ui.isPressedDown(0)) {
        	craft = Craft.AGRICULTURE;
        }
        
        if(craft.equals(Craft.AGRICULTURE)) {
        	if(!world.onGuiLayer) {
        		Vector3f ctp = MousePicker.getCurrentTerrainPoint();
        		if(ctp != null) {

        			if(Mouse.isButtonDown(0)) {
        				firstCell = new Vector3f(ctp.x, 0, ctp.z);
        			}

        			if(Mouse.isButton(0)) {
        				int ccX = (int)(((ctp.x - firstCell.x) / 3)) + 1;
        				int ccZ = (int)(((ctp.z - firstCell.z) / 3)) + 1;
        				
        				System.out.println(ccX + " " + ccZ);
        				
        				if(lastCCX != ccX || lastCCZ != ccZ) {
        					lastCCX = ccX;
        					lastCCZ = ccZ;
        					
        					
        					if(cellSize != null) {
	        					for(int x = 0; x < cellSize.length; x++) {        						
	        						for(int z = 0; z < cellSize[0].length; z++) {
	        							cellSize[x][z].remove();
	        						}
	        					}
        					}
        					
        					cellSize = new Plane[ccX][ccZ];
        					
        					for(int x = 0; x < ccX; x++) {        						
        						for(int z = 0; z < ccZ; z++) {
        							Plane plane = new Plane(world);
        							plane.position.x = firstCell.x + x * 3;
        							plane.position.z = firstCell.z + z * 3;
        							plane.position.y = ctp.y + 0.1f;
        							
        							int state = world.cells.get(plane.position.x + "," + plane.position.z).state;
        							
        							if(state == 0) {
        								plane.getComponent(Material.class).color.set(0.0f, 1.0f, 0.0f);
        							} else {
        								plane.getComponent(Material.class).color.set(1.0f, 0.0f, 0.0f);
        							}
        							
        							cellSize[x][z] = plane;
        						}       						
        					}
        				}
        				
        			}

        			if(Mouse.isButtonUp(0)) {

        			}
        		}
        	}
        }

        if(craft.equals(Craft.BUILDING)) {
        	if(!world.onGuiLayer) {
        		if(Keyboard.isKeyDown(Key.R)) {
        			building.rotation.y += 90;
        		}

        		Vector3f ctp = MousePicker.getCurrentTerrainPoint();
        		if (ctp != null) {
        			building.position.set(ctp);

        			int xS = cellSize.length;
        			int zS = cellSize[0].length;

        			int halfXS = xS / 2;
        			int halfZS = zS / 2;

        			for(int x = -halfXS; x < cellSize.length - halfXS; x++) {
        				for(int z = -halfZS; z < cellSize[0].length - halfZS; z++) {
        					float ctpX = ctp.x + x * 3f;
        					float ctpZ = ctp.z + z * 3f;

        					cellSize[x + halfXS][z + halfZS].position.x = ctpX;
        					cellSize[x + halfXS][z + halfZS].position.z = ctpZ;
        					cellSize[x + halfXS][z + halfZS].position.y = ctp.y + 0.1f;

        					if(world.cells.get(String.valueOf(ctpX) + "," + String.valueOf(ctpZ)).state == 0) {
        						cellSize[x + halfXS][z + halfZS].getComponent(Material.class).color.set(0.0f, 1.0f, 0.0f);
        					} else {
        						cellSize[x + halfXS][z + halfZS].getComponent(Material.class).color.set(1.0f, 0.0f, 0.0f);
        					}

        				}
        			}


        		}

        		if(Mouse.isButtonDown(0)) {
        			int state = 0;

        			for(int x = 0; x < cellSize.length; x++) {
        				for(int z = 0; z < cellSize[0].length; z++) {
        					if(world.cells.get(
        							String.valueOf(cellSize[x][z].position.x) + 
        							"," + 
        							String.valueOf(cellSize[x][z].position.z)).state == 1) {
        						state = 1;
        					}
        				}
        			}

        			if(state != 1) {
        				building = null;
        				craft = Craft.NONE;

        				for(int x = 0; x < cellSize.length; x++) {
        					for(int z = 0; z < cellSize[0].length; z++) {
        						world.cells.get(
        								String.valueOf(cellSize[x][z].position.x) + 
        								"," + 
        								String.valueOf(cellSize[x][z].position.z)).state = 1;
        						cellSize[x][z].remove();
        					}
        				}
        			}
        		}
        	}
	     
        }
    }

    @Override
    public void onGUI() {

    }

}
