package com.paleon.engine.graph.systems;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import com.paleon.engine.Display;
import com.paleon.engine.ResourceManager;
import com.paleon.engine.graph.ShaderProgram;
import com.paleon.engine.instances.Camera;
import com.paleon.engine.instances.Light;
import com.paleon.engine.processing.LodCalculator;
import com.paleon.engine.terrain.Terrain;
import com.paleon.engine.terrain.TerrainBlock;
import com.paleon.engine.terrain.TexturePack;
import com.paleon.engine.toolbox.Color;
import com.paleon.engine.toolbox.MathUtils;
import com.paleon.engine.toolbox.OpenglUtils;
import com.paleon.maths.vecmath.Matrix4f;
import com.paleon.maths.vecmath.Vector3f;
import com.paleon.maths.vecmath.Vector4f;

public class TerrainRendererSystem {

	private ShaderProgram shader;
	
	private Matrix4f modelMatrix = new Matrix4f();
	
	public TerrainRendererSystem(Camera camera) {
		shader = ResourceManager.loadShader("terrain");
		
		shader.createUniform("modelMatrix");
		shader.createUniform("viewMatrix");
		shader.createUniform("projectionMatrix");
		
		shader.createUniform("blendMap");
		shader.createUniform("aTexture");
		shader.createUniform("rTexture");
		shader.createUniform("gTexture");
		shader.createUniform("bTexture");
		
		shader.createUniform("lightPosition");
		shader.createUniform("lightColor");
		shader.createUniform("fogColor");
		
		shader.createUniform("plane");
		
		shader.setUniform("blendMap", 0, true);
		shader.setUniform("aTexture", 1, true);
		shader.setUniform("rTexture", 2, true);
		shader.setUniform("gTexture", 3, true);
		shader.setUniform("bTexture", 4, true);
		shader.setUniform("projectionMatrix", camera.getProjectionMatrix(), true);
	}

	public void render(Map<Terrain, List<TerrainBlock>> terrainBatches, Light light, Color fogColor, Vector4f plane, Camera camera) {
		if(Display.wasResized()) {
			shader.setUniform("projectionMatrix", camera.getProjectionMatrix(), true);
		}
		
		shader.bind();
		shader.setUniform("plane", plane);
		render(terrainBatches, light, fogColor, camera);
		shader.unbind();
	}
	
	public void render(Map<Terrain, List<TerrainBlock>> terrainBatches, Light light, Color fogColor, Camera camera) {
		for (List<TerrainBlock> blocks : terrainBatches.values()) {
			for (TerrainBlock block : blocks) {
				block.setStitching();
				LodCalculator.calculateTerrainLOD(block, camera);
			}
		}
		
		OpenglUtils.cullFace(true);
		for(Terrain terrain : terrainBatches.keySet()) {
			prepareTerrainInstance(terrain, camera, light, fogColor);
			List<TerrainBlock> batch = terrainBatches.get(terrain);
			for(TerrainBlock terrainBlock : batch) {
				if(camera.getFrusutmCuller().testTerrainInView(terrainBlock)) {
					int[] indexInfo = terrainBlock.getIndicesVBOInfo();
					OpenglUtils.bindIndicesVBO(indexInfo[0]);
					render(terrainBlock.getIndex(), indexInfo[1]);
					OpenglUtils.unbindIndicesVBO();
				}
			}
			OpenglUtils.unbindVAO();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
	}
	
	private void prepareTerrainInstance(Terrain terrain, Camera camera, Light light, Color fogColor) {
		shader.setUniform("viewMatrix", camera.getViewMatrix());
		shader.setUniform("lightPosition", light.getPosition());
		shader.setUniform("lightColor", light.getDiffuse());
		shader.setUniform("fogColor", fogColor);
		
		shader.setUniform("modelMatrix", MathUtils.getModelMatrix(modelMatrix, new Vector3f(terrain.getX(), 0, terrain.getZ()), 
				0, 0, 0, 1));
		
		TexturePack texturePack = terrain.getTexture();
		texturePack.getBlendMap().bind(0);
		texturePack.getaTexture().bind(1);
		texturePack.getrTexture().bind(2);
		texturePack.getgTexture().bind(3);
		texturePack.getbTexture().bind(4);
		
		OpenglUtils.bindVAO(terrain.getVaoId());
	}
	
	private void render(int blockIndex, int indicesLength) {
		int vertexOffset = blockIndex * Terrain.VERTICES_PER_NODE;
		GL32.glDrawElementsBaseVertex(GL11.GL_TRIANGLES, indicesLength, GL11.GL_UNSIGNED_INT, 0, vertexOffset);
	}
	
	public void cleanup() {
		shader.cleanup();
	}
	
}
