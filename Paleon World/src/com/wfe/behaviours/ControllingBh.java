package com.wfe.behaviours;

import com.wfe.graph.Camera;
import com.wfe.input.Key;
import com.wfe.input.Keyboard;
import com.wfe.math.Vector3f;
import com.wfe.physics.CollisionPacket;
import com.wfe.physics.FPlane;
import com.wfe.scenegraph.World;

public class ControllingBh extends Behaviour {

	private static final Vector3f gravity = new Vector3f(0, -9.81f, 0);
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
		
		colPackage = new CollisionPacket(new Vector3f(1, 2, 1), new Vector3f(384, world.getTerrainHeight(384, 400) + 2.0f, 400));
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
		colPackage.setR3toESpaceVelocity(0, 0, 0);
		
		if(Keyboard.isKey(Key.W)) {
			colPackage.setR3toESpaceVelocity((float)Math.sin(Math.toRadians(yaw)) * -1.0f * -speed * dt, 
					0, (float)Math.cos(Math.toRadians(yaw))* -speed * dt);
			parent.rotation.y = -yaw;
			move = true;
		} else if(Keyboard.isKey(Key.S)) {
			colPackage.setR3toESpaceVelocity((float)Math.sin(Math.toRadians(yaw)) * -1.0f * speed * dt, 
					0, (float)Math.cos(Math.toRadians(yaw))* speed * dt);
			parent.rotation.y = -yaw;
			move = true;
		} else if(Keyboard.isKey(Key.A)) {
			colPackage.setR3toESpaceVelocity((float)Math.sin(Math.toRadians(yaw + 90)) * -1.0f * speed * dt, 
					0, (float)Math.cos(Math.toRadians(yaw + 90))* speed * dt);
			parent.rotation.y = -yaw + 90;
			move = true;
		} else if(Keyboard.isKey(Key.D)) {
			colPackage.setR3toESpaceVelocity((float)Math.sin(Math.toRadians(yaw - 90)) * -1.0f * speed * dt,
					0, (float)Math.cos(Math.toRadians(yaw - 90))* speed * dt);
			parent.rotation.y = -yaw - 90;
			move = true;
		}
		
		collideAndSlide();
		
		parent.position.set(colPackage.getR3Position());
		camera.playerPosition.set(parent.position);
		camera.playerPosition.y += 3.8f;
		
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
	
	private int collisionRecursionDepth = 0;
	public void collideAndSlide() {
		Vector3f eSpacePosition = new Vector3f();
		eSpacePosition.set(colPackage.getBasePoint());
			
		Vector3f eSpaceVelocity = new Vector3f();
		eSpaceVelocity.set(colPackage.getVelocity());
		
		collisionRecursionDepth = 0;
		
		Vector3f finalPosition = collideWithWorld(eSpacePosition, eSpaceVelocity);
		
		colPackage.setESpacePosition(finalPosition.x, finalPosition.y, finalPosition.z);
	}
	
	private static final float unitsPerMeter = 100.0f;
	
	public Vector3f collideWithWorld(Vector3f pos, Vector3f vel) {
		float unitScale = unitsPerMeter / 100.0f;
		float veryCloseDistance = 0.005f * unitScale;
		
		if(collisionRecursionDepth > 5)
			return pos;
		
		colPackage.setESpaceVelocity(vel.x, vel.y, vel.z);
		colPackage.setESpacePosition(pos.x, pos.y, pos.z);
		colPackage.foundCollision = false;
		
		world.checkCollision(colPackage);
		
		if(colPackage.foundCollision == false) {
			return Vector3f.add(pos, vel, null);
		}
		
		return pos;
	}

}
