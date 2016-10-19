package com.paleon.engine.graph.transform;

import com.paleon.engine.scenegraph.Entity3D;
import com.paleon.engine.utils.MathUtils;

/**
 * Created by Rick on 12.10.2016.
 */
public class Transform3D extends Transform {

    public Transform3D() {
        super();
    }

    public void update() {
        MathUtils.getModelMatrix(modelMatrix, position, rotation, scale);

        Entity3D pp = parent.getParent();
        if (pp != null) {
            pp.getTransform().getModelMatrix().mul(modelMatrix, modelMatrix);
        }
    }

}
