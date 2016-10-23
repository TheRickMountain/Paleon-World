package com.paleon.engine.graph.systems;

import java.io.File;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.paleon.engine.Display;
import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.Image;
import com.paleon.engine.components.Text;
import com.paleon.engine.graph.Mesh;
import com.paleon.engine.graph.ShaderProgram;
import com.paleon.engine.graph.Texture;
import com.paleon.engine.graph.font.FontType;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.toolbox.Color;
import com.paleon.engine.toolbox.MathUtils;
import com.paleon.engine.toolbox.OpenglUtils;
import com.paleon.engine.toolbox.Rect;
import com.paleon.maths.vecmath.Matrix4f;

public class GUIRendererSystem {

	public static ShaderProgram shader;
	
	public static Mesh mesh;
	
	public static Matrix4f projectionMatrix = new Matrix4f();
	public static Matrix4f modelMatrix = new Matrix4f();
	public Matrix4f modelProjectionMatrix = new Matrix4f();
	
	public static FontType primitiveFont;
	
	public GUIRendererSystem() {
		initRenderData();
		
		shader = ResourceManager.loadShader("gui");
		
		shader.createUniform("spriteColor");
		shader.createUniform("image");
		shader.createUniform("mode");
		shader.createUniform("MP");

		shader.setUniform("image", 0);

		MathUtils.getOrtho2DProjectionMatrix(projectionMatrix, 0, Display.getWidth(), Display.getHeight(), 0);
		
		primitiveFont = new FontType(ResourceManager.getTexture("primitive_font"), new File("res/primitive_font.fnt"));
	}
	
	public void startRender() {
		OpenglUtils.alphaBlending(true);
		OpenglUtils.depthTest(false);
		
		if(Display.wasResized()) {
			MathUtils.getOrtho2DProjectionMatrix(projectionMatrix, 0, Display.getWidth(), Display.getHeight(), 0);
		}

		shader.bind();
	}
	
	public void render(List<Entity> gameObjects) {		
		for(Entity gameObject : gameObjects) {
			
			if(gameObject.isActive()) {
				
				Image image = gameObject.getComponent(Image.class);
				if(image != null) {
					if(image.enabled) {
						Texture textureId = image.texture;
						Color color = image.color;
						
						shader.setUniform("spriteColor", color);
						shader.setUniform("MP", 
								Matrix4f.mul(projectionMatrix, 
										MathUtils.getModelMatrix(modelMatrix, gameObject.position.x,
												gameObject.position.y, gameObject.rotation.z, gameObject.scale.x, gameObject.scale.y), 
										modelProjectionMatrix));
						
						if(textureId == null) {
							shader.setUniform("mode", 1);
						} else {
							textureId.bind(0);
							shader.setUniform("mode", 0);
						}
						
						GL30.glBindVertexArray(mesh.getVAO());
						GL20.glEnableVertexAttribArray(0);
						GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
						GL20.glDisableVertexAttribArray(0);
						GL30.glBindVertexArray(0);
					}
				}
				
				Text text = gameObject.getComponent(Text.class);
				if(text != null) {
					if(text.enabled) {
						text.font.getTextureAtlas().bind(0);
			        	
			        	shader.setUniform("mode", 2);
			        	
			        	shader.setUniform("MP", 
			        			MathUtils.getModelMatrix(modelMatrix, (gameObject.position.x / Display.getWidth()) * 2.0f, 
			        					(-gameObject.position.y / Display.getHeight()) * 2.0f, 
										gameObject.rotation.z, 
										gameObject.scale.x, gameObject.scale.y));
			        	
			        	shader.setUniform("spriteColor", text.color);
			        	
			            GL30.glBindVertexArray(text.mesh.getVAO());
			            GL20.glEnableVertexAttribArray(0);
			            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.mesh.getVertexCount());
			            GL20.glDisableVertexAttribArray(0);
			            GL30.glBindVertexArray(0);
					}
				}
				
			}
		}
	}
	
	public void endRender() {
		shader.unbind();
		
		OpenglUtils.alphaBlending(false);
		OpenglUtils.depthTest(true);
	}
	
	public static void render(Rect rect, Texture textureId) {
		render(rect.x, rect.y, rect.width, rect.height, textureId);
	}
	
	public static void render(float x, float y, float width, float height, Texture textureId) {
		shader.setUniform("spriteColor", Color.WHITE);
		shader.setUniform("MP", 
				Matrix4f.mul(projectionMatrix, 
						MathUtils.getModelMatrix(modelMatrix, x, y, 
								0, width, height), null));
		
		if(textureId == null) {
			shader.setUniform("mode", 1);
		} else {
			textureId.bind(0);
			shader.setUniform("mode", 0);
		}
		
		GL30.glBindVertexArray(mesh.getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public static void render(Rect rect, Text text) {
		render(rect.x, rect.y, text);
	}
	
	public static void render(float x, float y, Text text) {
		text.font.getTextureAtlas().bind(0);
    	
    	shader.setUniform("mode", 2);
    	
    	shader.setUniform("MP", 
    			MathUtils.getModelMatrix(modelMatrix, (x / Display.getWidth()) * 2.0f, 
    					(-y / Display.getHeight()) * 2.0f, 0, 1, 1));
    	
    	shader.setUniform("spriteColor", text.color);
    	
        GL30.glBindVertexArray(text.mesh.getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.mesh.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
	}
	
	private void initRenderData() {
		float[] data = new float[] {
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 1.0f, 1.0f
		};
		
		mesh = new Mesh(data, 4);
	}
	
	public void cleanup() {
		shader.cleanup();
	}
	
}
