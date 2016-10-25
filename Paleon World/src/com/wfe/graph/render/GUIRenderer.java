package com.wfe.graph.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.wfe.components.Component;
import com.wfe.components.Image;
import com.wfe.components.Text;
import com.wfe.core.Display;
import com.wfe.core.ResourceManager;
import com.wfe.graph.Mesh;
import com.wfe.graph.Texture;
import com.wfe.graph.font.FontType;
import com.wfe.graph.shaders.ShaderProgram;
import com.wfe.math.Matrix4f;
import com.wfe.scenegraph.Entity;
import com.wfe.utils.Color;
import com.wfe.utils.MathUtils;
import com.wfe.utils.OpenglUtils;

import java.util.List;

/**
 * Created by Rick on 12.10.2016.
 */
public class GUIRenderer {

    public static ShaderProgram shader;

    public static Mesh mesh;

    public Matrix4f projectionMatrix = new Matrix4f();
    public Matrix4f modelMatrix = new Matrix4f();
    public Matrix4f modelProjectionMatrix = new Matrix4f();

    public static FontType primitiveFont;

    public GUIRenderer() {
        initRenderData();

        shader = ResourceManager.loadShader("gui");

        shader.createUniform("spriteColor");
        shader.createUniform("image");
        shader.createUniform("mode");
        shader.createUniform("MP");

        shader.setUniform("image", 0);

        MathUtils.getOrtho2DProjectionMatrix(projectionMatrix, 0, Display.getWidth(), Display.getHeight(), 0);

        primitiveFont = new FontType("primitive_font");
    }

    public void startRender() {
        OpenglUtils.alphaBlending(true);
        OpenglUtils.depthTest(false);

        if(Display.wasResized()) {
            MathUtils.getOrtho2DProjectionMatrix(projectionMatrix, 0, Display.getWidth(), Display.getHeight(), 0);
        }
    }

    public void render(List<Component> guis) {
        shader.bind();

        startRender();

        for(Component gui : guis) {
        	if(gui.active) {
	            if(gui.type.equals(Component.Type.IMAGE)) {
	                Image image = (Image) gui;
	                GL30.glBindVertexArray(mesh.getVAO());
	                GL20.glEnableVertexAttribArray(0);
	
	                Texture texture = image.texture;
	                Color color = image.color;
	                Entity entity = image.parent;
	
	                shader.setUniform("spriteColor", color);
	                Matrix4f.mul(projectionMatrix, entity.getTransform().getModelMatrix(), modelProjectionMatrix);
	                shader.setUniform("MP", modelProjectionMatrix);
	
	                if (texture != null) {
	                    texture.bind(0);
	                    shader.setUniform("mode", 0);
	                } else {
	                    shader.setUniform("mode", 1);
	                }
	                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	
	                GL20.glDisableVertexAttribArray(0);
	                GL30.glBindVertexArray(0);
	            }
	
	            if(gui.type.equals(Component.Type.TEXT)) {
	                Text text = (Text) gui;
	                Entity parent = text.parent;
	                text.font.getTextureAtlas().bind(0);
	
	                shader.setUniform("mode", 2);
	
	                shader.setUniform("MP",
	                        MathUtils.getModelMatrix(modelMatrix, (parent.position.x / Display.getWidth()) * 2.0f,
	                                (-parent.position.y / Display.getHeight()) * 2.0f,
	                                parent.rotation.z,
	                                parent.scale.x, parent.scale.y));
	
	                shader.setUniform("spriteColor", text.color);
	
	                GL30.glBindVertexArray(text.mesh.getVAO());
	                GL20.glEnableVertexAttribArray(0);
	                GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.mesh.getVertexCount());
	                GL20.glDisableVertexAttribArray(0);
	                GL30.glBindVertexArray(0);
	            }
        	}

        }

        endRender();

        shader.unbind();
    }

    public void endRender() {
        OpenglUtils.alphaBlending(false);
        OpenglUtils.depthTest(true);
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
        mesh.cleanup();
    }

}