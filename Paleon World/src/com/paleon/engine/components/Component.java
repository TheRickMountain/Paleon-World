package com.paleon.engine.components;

import com.paleon.engine.scenegraph.Entity3D;

/**
 * Created by Rick on 06.10.2016.
 */

public abstract class Component {

    public boolean enabled = true;

    public Type type;

    public Entity3D parent;

    public void setParent(Entity3D parent) {
        this.parent = parent;
    }

    public enum Type {
        MODEL,
        MATERIAL,
        TEXT,
        IMAGE
    }

}

