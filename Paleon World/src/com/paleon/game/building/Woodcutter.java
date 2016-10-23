package com.paleon.game.building;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.Model;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.toolbox.MathUtils;

public class Woodcutter extends Entity {

	public Woodcutter(World world) {
		super("Woodcutter", world);
		
		addComponent(new Model(ResourceManager.getMesh("woodcutter")));
		setFurthestPoint(ResourceManager.getMesh("woodcutter").getFurthestPoint());
		scale.set(2.5f);
		setID(MathUtils.genID());
		
		addBehaviour(new ProfessionGuiBh(1));
	}

}
