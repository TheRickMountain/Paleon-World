package com.paleon.engine.behaviours;

import com.paleon.engine.scenegraph.Entity3D;

public abstract class Behaviour {

    public boolean enabled = true;
	
	public Entity3D parent;
	
	public void setParent(Entity3D parent) {
		this.parent = parent;
	}
	
	public abstract void start();
	
	public abstract void update(float deltaTime);
	
	public abstract void onGUI();
	
}