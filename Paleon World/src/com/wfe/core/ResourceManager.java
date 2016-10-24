package com.wfe.core;

import java.util.HashMap;
import java.util.Map;

import com.wfe.graph.Mesh;
import com.wfe.graph.Texture;
import com.wfe.graph.TextureLoader;
import com.wfe.graph.shaders.ShaderProgram;
import com.wfe.obj.OBJLoader;

public class ResourceManager {

	private static Map<String, ShaderProgram> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
	private static Map<String, Mesh> meshes = new HashMap<>();
	
	public static ShaderProgram loadShader(String shaderName) {
        try {
            ShaderProgram shader = new ShaderProgram();
            shader.createVertexShader("/shaders/" + shaderName + ".vert");
            shader.createFragmentShader("/shaders/" + shaderName + ".frag");
            shader.link();
            shaders.put(shaderName, shader);
            return shader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	public static ShaderProgram getShader(String shaderName) {
		return shaders.get(shaderName);
	}
	
	public static Texture loadTexture(String texturePath, String textureName){
        try {
        	Texture texture = TextureLoader.load("/" + texturePath + ".png");
            textures.put(textureName, texture);
            return texture;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static Texture getTexture(String textureName) {
		return textures.get(textureName);
	}
	
	public static Texture loadSkybox(String skyboxName) {
        try {
        	Texture texture = TextureLoader.loadCubemap(skyboxName);
            textures.put(skyboxName, texture);
            return texture;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static int getSkybox(String skyboxName) {
		return textures.get(skyboxName).getID();
	}

    public static Mesh loadMesh(String meshPath, String meshName) {
        try {
        	Mesh mesh = OBJLoader.loadMesh("/" + meshPath + ".obj");
            meshes.put(meshName, mesh);
            return mesh;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Mesh getMesh(String meshName) {
        return meshes.get(meshName);
    }

	public static void cleanup() {
		for(Map.Entry<String, ShaderProgram> entry : shaders.entrySet())
			entry.getValue().cleanup();
		
		for(Map.Entry<String, Texture> entry : textures.entrySet())
			entry.getValue().cleanup();

		for(Map.Entry<String, Mesh> entry : meshes.entrySet())
            entry.getValue().cleanup();
	}
}