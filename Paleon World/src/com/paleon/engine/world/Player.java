package com.paleon.engine.world;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.behaviour.CharacterAnimBh;
import com.paleon.engine.behaviour.SettlerBh;
import com.paleon.engine.components.MeshFilter;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;

public class Player extends Entity {

	private Entity leftArm;
	
	public Player(String flName, String tag, World world) {
		super(tag, world);
		this.addComponent(new MeshFilter(ResourceManager.getMesh("body")));
		this.addBehaviour(new CharacterAnimBh());
		this.addBehaviour(new SettlerBh(flName, world));
		
		{
			Entity head = new Entity("Head", world);
			head.addComponent(new MeshFilter(ResourceManager.getMesh("head")));
			head.localPosition.y = 0.85f;
			this.addChild(head);
			
			Entity eyes = new Entity("Eyes", world);
			eyes.addComponent(new MeshFilter(ResourceManager.getMesh("eyes")));
			eyes.localPosition.y = -0.1f;
			eyes.localRotation.y = 180;
			this.addChild(eyes);
			
			Entity rightArm = new Entity("RightArm", world);
			rightArm.addComponent(new MeshFilter(ResourceManager.getMesh("rightArm")));
			rightArm.localPosition.set(0.65f, 0.6f, 0);
			this.addChild(rightArm);
			
			{
				Entity rightForearm = new Entity("RightForearm", world);
				rightForearm.addComponent(new MeshFilter(ResourceManager.getMesh("rightForearm")));
				rightForearm.localPosition.set(1.08f, -0.7f, 0);
				rightArm.addChild(rightForearm);
			}
			
			leftArm = new Entity("LeftArm", world);
			leftArm.addComponent(new MeshFilter(ResourceManager.getMesh("leftArm")));
			leftArm.localPosition.set(-0.65f, 0.6f, 0);
			this.addChild(leftArm);
			
			{
				Entity leftForearm = new Entity("LeftForearm", world);
				leftForearm.addComponent(new MeshFilter(ResourceManager.getMesh("leftForearm")));
				leftForearm.localPosition.set(-1.08f, -0.7f, 0);
				leftArm.addChild(leftForearm);
			}
			
			Entity leftHip = new Entity("LeftHip", world);
			leftHip.addComponent(new MeshFilter(ResourceManager.getMesh("hip")));
			leftHip.localPosition.set(-0.4f, -0.45f, 0);
			this.addChild(leftHip);
			
			{
				Entity leftShin = new Entity("LeftShin", world);
				leftShin.addComponent(new MeshFilter(ResourceManager.getMesh("shin")));
				leftShin.localPosition.set(0, -0.8f, 0);
				leftHip.addChild(leftShin);
			}
			
			Entity rightHip = new Entity("RightHip", world);
			rightHip.addComponent(new MeshFilter(ResourceManager.getMesh("hip")));
			rightHip.localPosition.set(0.4f, -0.45f, 0);
			this.addChild(rightHip);
			
			{
				Entity rightShin = new Entity("RightShin", world);
				rightShin.addComponent(new MeshFilter(ResourceManager.getMesh("shin")));
				rightShin.localPosition.set(0, -0.8f, 0);
				rightHip.addChild(rightShin);
			}
		}
	}

}
