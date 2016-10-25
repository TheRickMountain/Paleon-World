package com.wfe.behaviours;

import com.wfe.graph.Camera;
import com.wfe.input.Key;
import com.wfe.input.Keyboard;
import com.wfe.input.Mouse;
import com.wfe.math.Vector3f;
import com.wfe.scenegraph.Entity;
import com.wfe.scenegraph.World;

public class ControllingBh extends Behaviour {

	private AnimBh anim;
	private BoundingBoxBh playerBB;
	
	private boolean move = false;
	
	private Camera camera;
	
	private float speed = 10.0f;
	
	private World world;
	
	public ControllingBh(Camera camera) {
		this.camera = camera;
	}
	
	@Override
	public void start() {
		this.anim = parent.getBehaviour(AnimBh.class);
		this.playerBB = parent.getBehaviour(BoundingBoxBh.class);
		this.world = parent.getWorld();
	}

	@Override
	public void update(float dt) {	
		for(Entity entity : world.entities) {
			if(entity.name != "Settler") {
				BoundingBoxBh bb = entity.getBehaviour(BoundingBoxBh.class);
				if(bb != null) {
					if(bb.intersect(playerBB)) {
						System.out.println("Collision Detected");
					}
				}
			}
		}
		
		if(Mouse.isButtonDown(0)) {
			for(Entity entity : world.entities) {
				BoundingBoxBh bb = entity.getBehaviour(BoundingBoxBh.class);
				if(bb != null) {
					if(bb.intersect()) {
						System.out.println(entity.name);
					}
				}
			}
		}
		
		moving(dt);
	}

	public void moving(float dt) {
		move = false;
		
		Vector3f parentPos = parent.position;
		parentPos.y = world.getTerrainHeight(parentPos.x, parentPos.z) + 2.2f;
		Vector3f parentRot = parent.rotation;
		
		camera.playerPosition.set(parentPos);
		camera.playerPosition.y += 3.8f;
		
		float yaw = camera.getYaw();
		
		if(Keyboard.isKey(Key.W) && Keyboard.isKey(Key.D)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw + 45)) * -1.0f * -speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw + 45))* -speed * dt;
			parentRot.y = -yaw - 45;
            move = true;
		} else if(Keyboard.isKey(Key.W) && Keyboard.isKey(Key.A)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw - 45)) * -1.0f * -speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw - 45))* -speed * dt;
			parentRot.y = -yaw + 45;
            move = true;
		} else if(Keyboard.isKey(Key.S) && Keyboard.isKey(Key.A)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw + 45)) * -1.0f * speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw + 45))* speed * dt;
			parentRot.y = -yaw - 45;
            move = true;
		} else if(Keyboard.isKey(Key.S) && Keyboard.isKey(Key.D)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw - 45)) * -1.0f * speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw - 45))* speed * dt;
			parentRot.y = -yaw + 45;
            move = true;
		} else if(Keyboard.isKey(Key.W)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw)) * -1.0f * -speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw))* -speed * dt;
			parentRot.y = -yaw;
            move = true;
		} else if(Keyboard.isKey(Key.S)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw)) * -1.0f * speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw))* speed * dt;
			parentRot.y = -yaw;
            move = true;
		} else if(Keyboard.isKey(Key.A)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw - 90)) * -1.0f * -speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw - 90))* -speed * dt;
			parentRot.y = -yaw + 90;
            move = true;
		} else if(Keyboard.isKey(Key.D)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw + 90)) * -1.0f * -speed * dt;;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw + 90))* -speed * dt;
			parentRot.y = -yaw - 90;
            move = true;
		}
		
		if(move) {
			anim.walkAnim(dt);	
		} else {
			anim.idleAnim(dt);
		}
	}
	
	@Override
	public void onGUI() {
		
	}

}
