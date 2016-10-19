package com.paleon.engine.graph.render;

import com.paleon.engine.core.Display;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.*;
import com.paleon.engine.components.Material;
import com.paleon.engine.graph.shader.ShaderProgram;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.utils.OpenglUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Rick on 06.10.2016.
 */
public class MeshRenderer {

    private ShaderProgram shader;

    private Camera camera;

    public MeshRenderer(Camera camera) {
        this.camera = camera;

        ResourceManager.loadShader("entity");
        shader = ResourceManager.getShader("entity");

        shader.createUniform("projection");
        shader.createUniform("view");
        shader.createUniform("model");

        shader.createUniform("image");
        shader.createUniform("color");

        shader.createUniform("lightPosition");
        shader.createUniform("lightColor");

        shader.createUniform("viewPosition");

        shader.createUniform("numberOfRows");
        shader.createUniform("offset");
        shader.createUniform("useFakeLighting");
        shader.createUniform("useSpecular");

        shader.setUniform("projection", this.camera.getProjectionMatrix(), true);
        shader.setUniform("image", 0, true);
    }

    public void render(Map<Mesh, List<Entity>> entities, Light light, Camera camera) {
        if(Display.wasResized()) {
            shader.setUniform("projection", this.camera.getProjectionMatrix(), true);
        }

        shader.bind();

        shader.setUniform("view", camera.getViewMatrix());
        shader.setUniform("viewPosition", camera.getPosition());

        shader.setUniform("lightPosition", light.position);
        shader.setUniform("lightColor", light.color);

        for(Mesh mesh :entities.keySet()) {
            mesh.startRender();

            List<Entity> batch = entities.get(mesh);
            for(Entity entity : batch) {
            	if(entity.isActive()) {
	                shader.setUniform("model", entity.getTransform().getModelMatrix());
	
	                Material material = entity.getComponent(Material.class);
	
	                shader.setUniform("color", material.color);
	                int numberOfRows = material.getNumberOfRows();
	                shader.setUniform("numberOfRows", numberOfRows);
	                if(numberOfRows != 1) {
	                    shader.setUniform("offset", entity.getTextureXOffset(), entity.getTextureYOffset());
	                }
	                shader.setUniform("useFakeLighting", material.useFakeLighting);
	                shader.setUniform("useSpecular", material.useSpecular);
	
	                if(material.transparency) {
	                    OpenglUtils.cullFace(false);
	                }
	
	                Texture texture = material.texture;
	
	                texture.bind(0);
	
	                mesh.render();
	
	                texture.unbind();
	
	                OpenglUtils.cullFace(true);
            	}
            }

            mesh.endRender();
        }
    }

    public void cleanup() {
        shader.cleanup();
    }

}
