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
		
		colPackage = new CollisionPacket(new Vector3f(1, 2, 1), new Vector3f(384, world.getTerrainHeight(384, 384) + 50.0f, 384));
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
		
		colPackage.setR3toESpaceVelocity(0, -0.2f, 0);
		eSpaceVelocity.set(colPackage.getVelocity());
		collisionRecursionDepth = 0;
		
		finalPosition = collideWithWorld(finalPosition, eSpaceVelocity);
		
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
		
		Vector3f destinationPoint = Vector3f.add(pos, vel, null);
		Vector3f newBasePoint = new Vector3f(pos);
		
		if(colPackage.nearestDistance >= veryCloseDistance) {
			Vector3f V = new Vector3f(vel);
			V.normalise();
			V.scale((float) (colPackage.nearestDistance - veryCloseDistance));
			newBasePoint = Vector3f.add(colPackage.getBasePoint(), V, null);
			
			V.normalise();
			Vector3f vcdV = new Vector3f(V);
			vcdV.scale(veryCloseDistance);
			Vector3f.sub(colPackage.intersectionPoint, vcdV, 
					colPackage.intersectionPoint);
		}
		
		Vector3f slidePlaneOrigin = new Vector3f(colPackage.intersectionPoint);
		Vector3f slidePlaneNormal = new Vector3f(
				Vector3f.sub(newBasePoint, colPackage.intersectionPoint, null));
		slidePlaneNormal.normalise();
		FPlane slidingPlane = new FPlane(slidePlaneOrigin, slidePlaneNormal);
		
		Vector3f newSlidePlaneNormal = new Vector3f(slidePlaneNormal);
		newSlidePlaneNormal.scale((float) slidingPlane.signedDistanceTo(destinationPoint));
		
		Vector3f newDestinationPoint = new Vector3f(
				Vector3f.sub(destinationPoint, newSlidePlaneNormal, null));
		
		Vector3f newVelocityVector = Vector3f.sub(newDestinationPoint, 
				colPackage.intersectionPoint, null);
		
		if(newVelocityVector.length() < veryCloseDistance) {
			return newBasePoint;
		}
		
		collisionRecursionDepth++;
		return collideWithWorld(newBasePoint, newVelocityVector);
	}

}
