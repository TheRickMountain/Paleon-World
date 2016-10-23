package com.paleon.game.building;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.MeshFilter;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.toolbox.MathUtils;

public class Barn extends Entity {

	public Barn(World world) {
		super("Barn", world);
		
		addComponent(new MeshFilter(ResourceManager.getMesh("barn")));
		setFurthestPoint(ResourceManager.getMesh("barn").getFurthestPoint());
		scale.set(4.5f);
		setID(MathUtils.genID());
		addBehaviour(new BarnGuiBh());
	}

}
