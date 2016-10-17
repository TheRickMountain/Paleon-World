package com.paleon.engine.graph.render;

import com.paleon.engine.core.Display;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.Camera;
import com.paleon.engine.graph.Light;
import com.paleon.engine.graph.processing.LodCalculator;
import com.paleon.engine.graph.shader.ShaderProgram;
import com.paleon.engine.terrain.Terrain;
import com.paleon.engine.terrain.TerrainBlock;
import com.paleon.engine.terrain.TexturePack;
import com.paleon.engine.utils.MathUtils;
import com.paleon.engine.utils.OpenglUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.util.List;
import java.util.Map;

/**
 * Created by Rick on 08.10.2016.
 */
public class TerrainRenderer {

    private ShaderProgram shader;

    private Matrix4f modelMatrix;

    private Camera camera;

    public TerrainRenderer(Camera camera) {
        this.camera = camera;

        ResourceManager.loadShader("terrain");
        shader = ResourceManager.getShader("terrain");

        shader.createUniform("model");
        shader.createUniform("view");
        shader.createUniform("projection");

        shader.createUniform("blendMap");
        shader.createUniform("aTexture");
        shader.createUniform("rTexture");
        shader.createUniform("gTexture");
        shader.createUniform("bTexture");

        shader.createUniform("lightPosition");
        shader.createUniform("lightColor");

        shader.setUniform("blendMap", 0, true);
        shader.setUniform("aTexture", 1, true);
        shader.setUniform("rTexture", 2, true);
        shader.setUniform("gTexture", 3, true);
        shader.setUniform("bTexture", 4, true);
        shader.setUniform("projection", this.camera.getProjectionMatrix(), true);

        modelMatrix = new Matrix4f();
    }

    public void render(Map<Terrain, List<TerrainBlock>> terrainBatches, Light light, Camera camera) {
        if(Display.wasResized()) {
            shader.setUniform("projection", this.camera.getProjectionMatrix(), true);
        }

        shader.bind();

        for (List<TerrainBlock> blocks : terrainBatches.values()) {
            for (TerrainBlock block : blocks) {
                block.setStitching();
                LodCalculator.calculateTerrainLOD(block, camera);
            }
        }

        OpenglUtils.cullFace(true);
        for(Terrain terrain : terrainBatches.keySet()) {
            prepareTerrainInstance(terrain, camera, light);
            List<TerrainBlock> batch = terrainBatches.get(terrain);
            for(TerrainBlock terrainBlock : batch) {
                //if(camera.getFrusutmCuller().testTerrainInView(terrainBlock)) {
                    int[] indexInfo = terrainBlock.getIndicesVBOInfo();
                    OpenglUtils.bindEBO(indexInfo[0]);
                    render(terrainBlock.getIndex(), indexInfo[1]);
                    OpenglUtils.unbindEBO();
                //}
            }
            OpenglUtils.unbindVAO();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        shader.unbind();
    }

    private void prepareTerrainInstance(Terrain terrain, Camera camera, Light light) {
        shader.setUniform("view", camera.getViewMatrix());
        shader.setUniform("lightPosition", light.position);
        shader.setUniform("lightColor", light.color);

        shader.setUniform("model",
                MathUtils.getModelMatrix(modelMatrix, new Vector3f(terrain.getX(), 0, terrain.getZ()),
                        new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)));

        TexturePack texturePack = terrain.getTexture();
        texturePack.blendMap.bind(0);
        texturePack.alphaTexture.bind(1);
        texturePack.redTexture.bind(2);
        texturePack.greenTexture.bind(3);
        texturePack.blueTexture.bind(4);

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
