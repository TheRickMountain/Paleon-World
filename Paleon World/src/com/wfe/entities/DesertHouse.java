package com.wfe.entities;

import com.wfe.behaviours.BoundingBoxBh;
import com.wfe.components.Material;
import com.wfe.components.Model;
import com.wfe.core.ResourceManager;
import com.wfe.graph.transform.Transform3D;
import com.wfe.math.Vector3f;
import com.wfe.scenegraph.Entity;
import com.wfe.scenegraph.World;

public class DesertHouse extends Entity {

	public DesertHouse(World world) {
		super(world, "Desert House");
		
        setTransform(new Transform3D());
        addComponent(new Model(ResourceManager.getMesh("desertHouse")));
        addComponent(new Material(ResourceManager.getTexture("desertHouse")));
        scale.set(6);
        addBehaviour(new BoundingBoxBh(new Vector3f(-6f, 0f, -6f),
        		new Vector3f(6f, 9f, 6f)));
	}

}
