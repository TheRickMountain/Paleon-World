package com.paleon.engine.utils;

import com.paleon.engine.core.Display;
import com.paleon.engine.graph.Camera;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.terrain.TerrainBlock;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MousePicker {

	private static Vector3f currentTerrainPoint;
	private static Vector3f currentRay = new Vector3f();

	private static final int RECURSION_COUNT = 21;
	private static final float RAY_RANGE = 600;

	private static Matrix4f projectionMatrix;
	private static Camera camera;
	
	private static World world;

	public static void setUpMousePicker(World w, Camera c) {
		world = w;
		camera = c;

		projectionMatrix = new Matrix4f();
		projectionMatrix = camera.getProjectionMatrix();
	}

	public static Vector3f getCurrentTerrainPoint() {
		if(currentTerrainPoint != null) {
			currentTerrainPoint.x += 1.5f;
			currentTerrainPoint.z += 1.5f;
			currentTerrainPoint.x /= 3;
			currentTerrainPoint.z /= 3;
			currentTerrainPoint.x = (int) Math.round(currentTerrainPoint.x);
			currentTerrainPoint.z = (int) Math.round(currentTerrainPoint.z);
			currentTerrainPoint.x *= 3;
			currentTerrainPoint.z *= 3;
			currentTerrainPoint.x -= 1.5f;
			currentTerrainPoint.z -= 1.5f;
		}
		return currentTerrainPoint;
	}

	public static Vector3f getCurrentRay() {
		return currentRay;
	}

	public static void update() {
		if(Display.wasResized()) {
			projectionMatrix = camera.getProjectionMatrix();
		}

		currentRay = calculateMouseRayCamera();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
		} else {
			currentTerrainPoint = null;
		}
	}

	public static Vector3f getPointOnYPlane(float height) {
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			float distance = (height - camera.getPosition().y) / (currentRay.y);
			return getPointOnRay(currentRay, distance);
		}
		return null;
	}

	private static Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			TerrainBlock terrain = world.getTerrainForPosition(endPoint.x,
					endPoint.z);
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) {
			return binarySearch(count + 1, start, half, ray);
		} else {
			return binarySearch(count + 1, half, finish, ray);
		}
	}

	public static boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isUnderGround(Vector3f testPoint) {
		TerrainBlock terrain = world.getTerrainForPosition(testPoint.x,
				testPoint.z);
		float height = 0;
		if (terrain != null) {
			height = world.getTerrainHeight(testPoint.x, testPoint.z);
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}

	public static Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f start = new Vector3f(camera.getPosition());
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
		return start.add(scaledRay, new Vector3f());
	}
	
	public static Vector3f getRayDirection(float range) {
		return getPointOnRay(currentRay, range);
	}
	
	public static Vector3f getRayOrigin() {
		return camera.getPosition();
	}

	private static Vector3f calculateMouseRayCamera() {
		Vector2f screenCoords = Mouse.getNDC();
		Vector4f clipCoords = new Vector4f(screenCoords.x, screenCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private static Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = new Matrix4f();
		camera.getViewMatrix().invert(invertedView);
		Vector4f rayWorld = new Vector4f();
		invertedView.transform(eyeCoords, rayWorld);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalize();
		return mouseRay;
	}

	private static Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = new Matrix4f();
		projectionMatrix.invert(invertedProjection);
		Vector4f eyeCoords = new Vector4f();
		invertedProjection.transform(clipCoords, eyeCoords);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

}
