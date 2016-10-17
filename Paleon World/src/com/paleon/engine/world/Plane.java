package com.paleon.engine.world;

import com.paleon.engine.components.Model;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

/**
 * Created by Rick on 17.10.2016.
 */
public class Plane extends Entity {


    public Plane(World world) {
        super(world, "Plane");

        addComponent(new Model(ResourceManager.getMesh("plane")));
        addComponent(ResourceManager.getMaterial("plane"));
        setTransform(new Transform3D());
        scale.set(1.5f);
    }
}
