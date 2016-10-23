package com.paleon.engine.components;

import com.paleon.engine.scenegraph.Entity;

public abstract class Component {

	public boolean enabled = true;
	
	protected Entity parent;
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}
	
}
