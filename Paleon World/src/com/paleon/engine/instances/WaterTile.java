package com.paleon.engine.instances;

public class WaterTile {

	public static final float TILE_SIZE = 60;
	public static final float HEIGHT = 0.5f;
	
	private float x, z;

	public WaterTile(float centerX, float centerZ) {
		this.x = centerX;
		this.z = centerZ;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
}
