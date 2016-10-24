package com.wfe.graph.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.wfe.core.Display;
import com.wfe.core.ResourceManager;
import com.wfe.graph.Camera;
import com.wfe.graph.Mesh;
import com.wfe.graph.shaders.ShaderProgram;
import com.wfe.graph.water.WaterFrameBuffers;
import com.wfe.graph.water.WaterTile;
import com.wfe.math.Matrix4f;
import com.wfe.math.Vector3f;
import com.wfe.utils.MathUtils;

public class WaterRenderer {

	private Camera camera;
	
	private Mesh quad;
	private ShaderProgram shader;
	private Matrix4f modelMatrix;
	
	private WaterFrameBuffers fbos;

	public WaterRenderer(Camera camera, WaterFrameBuffers fbos) {
		this.fbos = fbos;
		
		this.camera = camera;
		
		this.shader = ResourceManager.loadShader("water");
		
		shader.createUniform("projectionMatrix");
		shader.createUniform("modelMatrix");
		shader.createUniform("viewMatrix");
		
		shader.createUniform("reflectionTexture");
		shader.createUniform("refractionTexture");
		
		shader.setUniform("reflectionTexture", 0, true);
		shader.setUniform("refractionTexture", 1, true);
		
		setUpVAO();
		
		modelMatrix = new Matrix4f();
		
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix(), true);
	}

	public void render(List<WaterTile> water) {
		prepareRender();	
		for (WaterTile tile : water) {
			MathUtils.getEulerModelMatrix(modelMatrix,
					new Vector3f(tile.getX(), WaterTile.HEIGHT, tile.getZ()), new Vector3f(0, 0, 0),
					WaterTile.TILE_SIZE);
			shader.setUniform("modelMatrix", modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(){
		shader.bind();
		
		if(Display.wasResized()) {
			shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
		}
		
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		GL30.glBindVertexArray(quad.getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.unbind();
	}

	private void setUpVAO() {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = new Mesh(vertices, 2);
	}
	
	public void cleanup() {
		quad.cleanup();
		shader.cleanup();
	}

}
