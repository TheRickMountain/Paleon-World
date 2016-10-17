package com.paleon.engine.graph.render;

import com.paleon.engine.core.Display;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.Camera;
import com.paleon.engine.components.Mesh;
import com.paleon.engine.graph.shader.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
            -SIZE, -SIZE, SIZE
    };

    private ShaderProgram shader;

    private Camera camera;

    int skyBoxTexture;

    public SkyboxRenderer(Camera camera) {
        this.camera = camera;

        mesh = new Mesh(VERTICES, 3);

        ResourceManager.loadShader("skybox");
        shader = ResourceManager.getShader("skybox");

        shader.createUniform("projection");
        shader.createUniform("view");

        shader.createUniform("cubeMap");

        shader.setUniform("projection", this.camera.getProjectionMatrix(), true);
        shader.setUniform("cubeMap", 0, true);

        skyBoxTexture = ResourceManager.getSkybox("sunny");
    }

    public void render(Camera camera) {
        if(Display.wasResized()) {
            shader.setUniform("projection", this.camera.getProjectionMatrix(), true);
        }

        shader.bind();
        Matrix4f view = camera.getViewMatrix();
        view.m30(0);
        view.m31(0);
        view.m32(0);
        shader.setUniform("view", view);
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
