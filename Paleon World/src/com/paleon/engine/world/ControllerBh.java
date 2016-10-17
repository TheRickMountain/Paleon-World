package com.paleon.engine.world;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.graph.Camera;

/**
 * Created by Rick on 06.10.2016.
 */
public class ControllerBh extends Behaviour {

    private float speed = 10;
    public Camera camera;
    private AnimBh animBh;

    public ControllerBh(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void start() {
        parent.position.y = parent.getWorld().getTerrainHeight(parent.position.x, parent.position.z) + 2.25f;
        animBh = parent.getBehaviour(AnimBh.class);
    }

    @Override
    public void update(float deltaTime) {
        boolean move = false;

        if(move) {
            parent.position.y = parent.getWorld().getTerrainHeight(parent.position.x, parent.position.z) + 2.25f;
            animBh.walkAnim(deltaTime);
        } else {
            animBh.idleAnim(deltaTime);
        }

    }

    @Override
    public void onGUI() {

    }
}
