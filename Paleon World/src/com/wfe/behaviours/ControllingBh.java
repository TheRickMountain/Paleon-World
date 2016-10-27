package com.wfe.behaviours;

import com.wfe.graph.Camera;
import com.wfe.input.Key;
import com.wfe.input.Keyboard;
import com.wfe.math.Vector3f;
import com.wfe.physics.CollisionPacket;
import com.wfe.physics.FPlane;
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
	
	CollisionPacket colPackage;
	
	public ControllingBh(Camera camera) {
		this.camera = camera;
	}
	
	@Override
	public void start() {
		this.anim = parent.getBehaviour(AnimBh.class);
		this.world = parent.getWorld();
		
		colPackage = new CollisionPacket(new Vector3f(3, 7, 3), new Vector3f(384, world.getTerrainHeight(384, 384), 384));
	}

	@Override
	public void update(float dt) {	
		moving(dt);
		
		/*Vector3f parentPos = parent.position;
		upwardSpeed += GRAVITY * dt;
		parentPos.y += upwardSpeed * dt;
		float terrainHeight = world.getTerrainHeight(parentPos.x, parentPos.z) + 2.2f;
		if(parentPos.y < terrainHeight) {
			upwardSpeed = 0;
			isInAir = false;
			parentPos.y = terrainHeight;
		}*/
	}
	
	public void jump() {
		if(!isInAir) {
			this.upwardSpeed = JUMP_POWER;
			isInAir = true;
		}
	}

	public void moving(float dt) {
		move = false;
		
		float yaw = camera.getYaw();
		
		if(Keyboard.isKey(Key.W)) {
			colPackage.setVelocity((float)Math.sin(Math.toRadians(yaw)) * -1.0f * -speed * dt, 
					0, (float)Math.cos(Math.toRadians(yaw))* -speed * dt);
			parent.rotation.y = -yaw;
			move = true;
		} else if(Keyboard.isKey(Key.S)) {
			colPackage.setVelocity((float)Math.sin(Math.toRadians(yaw)) * -1.0f * speed * dt, 
					0, (float)Math.cos(Math.toRadians(yaw))* speed * dt);
			parent.rotation.y = -yaw;
			move = true;
		} else if(Keyboard.isKey(Key.A)) {
			colPackage.setVelocity((float)Math.sin(Math.toRadians(yaw + 90)) * -1.0f * speed * dt, 
					0, (float)Math.cos(Math.toRadians(yaw + 90))* speed * dt);
			parent.rotation.y = -yaw + 90;
			move = true;
		} else if(Keyboard.isKey(Key.D)) {
			colPackage.setVelocity((float)Math.sin(Math.toRadians(yaw - 90)) * -1.0f * speed * dt,
					0, (float)Math.cos(Math.toRadians(yaw - 90))* speed * dt);
			parent.rotation.y = -yaw - 90;
			move = true;
		}
		
		colPackage.update();
		
		parent.position.set(colPackage.getR3Position());
		camera.playerPosition.set(parent.position);
		camera.playerPosition.y += 3.8f;
		
		//world.checkCollision(colPackage);
		
		/*if(Keyboard.isKeyDown(Key.SPACE)) {
			jump();
		}*/
		
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
