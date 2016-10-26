package com.wfe.behaviours;

import com.wfe.graph.Camera;
import com.wfe.input.Key;
import com.wfe.input.Keyboard;
import com.wfe.math.Vector3f;
import com.wfe.scenegraph.World;

public class ControllingBh extends Behaviour {

	private static final float GRAVITY = -50.0f;
	private static final float JUMP_POWER = 20.0f;
	private float upwardSpeed = 0;
	private boolean isInAir = false;
	public float speed = 15.0f;
	
	private AnimBh anim;
	
	private boolean move = false;
	
	private Camera camera;
	
	private World world;
	
	public Vector3f direction;
	
	public ControllingBh(Camera camera) {
		this.camera = camera;
	}
	
	@Override
	public void start() {
		this.anim = parent.getBehaviour(AnimBh.class);
		this.world = parent.getWorld();
		direction = new Vector3f(0, 0, 0);
	}

	@Override
	public void update(float dt) {	
		moving(dt);
		
		Vector3f parentPos = parent.position;
		upwardSpeed += GRAVITY * dt;
		parentPos.y += upwardSpeed * dt;
		float terrainHeight = world.getTerrainHeight(parentPos.x, parentPos.z) + 2.2f;
		if(parentPos.y < terrainHeight) {
			upwardSpeed = 0;
			isInAir = false;
			parentPos.y = terrainHeight;
		}
	}
	
	public void jump() {
		if(!isInAir) {
			this.upwardSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	public void moving(float dt) {
		move = false;
		
		Vector3f parentPos = parent.position;
		Vector3f parentRot = parent.rotation;
		
		camera.playerPosition.set(parentPos);
		camera.playerPosition.y += 3.8f;
		
		float yaw = camera.getYaw();
		
		if(Keyboard.isKey(Key.W) && Keyboard.isKey(Key.D)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw + 45)) * -1.0f * -speed * dt;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw + 45))* -speed * dt;
			parentRot.y = -yaw - 45;
            move = true;
		} else if(Keyboard.isKey(Key.W) && Keyboard.isKey(Key.A)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw - 45)) * -1.0f * -speed * dt;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw - 45))* -speed * dt;
			parentRot.y = -yaw + 45;
            move = true;
		} else if(Keyboard.isKey(Key.S) && Keyboard.isKey(Key.A)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw + 45)) * -1.0f * speed * dt;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw + 45))* speed * dt;
			parentRot.y = -yaw - 45;
            move = true;
		} else if(Keyboard.isKey(Key.S) && Keyboard.isKey(Key.D)) {
			parentPos.x += (float)Math.sin(Math.toRadians(yaw - 45)) * -1.0f * speed * dt;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw - 45))* speed * dt;
			parentRot.y = -yaw + 45;
            move = true;
		} else if(Keyboard.isKey(Key.W)) {	
			parentPos.x += (float)Math.sin(Math.toRadians(yaw)) * -1.0f * -speed * dt;
			parentPos.z += (float)Math.cos(Math.toRadians(yaw))* -speed * dt;
			parentRot.y = -yaw;
			direction.set((float)Math.sin(Math.toRadians(yaw)) * -1.0f * -speed * dt, 0,
					(float)Math.cos(Math.toRadians(yaw))* -speed * dt);
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
		
		if(Keyboard.isKeyDown(Key.SPACE)) {
			jump();
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
