package com.paleon.engine.graph.transform;

import com.paleon.engine.scenegraph.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Rick on 13.10.2016.
 */
public abstract class Transform {

    public Entity parent;
    
    public boolean active = true;

    protected final Matrix4f modelMatrix;

    protected Vector3f position;
    protected Vector3f rotation;
    protected Vector3f scale;

    public void setParent(Entity parent) {
        this.parent = parent;

        this.position = parent.position;
        this.rotation = parent.rotation;
        this.scale = parent.scale;
    }

    public Transform() {
        modelMatrix = new Matrix4f();
    }

    public abstract void update();

    public final Matrix4f getModelMatrix() {
        return modelMatrix;
    }


}
