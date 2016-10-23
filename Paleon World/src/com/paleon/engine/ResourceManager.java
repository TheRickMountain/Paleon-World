package com.paleon.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.paleon.engine.graph.Material;
import com.paleon.engine.graph.Mesh;
import com.paleon.engine.graph.ShaderProgram;
import com.paleon.engine.graph.Texture2D;
import com.paleon.engine.loaders.OBJLoader;
import com.paleon.engine.loaders.TextureLoader;

public class ResourceManager {

	private static Map<String, ShaderProgram> shaders = new HashMap<String, ShaderProgram>();
	private static Map<String, Texture2D> textures = new HashMap<String, Texture2D>();
	private static Map<String, Mesh> meshes = new HashMap<String, Mesh>();
	
	public static ShaderProgram loadShader(String shaderName) {
		ShaderProgram shader = null;
		try {
			shader = new ShaderProgram();
			shader.createVertexShader("/shaders/" + shaderName + ".vs");
			shader.createFragmentShader("/shaders/" + shaderName + ".fs");
			shader.link();
			shaders.put(shaderName, shader);
			return shader;
		} catch (Exception e) {
			System.err.println("Failed to load " + shaderName + " shader");
			e.printStackTrace();
		}
		return null;
	}
	
	public static ShaderProgram getShader(String shaderName) {
		return shaders.get(shaderName);
	}
	
	public static Texture2D loadTexture(String texturePath, String textureName) {
		Texture2D texture = null;
		try {
			texture = TextureLoader.load(texturePath);
			textures.put(textureName, texture);
			return texture;
		} catch (Exception e) {
			System.err.println("Failed to load " + texturePath + " texture");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Texture2D getTexture(String textureName) {
		return textures.get(textureName);
	}
	
	public static int loadSkybox(String skyboxName) {
		Texture2D texture = null;
		try {
			texture = TextureLoader.loadCubemap(skyboxName);
			textures.put(skyboxName, texture);
			return texture.getID();
		} catch (Exception e) {
			System.err.println("Failed to load " + skyboxName + " texture");
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getSkybox(String skyboxName) {
		return textures.get(skyboxName).getID();
	}
	
	public static Mesh loadMesh(String meshPath, String meshName, Material material) {
		try {
			Mesh mesh = null;
			mesh = OBJLoader.loadMesh(meshPath);
			mesh.setMaterial(material);
			meshes.put(meshName, mesh);
			return mesh;
		} catch (Exception e) {
			System.err.println("Failed to load " + meshPath + " mesh");
			e.printStackTrace();
		}
		return null;
	}
	
	public static Mesh getMesh(String meshName) {
		return meshes.get(meshName);
	}
	
	public static void clear() {
		for(Entry<String, ShaderProgram> entry : shaders.entrySet()) {
			entry.getValue().cleanup();
		}
		for(Entry<String, Texture2D> entry : textures.entrySet()) {
			entry.getValue().cleanup();
		}
		for(Entry<String, Mesh> entry : meshes.entrySet()) {
			entry.getValue().cleanup();
		}
	}
}
