package com.paleon.engine.components;

import com.paleon.engine.utils.OpenglUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

/**
 * Created by Rick on 06.10.2016.
 */
public class Mesh {

    private int VAO;
    private int VBO;
    private int EBO;

    private int vertexCount;

    public Mesh(float[] vertices, float[] uvs, float[] normals, int[] triangles) {
        set(vertices, uvs, normals, triangles);
    }

    public Mesh(float[] vertices, int dimension) {
        set(vertices, dimension);
    }

    public void set(float[] vertices, float[] uvs, float[] normals, int[] triangles) {
        cleanup();

        VAO = OpenglUtils.createVAO();

        vertexCount = triangles.length;

        int vertexByteCount = 4 * (3 + 2 + 3);

        VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, OpenglUtils.dataToFloatBuffer(vertices, uvs, normals), GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, vertexByteCount, 0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, vertexByteCount, 4 * 3);
        GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, vertexByteCount, 4 * (3 + 2));

        EBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, OpenglUtils.dataToIntBuffer(triangles), GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void set(float[] vertices, int dimension) {
        cleanup();

        VAO = OpenglUtils.createVAO();

        vertexCount = vertices.length / dimension;

        VBO = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, OpenglUtils.dataToFloatBuffer(vertices), GL15.GL_STATIC_DRAW);

        int vertexByteCount = 4 * dimension;

        GL20.glVertexAttribPointer(0, dimension, GL11.GL_FLOAT, false, vertexByteCount, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public int getVAO() {
        return VAO;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void startRender() {
        GL30.glBindVertexArray(VAO);

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    public void render() {
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
    }

    public void endRender() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        GL20.glDisableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(VBO);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(VAO);
    }

}
