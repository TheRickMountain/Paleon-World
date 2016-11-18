package com.wfe.behaviours;

import com.wfe.input.Mouse;

public class ButtonBh extends Behaviour {

	@Override
	public void start() {
		
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void onGUI() {
		
	}

	public boolean over() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		return (mouseX > parent.position.x  && mouseX < parent.position.x + parent.scale.x) &&
				(mouseY > parent.position.y  && mouseY < parent.position.y + parent.scale.y);
	}
	
	public boolean isPressedDown() {
		if(over()) {
			if(Mouse.isButtonDown(0)) {
				return true;
			}
		}
		return false;
	}
	
}
