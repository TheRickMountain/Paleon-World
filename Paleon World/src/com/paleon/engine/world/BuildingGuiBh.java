package com.paleon.engine.world;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.components.Material;
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

    private Button buildingButton;

    private Entity building;

    private Plane[][] cellSize;

    public BuildingGuiBh(Button buildingButton) {
        this.buildingButton = buildingButton;
    }

    private World world;

    @Override
    public void start() {
        this.world = parent.getWorld();
    }

    @Override
    public void update(float deltaTime) {
        if(buildingButton.isOverMouse()) {
            world.onGuiLayer = true;
        } else {
            world.onGuiLayer = false;
        }

        if(buildingButton.isPressedDown(0)) {
            building = new House(world);
            cellSize = new Plane[7][7];

            for(int x = 0; x < cellSize.length; x++) {
                for(int z = 0; z < cellSize[0].length; z++) {
                    cellSize[x][z] = new Plane(parent.getWorld());
                }
            }
        }

        if(building != null) {
            if(!world.onGuiLayer) {
                if(Keyboard.isKeyDown(Key.R)) {
                    building.rotation.y += 90;
                }

                int totalState = 0;

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
                            	totalState = 0;
                            } else {
                            	cellSize[x + halfXS][z + halfZS].getComponent(Material.class).color.set(1.0f, 0.0f, 0.0f);
                            	totalState = 1;
                            }
                     
                        }
                    }

                }

                if(Mouse.isButtonDown(0)) {
                    if(totalState == 0) {
                        building = null;

                        for(int x = 0; x < cellSize.length; x++) {
                            for(int z = 0; z < cellSize[0].length; z++) {
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
