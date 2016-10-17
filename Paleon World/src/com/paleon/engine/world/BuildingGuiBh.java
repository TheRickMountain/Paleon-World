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
            cellSize = new Plane[8][8];

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

                int state = 0;

                Vector3f ctp = MousePicker.getCurrentTerrainPoint();
                if (ctp != null) {
                    building.position.set(ctp);

                    state = world.cells.get(String.valueOf(ctp.x) + "," + String.valueOf(ctp.z)).state;

                    for(int x = 0; x < cellSize.length; x++) {
                        for(int z = 0; z < cellSize[0].length; z++) {
                            cellSize[x][z].position.x = ctp.x + x * 3f;
                            cellSize[x][z].position.z = ctp.z + z * 3f;
                            cellSize[x][z].position.y = ctp.y + 0.1f;

                            if(state == 1) {
                                cellSize[x][z].getComponent(Material.class).color.set(1.0f, 0.0f, 0.0f);
                            } else {
                                cellSize[x][z].getComponent(Material.class).color.set(0.0f, 1.0f, 0.0f);
                            }
                        }
                    }

                }

                if(Mouse.isButtonDown(0)) {
                    if(state == 0) {
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
