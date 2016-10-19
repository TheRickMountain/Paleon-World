package com.paleon.engine.world;

import com.paleon.engine.components.Material;
import com.paleon.engine.components.Model;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.scenegraph.Entity3D;
import com.paleon.engine.scenegraph.World;

/**
 * Created by Rick on 17.10.2016.
 */
public class Plane extends Entity3D {


    public Plane(World world) {
        super(world, "Plane");

        addComponent(new Model(ResourceManager.getMesh("plane")));
        Material planeMat = new Material(ResourceManager.getTexture("plane"));
        planeMat.useFakeLighting = true;
        addComponent(planeMat);
        setTransform(new Transform3D());
        scale.set(1.5f);
    }
}
