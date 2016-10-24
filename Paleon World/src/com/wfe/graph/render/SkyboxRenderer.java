package com.wfe.graph.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.wfe.core.Display;
import com.wfe.core.ResourceManager;
import com.wfe.graph.Camera;
import com.wfe.graph.Mesh;
import com.wfe.graph.shaders.ShaderProgram;
import com.wfe.math.Matrix4f;

/**
 * Created by Rick on 11.10.2016.
 */
public class SkyboxRenderer {

    private Mesh mesh;

    private static final float SIZE = 500.0f;

    private static final float[] VERTICES = {
            -SIZE, SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, -SIZE,
            -SIZE, SIZE, SIZE,
            -SIZE, -SIZE, SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE, SIZE,
            -SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, SIZE, SIZE,
            SIZE, -SIZE, SIZE,
            -SIZE, -SIZE, SIZE,
            
            -SIZE,  SIZE, -SIZE,
   	     	SIZE,  SIZE, -SIZE,
   	     	SIZE,  SIZE,  SIZE,
   	     	SIZE,  SIZE,  SIZE,
   	     	-SIZE,  SIZE,  SIZE,
   	     	-SIZE,  SIZE, -SIZE,
    };

    private ShaderProgram shader;

    private Camera camera;
    
    private Matrix4f viewMatrix;

    int skyBoxTexture;

    public SkyboxRenderer(Camera camera) {
        this.camera = camera;

        mesh = new Mesh(VERTICES, 3);

        shader = ResourceManager.loadShader("skybox");

        shader.createUniform("projection");
        shader.createUniform("view");

        shader.createUniform("cubeMap");

        shader.setUniform("projection", this.camera.getProjectionMatrix(), true);
        shader.setUniform("cubeMap", 0, true);
        
        viewMatrix = new Matrix4f();

        skyBoxTexture = ResourceManager.getSkybox("sunny");
    }

    public void render(Camera camera) {
        if(Display.wasResized()) {
            shader.setUniform("projection", this.camera.getProjectionMatrix(), true);
        }

        shader.bind();
        Matrix4f.load(camera.getViewMatrix(), viewMatrix);
        viewMatrix.m30 = 0;
        viewMatrix.m31 = 0;
        viewMatrix.m32 = 0;
        shader.setUniform("view", viewMatrix);
        GL30.glBindVertexArray(mesh.getVAO());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, skyBoxTexture);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void cleanup() {
        mesh.cleanup();
        shader.cleanup();
    }

}
