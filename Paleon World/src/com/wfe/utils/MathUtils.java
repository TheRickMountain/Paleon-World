package com.wfe.utils;

import com.wfe.graph.Camera;
import com.wfe.math.Matrix4f;
import com.wfe.math.Vector2f;
import com.wfe.math.Vector3f;
import com.wfe.math.altvecmath.Matrix4;
import com.wfe.math.altvecmath.Transform;
import com.wfe.math.altvecmath.Vector3;

public class MathUtils {
	
	public static final float PI = 3.1415927f;
	public static final float RADIANS_TO_DEGREES = 180f / PI;
	public static final float DEGREES_TO_RADIANS = PI / 180;
	
	public static final Vector3f AXIS_X = new Vector3f(1, 0, 0);
    public static final Vector3f AXIS_Y = new Vector3f(0, 1, 0);
    public static final Vector3f AXIS_Z = new Vector3f(0, 0, 1);
    public static final Vector3f ZERO = new Vector3f(0, 0, 0);
    public static final Vector3f IDENTITY = new Vector3f(1, 1, 1);
	
	public static Matrix4f getModelMatrix(Matrix4f matrix, float xPos, float yPos, float rotation, float xScale, float yScale){
		matrix.setIdentity();
		Matrix4f.translate(new Vector2f(xPos, yPos), matrix, matrix);
        Matrix4f.translate(new Vector2f(0.5f * xScale, 0.5f * yScale), matrix, matrix);
		Matrix4f.rotate(rotation* DEGREES_TO_RADIANS, AXIS_Z, matrix, matrix);
		Matrix4f.translate(new Vector2f(-0.5f * xScale, -0.5f * yScale), matrix, matrix);
		Matrix4f.scale(new Vector3f(xScale, yScale, 0.0f), matrix, matrix);
        
		return matrix;
	}
	
	public static Matrix4f getModelMatrix(Matrix4f matrix, Vector3f offset, Vector3f rotation, Vector3f scale){
		matrix.setIdentity();
		Matrix4f.translate(offset, matrix, matrix);
		Matrix4f.rotate(rotation.x * DEGREES_TO_RADIANS, AXIS_X, matrix, matrix);
		Matrix4f.rotate(rotation.y * DEGREES_TO_RADIANS, AXIS_Y, matrix, matrix);
		Matrix4f.rotate(rotation.z * DEGREES_TO_RADIANS, AXIS_Z, matrix, matrix);
		Matrix4f.scale(scale, matrix, matrix);
		return matrix;
	}
	
	static Transform transform = new Transform();
	
	public static Matrix4f getEulerModelMatrix(Matrix4f matrix, Vector3f position, Vector3f rotation, float scale) {
		transform.reset()
        .rotateSelf(Vector3.AXIS_X, rotation.x * DEGREES_TO_RADIANS)
        .rotateSelf(Vector3.AXIS_Z, rotation.z * DEGREES_TO_RADIANS)
        .rotateSelf(Vector3.AXIS_Y, rotation.y * DEGREES_TO_RADIANS)
        .scaleSelf(new Vector3(scale, scale, scale))
        .translateSelf(new Vector3(position.x, position.y, position.z));
		Matrix4 tempMatrix = transform.getMatrix();
		matrix.m00 = tempMatrix.get(0, 0);
		matrix.m01 = tempMatrix.get(0, 1);
		matrix.m02 = tempMatrix.get(0, 2);
		matrix.m03 = tempMatrix.get(0, 3);
		matrix.m10 = tempMatrix.get(1, 0);
		matrix.m11 = tempMatrix.get(1, 1);
		matrix.m12 = tempMatrix.get(1, 2);
		matrix.m13 = tempMatrix.get(1, 3);
		matrix.m20 = tempMatrix.get(2, 0);
		matrix.m21 = tempMatrix.get(2, 1);
		matrix.m22 = tempMatrix.get(2, 2);
		matrix.m23 = tempMatrix.get(2, 3);
		matrix.m30 = tempMatrix.get(3, 0);
		matrix.m31 = tempMatrix.get(3, 1);
		matrix.m32 = tempMatrix.get(3, 2);
		matrix.m33 = tempMatrix.get(3, 3);
		return matrix;
    }
	
	public static Matrix4f getPerspProjectionMatrix(Matrix4f matrix, float fov, float width, float height, float zNear, float zFar){
		float aspectRatio = (float) width/ (float)height;
		float y_scale = (float) ((1f / Math.tan((fov / 2) * DEGREES_TO_RADIANS)) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = zFar - zNear;
		
		matrix.m00 = x_scale;
		matrix.m11 = y_scale;
		matrix.m22 = -((zFar + zNear) / frustum_length);
		matrix.m23 = -1;
		matrix.m32 = -((2 * zFar * zNear) / frustum_length);
		matrix.m33 = 0;
		
		return matrix; 
	}
	
	public static Matrix4f getOrtho2DProjectionMatrix(Matrix4f matrix, float left, float right, float bottom, float top) {
        matrix.m00 = 2.0f / (right - left);
        matrix.m11 = 2.0f / (top - bottom);
        matrix.m22 = -1.0f;
        matrix.m30 = -(right + left) / (right - left);
        matrix.m31 = -(top + bottom) / (top - bottom);
        return matrix;
	}
	
	public static Matrix4f getViewMatrix(Matrix4f matrix, Camera camera) {
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), AXIS_X, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), AXIS_Y, matrix, matrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, matrix, matrix);
		return matrix;
	}
	
	public static float getDistanceBetweenPoints(float x1, float y1, float x2, float y2) {
		float dX = x1 - x2;
		float dY = y1 - y2;
		float distance = (float) Math.sqrt((dX * dX) + (dY * dY));
		return distance;
	}

}
