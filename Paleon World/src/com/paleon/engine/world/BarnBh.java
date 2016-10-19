package com.paleon.engine.world;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.behaviours.BoundingBoxBh;
import com.paleon.engine.input.Mouse;

public class BarnBh extends Behaviour {

	private BoundingBoxBh boundingBox;
	
	public BarnBh(BoundingBoxBh boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void update(float deltaTime) {
		if(Mouse.isButtonDown(0)) {
			if(boundingBox.intersect())
				System.out.println("Barn intersected");
		}
	}

	@Override
	public void onGUI() {
		
	}

	
	
}
