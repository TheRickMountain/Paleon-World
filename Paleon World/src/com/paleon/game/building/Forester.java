package com.paleon.game.building;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.MeshFilter;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.toolbox.MathUtils;

public class Forester extends Entity {

	public Forester(World world) {
		super("Forester", world);
		
		addComponent(new MeshFilter(ResourceManager.getMesh("forester")));
		setFurthestPoint(ResourceManager.getMesh("forester").getFurthestPoint());
		setID(MathUtils.genID());
		scale.set(3f);
		addBehaviour(new ProfessionGuiBh(4));
	}

}
