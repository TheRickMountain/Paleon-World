package com.paleon.engine.world;

import org.joml.Vector3f;

import com.paleon.engine.behaviours.BoundingBoxBh;
import com.paleon.engine.components.Material;
import com.paleon.engine.components.Model;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

/**
 * Created by Rick on 17.10.2016.
 */
public class Barn extends Entity {

    public Barn(World world) {
        super(world, "Barn");

        addComponent(new Model(ResourceManager.getMesh("barn")));
        addComponent(new Material(ResourceManager.getTexture("barn")));
        setTransform(new Transform3D());
        scale.set(3.5f);
        
        BoundingBoxBh bbh = new BoundingBoxBh(new Vector3f(-10f, 0f, -10f), 
        		new Vector3f(10f, 15f, 10f));
        addBehaviour(bbh);
        addBehaviour(new BarnBh(bbh));
    }

}
