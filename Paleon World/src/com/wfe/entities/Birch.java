package com.wfe.entities;

import com.wfe.components.Material;
import com.wfe.components.Model;
import com.wfe.core.ResourceManager;
import com.wfe.graph.transform.Transform3D;
import com.wfe.scenegraph.Entity;
import com.wfe.scenegraph.World;

public class Birch extends Entity {

	public Birch(World world) {
		super(world, "Birch");
		
		addComponent(new Model(ResourceManager.getMesh("birch_trunk")));
		addComponent(new Material(ResourceManager.getTexture("birch_trunk")));
		setTransform(new Transform3D());
		scale.set(2.5f);
		
		Entity leaves = new Entity(world, "");
		leaves.addComponent(new Model(ResourceManager.getMesh("birch_leaves")));
		Material leavesMat = new Material(ResourceManager.getTexture("birch_leaves"));
		leavesMat.transparency = true;
		leaves.addComponent(leavesMat);
		leaves.setTransform(new Transform3D());
		
		addChild(leaves);
	}

}
