package com.paleon.engine.utils;

import org.joml.Vector3f;

import com.paleon.engine.components.Material;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.world.Plane;

/**
 * Created by Rick on 17.10.2016.
 */
public class CellInfo {

    public final Vector3f position;
    private int state;
    private Plane cell;

    public CellInfo(Vector3f position, int state, World world) {
        this.position = position;
        cell = new Plane(world);
        cell.position.set(position.x, position.y + 0.1f, position.z);
        cell.deactivate();
        setState(state);
    }
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
		
		if(state < 0 || state > 3)
			state = 0;
		
		if(state == 0)
			cell.getComponent(Material.class).color.set(0.0f, 1.0f, 0.0f);
		else if(state == 1)
			cell.getComponent(Material.class).color.set(1.0f, 0.0f, 0.0f);
		else if(state == 2)
			cell.getComponent(Material.class).color.set(1.0f, 1.0f, 0.0f);
	}
	
	public void show() {
		cell.activate();
	}
	
	public void hide() {
		cell.deactivate();
	}

}
