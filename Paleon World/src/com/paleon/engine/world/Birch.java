package com.paleon.engine.world;

import com.paleon.engine.components.Material;
import com.paleon.engine.components.Model;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class Birch extends Entity {

	public Birch(World world) {
		super(world, "Birch");
		
		addComponent(new Model(ResourceManager.getMesh("birch_trunk")));
		addComponent(new Material(ResourceManager.getTexture("birch_trunk")));
		setTransform(new Transform3D());
		scale.set(2.5f);
		
		Entity leaves = new Entity(world, "");
		leaves.addComponent(new Model(ResourceManager.getMesh("birch_leaves")));
		leaves.addComponent(new Material(ResourceManager.getTexture("birch_leaves")));
		leaves.setTransform(new Transform3D());
		
		addChild(leaves);
	}

}
