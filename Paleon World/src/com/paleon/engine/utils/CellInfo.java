package com.paleon.engine.utils;

/**
 * Created by Rick on 17.10.2016.
 */
public class CellInfo {

    private int x;
    private int z;
    public int state;

    public CellInfo(int x, int z, int state) {
        this.x = x;
        this.z = z;
        this.state = state;
    }

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

}
