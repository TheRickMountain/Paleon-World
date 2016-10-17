package com.paleon.engine.utils;

import com.paleon.engine.graph.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Rick on 15.10.2016.
 */
public class MathUtils {

    public static final float PI = 3.1415927f;
    public static final float RADIANS_TO_DEGREES = 180f / PI;
    public static final float DEGREES_TO_RADIANS = PI / 180;

    public static Matrix4f getOrthoProjectionMatrix(Matrix4f matrix, float left, float right, float bottom, float top) {
        matrix.identity();
        matrix.m00(2.0f / (right - left));
        matrix.m11(2.0f / (top - bottom));
        matrix.m22(-1.0f);
        matrix.m30(-(right + left) / (right - left));
        matrix.m31(-(top + bottom) / (top - bottom));
        return matrix;
    }

    public static Matrix4f getPerspectiveProjectionMatrix(Matrix4f matrix,
                                                          float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        matrix.identity();
        matrix.perspective(fov * DEGREES_TO_RADIANS, aspectRatio, zNear, zFar);
        return matrix;
    }

    public static Matrix4f getViewMatrix(Matrix4f matrix, Camera camera) {
        Vector3f cameraPos = new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
        matrix.identity();
        // First do the rotation so camera rotates over its position
        matrix.rotate(camera.getPitch() * DEGREES_TO_RADIANS, new Vector3f(1, 0, 0))
                .rotate(camera.getYaw() * DEGREES_TO_RADIANS, new Vector3f(0, 1, 0));
        // Then do the translation
        matrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return matrix;
    }

    public static Matrix4f getModelMatrix(Matrix4f matrix, float posX, float posY, float rotation, float scaleX, float scaleY) {
        matrix.identity();
        matrix.translate(new Vector3f(posX, posY, 0))
                .translate(new Vector3f(0.5f * scaleX, 0.5f * scaleY, 0))
                .rotateZ(rotation * DEGREES_TO_RADIANS)
                .translate(new Vector3f(-0.5f * scaleX, -0.5f * scaleY, 0))
                .scale(new Vector3f(scaleX, scaleY, 0.0f));
        return matrix;
    }

    public static Matrix4f getModelMatrix(Matrix4f matrix, Vector3f position,
                                          Vector3f rotation, Vector3f scale) {
        matrix.identity();
        matrix.translate(new Vector3f(position.x, position.y, position.z))
                .rotateX(rotation.x * DEGREES_TO_RADIANS)
                .rotateY(rotation.y * DEGREES_TO_RADIANS)
                .rotateZ(rotation.z * DEGREES_TO_RADIANS)
                .scale(new Vector3f(scale.x, scale.y, scale.z));
        return matrix;
    }

}

