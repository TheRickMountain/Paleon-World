package com.paleon.engine.world;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.MeshFilter;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class Logs extends Entity {

	public Logs(World world) {
		super("Logs", world);
		addComponent(new MeshFilter(ResourceManager.getMesh("logs")));
		setFurthestPoint(ResourceManager.getMesh("logs").getFurthestPoint());
	}

}
