package com.paleon.engine.components;

import com.paleon.engine.graph.Mesh;

/**
 * Created by Rick on 14.10.2016.
 */
public class Model extends Component {

    public Mesh mesh;

    public Model(Mesh mesh) {
        type = Type.MODEL;
        this.mesh = mesh;
    }

}
