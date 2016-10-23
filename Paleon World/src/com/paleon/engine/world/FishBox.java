package com.paleon.engine.world;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.Model;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class FishBox extends Entity {

	public FishBox(World world) {
		super("Fish Box", world);
		
		addComponent(new Model(ResourceManager.getMesh("fishBox")));
		setFurthestPoint(ResourceManager.getMesh("fishBox").getFurthestPoint());
		localScale.set(0.3f);
	}

}
