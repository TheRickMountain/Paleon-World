package com.paleon.game.building;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.MeshFilter;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.toolbox.MathUtils;

public class FishingDock extends Entity {

	public FishingDock(World world) {
		super("Fishing Dock", world);
		
		addComponent(new MeshFilter(ResourceManager.getMesh("fishingDock")));
		setFurthestPoint(ResourceManager.getMesh("fishingDock").getFurthestPoint());
		setID(MathUtils.genID());
		scale.set(3.5f);
		addBehaviour(new ProfessionGuiBh(2));
	}

}
