package com.paleon.engine.instances;

import com.paleon.engine.Display;
import com.paleon.engine.input.Key;
import com.paleon.engine.input.Keyboard;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.processing.Frustum;
import com.paleon.engine.toolbox.MathUtils;
import com.paleon.maths.vecmath.Matrix4f;
import com.paleon.maths.vecmath.Vector3f;

public class Camera {
	
	public final float nearPlane = 0.1f;
	public final float farPlane = 1000f;
	public final float fieldOfView = 70f;
	
	private Frustum frustum;
	
	private static final float MIN_DISTANCE = 0;
	private static final float MAX_DISTANCE = 200;
	private float distanceFromPlayer = 60;
 	private float angleAroundPlayer = 180;
 	
 	private static final float MAX_PITCH = 90;
 	private static final float MIN_PITCH = -20;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 35;
	private float yaw = 0;
	private float roll;
	
	private float speed = 40;

	Vector3f playerPosition;
	
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	
	public Camera(Vector3f position){
		playerPosition = position;
		frustum = new Frustum(this);
		projectionMatrix = MathUtils.getPerspectiveProjectionMatrix(projectionMatrix, fieldOfView, 
				Display.getWidth(), Display.getHeight(), nearPlane, farPlane);
	}

	public void update(float deltaTime){	
		
		if(Mouse.isButton(2)) {
			calculateAngleAroundPlayer(deltaTime);
			calculatePitch(deltaTime);
		}
		calculateZoom();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - angleAroundPlayer;
		
		frustum.updatePlanes();
	}
	
	public void move(float dt) {
		playerPosition.y = 3.92f;
		
		float rot = -getYaw();
		
		if(Keyboard.isKey(Key.W) && Keyboard.isKey(Key.A)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot - 45)) * -1.0f * speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot - 45)) * speed * dt;
		} else if(Keyboard.isKey(Key.W) && Keyboard.isKey(Key.D)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot + 45)) * -1.0f * speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot + 45)) * speed * dt;
		} else if(Keyboard.isKey(Key.S) && Keyboard.isKey(Key.D)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot - 45)) * -1.0f * -speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot - 45)) * -speed * dt;
		} else if(Keyboard.isKey(Key.S) && Keyboard.isKey(Key.A)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot + 45)) * -1.0f * -speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot + 45)) * -speed * dt;
		} else if(Keyboard.isKey(Key.W)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot)) * -1.0f * speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot)) * speed * dt;
		} else if(Keyboard.isKey(Key.S)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot)) * -1.0f * -speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot)) * -speed * dt;
		} else if(Keyboard.isKey(Key.A)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot - 90)) * -1.0f * speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot - 90)) * speed * dt;
		} else if(Keyboard.isKey(Key.D)) {
			playerPosition.x -= (float)Math.sin(Math.toRadians(-rot + 90)) * -1.0f * speed * dt;
			playerPosition.z -= (float)Math.cos(Math.toRadians(-rot + 90)) * speed * dt;
		}
	}
	
	public void updateProjectionMatrix() {
		projectionMatrix = MathUtils.getPerspectiveProjectionMatrix(projectionMatrix, fieldOfView, 
				Display.getWidth(), Display.getHeight(), nearPlane, farPlane);
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
	}

	public Vector3f getPosition(){
		return position;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getZ() {
		return position.z;
	}
	
	public void setPosition(Vector3f position){
		this.playerPosition = position;
	}
	
	public void setPosition(float x, float y, float z){
		this.playerPosition.set(x, y, z);
	}

	public float getPitch(){
		return pitch;
	}

	public float getYaw(){
		return yaw;
	}

	public float getRoll(){
		return roll;
	}

	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float theta = angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = playerPosition.x - offsetX;
		position.z = playerPosition.z - offsetZ;
		position.y = playerPosition.y + verticDistance;
	}

	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	

	private void calculateAngleAroundPlayer(float dt){
		float angleChange = Mouse.Cursor.getDX() * 20 * dt;
		angleAroundPlayer -= angleChange;
	}
	
	private void calculatePitch(float dt){
  		float pitchChange = -Mouse.Cursor.getDY() * 10 * dt;
  		pitch -= pitchChange;
 		if(pitch >= MAX_PITCH){
 			pitch = MAX_PITCH;
  		} else if(pitch <= MIN_PITCH){
  			pitch = MIN_PITCH;
  		}
  	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getScroll();
		float temp = distanceFromPlayer;
		temp -= zoomLevel;
		if(temp >= MAX_DISTANCE){
			temp = MAX_DISTANCE;
		} else if(temp <= MIN_DISTANCE){
			temp = MIN_DISTANCE;
		}
		distanceFromPlayer = temp;
	}
	
	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}
	
	public Frustum getFrusutmCuller() {
		return frustum;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public Matrix4f getViewMatrix() {
		return MathUtils.getViewMatrix(viewMatrix, this);
	}
	
}
