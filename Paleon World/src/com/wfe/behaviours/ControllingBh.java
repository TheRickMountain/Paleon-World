package com.wfe.behaviours;

public class ControllingBh extends Behaviour {

	private AnimBh anim;
	
	private boolean move = false;
	
	public ControllingBh() {
	}
	
	@Override
	public void start() {
		this.anim = parent.getBehaviour(AnimBh.class);
	}

	@Override
	public void update(float dt) {
		move = false;
		
		if(move) {
			anim.walkAnim(dt);	
		} else {
			anim.idleAnim(dt);
		}
	}

	@Override
	public void onGUI() {
		
	}

}
