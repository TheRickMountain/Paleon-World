package com.paleon.engine.terrain;

import com.paleon.engine.ResourceManager;
import com.paleon.engine.graph.Texture2D;

public class TexturePack {

	Texture2D blendMap;
	Texture2D aTexture;
	Texture2D rTexture;
	Texture2D gTexture;
	Texture2D bTexture;
	
	public TexturePack(String blendMap, String alpha, String red, String green, String blue) {
		this.blendMap = ResourceManager.getTexture(blendMap);
		this.aTexture = ResourceManager.getTexture(alpha);
		this.rTexture = ResourceManager.getTexture(red);
		this.gTexture = ResourceManager.getTexture(green);
		this.bTexture = ResourceManager.getTexture(blue);
	}

	public Texture2D getBlendMap() {
		return blendMap;
	}

	public Texture2D getaTexture() {
		return aTexture;
	}

	public Texture2D getrTexture() {
		return rTexture;
	}

	public Texture2D getgTexture() {
		return gTexture;
	}

	public Texture2D getbTexture() {
		return bTexture;
	}
	
}
