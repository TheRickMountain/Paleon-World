package com.paleon.engine.world;

import com.paleon.engine.components.Material;
import com.paleon.engine.components.Model;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class Conifer extends Entity {

	public Conifer(World world) {
		super(world, "Conifer");
		
		addComponent(new Model(ResourceManager.getMesh("conifer_trunk")));
		addComponent(new Material(ResourceManager.getTexture("conifer_trunk")));
		setTransform(new Transform3D());
		scale.set(2f);
		
		Entity leaves = new Entity(world, "");
		leaves.addComponent(new Model(ResourceManager.getMesh("conifer_leaves")));
		leaves.addComponent(new Material(ResourceManager.getTexture("conifer_leaves")));
		leaves.setTransform(new Transform3D());
		
		addChild(leaves);
	}

}
