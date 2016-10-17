package com.paleon.engine.core;

import com.paleon.engine.components.Mesh;
import com.paleon.engine.graph.Texture;
import com.paleon.engine.components.Material;
import com.paleon.engine.graph.shader.ShaderProgram;
import com.paleon.engine.loaders.OBJLoader;
import com.paleon.engine.loaders.TextureLoader;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

	private static Map<String, ShaderProgram> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Mesh> meshes = new HashMap<>();
	private static Map<String, Material> materials = new HashMap<>();
	
	public static void loadShader(String shaderName) {
        try {
            ShaderProgram shader = new ShaderProgram();
            shader.createVertexShader("/shaders/" + shaderName + ".vertex");
            shader.createFragmentShader("/shaders/" + shaderName + ".fragment");
            shader.link();
            shaders.put(shaderName, shader);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static ShaderProgram getShader(String shaderName) {
		return shaders.get(shaderName);
	}
	
	public static void loadTexture(String texturePath, String textureName){
        try {
            textures.put(textureName, TextureLoader.load(texturePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static Texture getTexture(String textureName) {
		return textures.get(textureName);
	}
	
	public static void loadSkybox(String skyboxName) {
        try {
            textures.put(skyboxName, TextureLoader.loadCubemap(skyboxName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static int getSkybox(String skyboxName) {
		return textures.get(skyboxName).getID();
	}

    public static void loadMesh(String meshPath, String meshName) {
        try {
            meshes.put(meshName, OBJLoader.loadMesh(meshPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Mesh getMesh(String meshName) {
        return meshes.get(meshName);
    }

    public static void loadMaterial(Material material, String name) {
        materials.put(name, material);
    }

    public static Material getMaterial(String name) {
        return materials.get(name);
    }

	public static void cleanup() {
		for(Map.Entry<String, ShaderProgram> entry : shaders.entrySet()) {
			entry.getValue().cleanup();
		}
		for(Map.Entry<String, Texture> entry : textures.entrySet()) {
			entry.getValue().cleanup();
		}

		for(Map.Entry<String, Mesh> entry : meshes.entrySet()) {
            entry.getValue().cleanup();
        }
	}
}