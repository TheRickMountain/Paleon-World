package com.wfe.behaviours;

import com.wfe.graph.Camera;
import com.wfe.input.Mouse;
import com.wfe.math.Vector2f;
import com.wfe.math.Vector3f;
import com.wfe.scenegraph.World;
import com.wfe.utils.MathUtils;
import com.wfe.utils.MousePicker;

public class ControllingBh extends Behaviour {

	public float speed = 15.0f;
	
	private AnimBh anim;
	
	private boolean move = false;
	
	private World world;
	
	private Vector3f targetPosition;
	
	public ControllingBh(Camera camera) {
		
	}
	
	@Override
	public void start() {
		this.anim = parent.getBehaviour(AnimBh.class);
		this.world = parent.getWorld();	
	}

	@Override
	public void update(float dt) {	
		Vector2f ctp = MousePicker.getGridPoint();
		System.out.println(ctp);
		if(Mouse.isButtonDown(0)) {
			//Vector2f ctp = MousePicker.getGridPoint();
			//System.out.println(ctp);
			
			if(ctp != null) {
				targetPosition = new Vector3f();
				Vector3f tp = world.cells.get((int)ctp.x + " " + (int)ctp.y).position;
				targetPosition.x = tp.x;
				targetPosition.z = tp.z;
			}
		}
		
		
		moving(dt);
	}

	public void moving(float dt) {
		move = false;
		
		parent.position.y = world.getTerrainHeight(parent.position.x, parent.position.z) + 2.25f;
		
		if(targetPosition != null) {
			if(MathUtils.getDistanceBetweenPoints(targetPosition.x, targetPosition.z, 
					parent.position.x, parent.position.z) >= 0.5f) {
				float direction = MathUtils.getRotationBetweenPoints(targetPosition.x, targetPosition.z, 
						parent.position.x, parent.position.z);
				parent.position.x += (float)Math.sin(Math.toRadians(direction + 90)) * -1.0f * speed * dt;
				parent.position.z += (float)Math.cos(Math.toRadians(direction + 90)) * speed * dt;
				parent.rotation.y = -direction + 90;
				move = true;
			} else {
				parent.position.x = targetPosition.x;
				parent.position.z = targetPosition.z;
				System.out.println(parent.position);
				targetPosition = null;
			}
		}
		
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
