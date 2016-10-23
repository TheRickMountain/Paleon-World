package com.paleon.engine.world;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.Model;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class Firewood extends Entity {

	public Firewood(World world) {
		super("Firewood", world);
		addComponent(new Model(ResourceManager.getMesh("firewood")));
		setFurthestPoint(ResourceManager.getMesh("firewood").getFurthestPoint());
	}

}
