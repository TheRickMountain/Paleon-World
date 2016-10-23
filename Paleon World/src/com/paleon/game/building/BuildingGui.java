package com.paleon.game.building;

import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class BuildingGui extends Entity {

	public BuildingGui(World world) {
		super(world);
		addBehaviour(new BuildingGuiBh(world));
	}
	
}
