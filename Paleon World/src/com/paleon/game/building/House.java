package com.paleon.game.building;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.MeshFilter;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class House extends Entity {

	public House(World world) {
		super("House", world);
		addComponent(new MeshFilter(ResourceManager.getMesh("house")));
		setFurthestPoint(ResourceManager.getMesh("house").getFurthestPoint());
		scale.set(3.5f);
	}

}
