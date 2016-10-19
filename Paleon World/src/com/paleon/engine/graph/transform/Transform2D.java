package com.paleon.engine.graph.transform;

import com.paleon.engine.scenegraph.Entity3D;
import com.paleon.engine.utils.MathUtils;

/**
 * Created by Rick on 12.10.2016.
 */
public class Transform2D extends Transform {

    public Transform2D() {
        super();
    }

    public void update() {
        MathUtils.getModelMatrix(modelMatrix, position.x, position.y, rotation.z, scale.x, scale.y);

        Entity3D pp = parent.getParent();
        if (pp != null) {
            pp.getTransform().getModelMatrix().mul(modelMatrix, modelMatrix);
        }
    }

}
