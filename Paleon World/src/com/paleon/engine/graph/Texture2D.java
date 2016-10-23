package com.paleon.engine.graph;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture2D {

	private int id;
	private int width, height;
	
	public Texture2D(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public int getID() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void bind(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public void cleanup() {
		GL11.glDeleteTextures(id);
	}
	
}
