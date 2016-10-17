package com.paleon.engine.behaviours;

import com.paleon.engine.scenegraph.Entity;

public abstract class Behaviour {

    public boolean enabled = true;
	
	public Entity parent;
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}
	
	public abstract void start();
	
	public abstract void update(float deltaTime);
	
	public abstract void onGUI();
	
}