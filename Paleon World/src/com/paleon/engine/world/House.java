package com.paleon.engine.world;

import com.paleon.engine.components.Model;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.scenegraph.Entity3D;
import com.paleon.engine.scenegraph.World;

/**
 * Created by Rick on 17.10.2016.
 */
public class House extends Entity3D {

    public House(World world) {
        super(world, "House");

        addComponent(new Model(ResourceManager.getMesh("house")));
        addComponent(ResourceManager.getMaterial("house"));
        setTransform(new Transform3D());
        scale.set(3.5f);
    }

}
