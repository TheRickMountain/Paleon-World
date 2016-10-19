package com.paleon.engine.world;

import com.paleon.engine.components.Model;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.Camera;
import com.paleon.engine.scenegraph.Entity3D;
import com.paleon.engine.scenegraph.World;

/**
 * Created by Rick on 08.10.2016.
 */
public class SettlerPf extends Entity3D {

    public SettlerPf(World world, Camera camera) {
        super(world, "Settler");

        addComponent(new Model(ResourceManager.getMesh("body")));
        addComponent(ResourceManager.getMaterial("skin"));
        setTransform(new Transform3D());
        position.set(400, world.getTerrainHeight(400, 400), 400);

        Entity3D head = new Entity3D(world, "Head");
        head.addComponent(new Model(ResourceManager.getMesh("head")));
        head.addComponent(ResourceManager.getMaterial("skin"));
        head.setTransform(new Transform3D());
        head.position.set(0, 0.85f, 0);
        addChild(head);

        Entity3D eyes = new Entity3D(world, "Eyes");
        eyes.addComponent(new Model(ResourceManager.getMesh("eyes")));
        eyes.addComponent(ResourceManager.getMaterial("eyes"));
        eyes.setTransform(new Transform3D());
        eyes.position.set(0, -0.1f, 0);
        eyes.rotation.set(0, 180, 0);
        addChild(eyes);

        Entity3D rightArm = new Entity3D(world, "Right Arm");
        rightArm.addComponent(new Model(ResourceManager.getMesh("rightArm")));
        rightArm.addComponent(ResourceManager.getMaterial("skin"));
        rightArm.setTransform(new Transform3D());
        rightArm.position.set(0.65f, 0.6f, 0);
        addChild(rightArm);

        {
            Entity3D rightForearm = new Entity3D(world, "Right Forearm");
            rightForearm.addComponent(new Model(ResourceManager.getMesh("rightForearm")));
            rightForearm.addComponent(ResourceManager.getMaterial("skin"));
            rightForearm.setTransform(new Transform3D());
            rightForearm.position.set(1.08f, -0.7f, 0);
            rightArm.addChild(rightForearm);
        }

        Entity3D leftArm = new Entity3D(world, "Left Arm");
        leftArm.addComponent(new Model(ResourceManager.getMesh("leftArm")));
        leftArm.addComponent(ResourceManager.getMaterial("skin"));
        leftArm.setTransform(new Transform3D());
        leftArm.position.set(-0.65f, 0.6f, 0);
        addChild(leftArm);

        {
            Entity3D leftForearm = new Entity3D(world, "Left Forearm");
            leftForearm.addComponent(new Model(ResourceManager.getMesh("leftForearm")));
            leftForearm.addComponent(ResourceManager.getMaterial("skin"));
            leftForearm.setTransform(new Transform3D());
            leftForearm.position.set(-1.08f, -0.7f, 0);
            leftArm.addChild(leftForearm);
        }

        Entity3D leftHip = new Entity3D(world, "Left Hip");
        leftHip.addComponent(new Model(ResourceManager.getMesh("hip")));
        leftHip.addComponent(ResourceManager.getMaterial("skin"));
        leftHip.setTransform(new Transform3D());
        leftHip.position.set(-0.4f, -0.45f, 0);
        addChild(leftHip);

        {
            Entity3D leftShin = new Entity3D(world, "Left Shin");
            leftShin.addComponent(new Model(ResourceManager.getMesh("shin")));
            leftShin.addComponent(ResourceManager.getMaterial("skin"));
            leftShin.setTransform(new Transform3D());
            leftShin.position.set(0, -0.8f, 0);
            leftHip.addChild(leftShin);
        }

        Entity3D rightHip = new Entity3D(world, "Right Hip");
        rightHip.addComponent(new Model(ResourceManager.getMesh("hip")));
        rightHip.addComponent(ResourceManager.getMaterial("skin"));
        rightHip.setTransform(new Transform3D());
        rightHip.position.set(0.4f, -0.45f, 0);
        addChild(rightHip);

        {
            Entity3D rightShin = new Entity3D(world, "Right Shin");
            rightShin.addComponent(new Model(ResourceManager.getMesh("shin")));
            rightShin.addComponent(ResourceManager.getMaterial("skin"));
            rightShin.setTransform(new Transform3D());
            rightShin.position.set(0, -0.8f, 0);
            rightHip.addChild(rightShin);
        }

        addBehaviour(new AnimBh());
        addBehaviour(new ControllerBh(camera));
    }
}
