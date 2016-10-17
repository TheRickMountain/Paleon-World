package com.paleon.engine.world;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.scenegraph.Entity;

/**
 * Created by Rick on 08.10.2016.
 */
public class AnimBh extends Behaviour {

    private Entity rightArm;
    private Entity leftArm;
    private Entity rightHip;
    private Entity leftHip;
    private Entity leftForearm;
    private Entity rightForearm;
    private Entity leftShin;
    private Entity rightShin;

    private boolean extremitiesState = false;

    private int animSpeed = 180;

    @Override
    public void start() {
        rightArm = parent.getChildByName("Right Arm");
        leftArm = parent.getChildByName("Left Arm");
        rightHip = parent.getChildByName("Right Hip");
        leftHip = parent.getChildByName("Left Hip");
        leftForearm = leftArm.getChildByName("Left Forearm");
        rightForearm = rightArm.getChildByName("Right Forearm");
        leftShin = leftHip.getChildByName("Left Shin");
        rightShin = rightHip.getChildByName("Right Shin");
    }

    @Override
    public void update(float dt) {

    }

    public void walkAnim(float dt) {
        if(leftArm.rotation.x >= 40) {
            extremitiesState = true;
        } else if(leftArm.rotation.x <= -40){
            extremitiesState = false;
        }

        if(extremitiesState) {
            leftArm.rotation.x -= animSpeed * dt;
            rightArm.rotation.x += animSpeed * dt;

            leftHip.rotation.x += animSpeed * dt;
            rightHip.rotation.x -= animSpeed * dt;
        } else {
            leftArm.rotation.x += animSpeed * dt;
            rightArm.rotation.x -= animSpeed * dt;

            leftHip.rotation.x -= animSpeed * dt;
            rightHip.rotation.x += animSpeed * dt;
        }

        leftForearm.rotation.x = 70;
        rightForearm.rotation.x = 70;

        leftShin.rotation.x = -20;
        rightShin.rotation.x = -20;
    }

    public void idleAnim(float dt) {
        resetRotationX(leftArm, dt);
        resetRotationX(leftForearm, dt);

        resetRotationX(rightArm, dt);
        resetRotationX(rightForearm, dt);

        resetRotationX(leftHip, dt);
        resetRotationX(leftShin, dt);

        resetRotationX(rightHip, dt);
        resetRotationX(rightShin, dt);
    }

    private void resetRotationX(Entity entity, float dt) {
        if(entity.rotation.x > 2) {
            entity.rotation.x -= animSpeed * dt;
        } else if(entity.rotation.x < -2) {
            entity.rotation.x += animSpeed * dt;
        } else {
            entity.rotation.x = 0;
        }
    }

    @Override
    public void onGUI() {

    }
}
