package com.paleon.engine.graph.systems;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.graph.ShaderProgram;
import com.paleon.engine.instances.Camera;
import com.paleon.engine.toolbox.Color;
import com.paleon.engine.weather.Skybox;
import com.paleon.maths.vecmath.Matrix4f;
import com.paleon.maths.vecmath.Vector3f;

public class SkyboxRendererSystem {

	private ShaderProgram skyboxShader;
	
	private static final float ROTATE_SPEED = 0.5f;
	private float rotation = 0;
	
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionViewMatrix = new Matrix4f();
	
	public SkyboxRendererSystem(Camera camera) {
		skyboxShader = ResourceManager.loadShader("skybox");
		
		skyboxShader.createUniform("VP");
		skyboxShader.createUniform("sampler_cube_1");
		skyboxShader.createUniform("sampler_cube_2");
		skyboxShader.createUniform("blendFactor");
		skyboxShader.createUniform("fogColor");
		
		skyboxShader.setUniform("sampler_cube_1", 0, true);
		skyboxShader.setUniform("sampler_cube_2", 1, true);
	}
	
	public void update(float dt) {
		rotation += ROTATE_SPEED * dt;
	}
	
	public void render(Skybox skybox, Color fogColor, Camera camera) {		
		skyboxShader.bind();
		viewMatrix.load(camera.getViewMatrix());
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		skyboxShader.setUniform("VP", Matrix4f.mul(camera.getProjectionMatrix(), viewMatrix, projectionViewMatrix));
		skyboxShader.setUniform("fogColor", fogColor);
		skybox.render(skyboxShader);
		skyboxShader.unbind();
	}
	
	public void cleanup() {
		skyboxShader.cleanup();
	}
	
}
