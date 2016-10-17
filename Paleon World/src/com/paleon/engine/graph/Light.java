package com.paleon.engine.graph;

import com.paleon.engine.utils.Color;
import org.joml.Vector3f;

/**
 * Created by Rick on 08.10.2016.
 */
public class Light {

    public final Vector3f position;
    public final Color color;

    public Light(Vector3f position, Color color) {
        this.position = position;
        this.color = color;
    }

}
