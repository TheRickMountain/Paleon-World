package com.wfe.behaviours;

import com.wfe.math.Vector3f;
import com.wfe.utils.MousePicker;

public class BoundingBoxBh extends Behaviour {

	public Vector3f bounds[];
	
	private Vector3f min, max;
	
	public BoundingBoxBh(Vector3f min, Vector3f max) {
		bounds = new Vector3f[2];
		bounds[0] = new Vector3f(min);
		bounds[1] = new Vector3f(max);
		
		this.min = min;
		this.max = max;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void update(float deltaTime) {
		bounds[0].x = min.x + parent.position.x;
		bounds[0].y = min.y + parent.position.y;
		bounds[0].z = min.z + parent.position.z;
		
		bounds[1].x = max.x + parent.position.x;
		bounds[1].y = max.y + parent.position.y;
		bounds[1].z = max.z + parent.position.z;
	}

	@Override
	public void onGUI() {
		
	}
	
	public boolean collisionDetection(BoundingBoxBh bb) {
		return (bb.bounds[0].x <= bounds[1].x && bb.bounds[1].x >= bounds[0].x) &&
				(bb.bounds[0].y <= bounds[1].y && bb.bounds[1].y >= bounds[0].y) &&
				(bb.bounds[0].z <= bounds[1].z && bb.bounds[1].z >= bounds[0].z);
	}
	
	public void collisionResponce(BoundingBoxBh bb) {
		if((bb.bounds[0].z <= bounds[1].z && bb.bounds[1].z >= bounds[0].z)) {
			if((bb.bounds[1].x > bounds[0].x && bb.bounds[1].x < bounds[1].x)) {
				float length = bb.bounds[1].x - bounds[0].x;
				System.out.println(length);
				bb.parent.position.x -= length;
			}
			
			if((bb.bounds[0].x > bounds[0].x && bb.bounds[0].x < bounds[1].x)) {
				float length = bounds[1].x - bb.bounds[0].x;
				bb.parent.position.x += length;
			}
		}
		
		/*if((bb.bounds[0].z > bounds[0].z && bb.bounds[0].z < bounds[1].z) ||
				(bb.bounds[1].z > bounds[0].z && bb.bounds[1].z < bounds[1].z)) {
			float length = bb.bounds[1].z - bounds[0].z;
			bb.parent.position.z -= length;
		}*/
		
		/*boolean moveX = false;
		boolean moveY = false;
		boolean moveZ = false;
		
		Vector3f endp[] = new Vector3f[2];
		endp[0] = new Vector3f();
		endp[1] = new Vector3f();
		
		Vector3f direction = Vector3f.sub(bb.parent.position, parent.position, null);
		
		endp[1].x = Math.min(bb.bounds[1].x, bounds[1].x);
		endp[1].y = Math.min(bb.bounds[1].y, bounds[1].y);
		endp[1].z = Math.min(bb.bounds[1].z, bounds[1].z);
		
		endp[0].x = Math.max(bb.bounds[0].x, bounds[0].x);
		endp[0].y = Math.max(bb.bounds[0].y, bounds[0].y);
		endp[0].z = Math.max(bb.bounds[0].z, bounds[0].z);
		
		Vector3f overlap = Vector3f.sub(endp[1], endp[0], null);
		
		
		if((overlap.x < overlap.y) && (overlap.x < overlap.z))
			moveX = true;
		
		if((overlap.y < overlap.x) && (overlap.y < overlap.z))
			moveY = true;
		
		if((overlap.z < overlap.x) && (overlap.z < overlap.y))
			moveZ = true;
		
		if(moveX) {
			bb.parent.position.x = parent.position.x + (Math.signum(direction.x) *
					((bb.bounds[1].x - bb.bounds[0].x + bounds[1].x - bounds[0].x) / 2));
		}
		
			
		if(moveY) {
			bb.parent.position.y = parent.position.y + (Math.signum(direction.y) *
					((bb.bounds[1].y - bb.bounds[0].y + bounds[1].y - bounds[0].y) / 2));
		}
		
		if(moveZ) {
			bb.parent.position.z = parent.position.z + (Math.signum(direction.z) *
					((bb.bounds[1].z - bb.bounds[0].z + bounds[1].z - bounds[0].z) / 2));
		}*/
	}
	
	public boolean intersect() {
		float tmin, tmax, tymin, tymax, tzmin, tzmax;
		Vector3f rayOrigin = MousePicker.getRayOrigin();
		Vector3f rayDirection = MousePicker.getCurrentRay();
		
		if (rayDirection.x >= 0) {
			tmin = (bounds[0].x - rayOrigin.x) / rayDirection.x;
			tmax = (bounds[1].x - rayOrigin.x) / rayDirection.x;
		} else {
			tmin = (bounds[1].x - rayOrigin.x) / rayDirection.x;
			tmax = (bounds[0].x - rayOrigin.x) / rayDirection.x;
		}
		
		if (rayDirection.y >= 0) {
			tymin = (bounds[0].y - rayOrigin.y) / rayDirection.y;
			tymax = (bounds[1].y - rayOrigin.y) / rayDirection.y;
		} else {
			tymin = (bounds[1].y - rayOrigin.y) / rayDirection.y;
			tymax = (bounds[0].y - rayOrigin.y) / rayDirection.y;
		}
		
		if ( (tmin > tymax) || (tymin > tmax) )
			return false;
			
		if (tymin > tmin)
			tmin = tymin;
		
		if (tymax < tmax)
			tmax = tymax;
		
		if (rayDirection.z >= 0) {
			tzmin = (bounds[0].z - rayOrigin.z) / rayDirection.z;
			tzmax = (bounds[1].z - rayOrigin.z) / rayDirection.z;
		} else {
			tzmin = (bounds[1].z - rayOrigin.z) / rayDirection.z;
			tzmax = (bounds[0].z - rayOrigin.z) / rayDirection.z;
		}
			
		if ( (tmin > tzmax) || (tzmin > tmax) )
			return false;
			
		if (tzmin > tmin)
			tmin = tzmin;
			
		if (tzmax < tmax)
			tmax = tzmax;
			
		return ( (tmin < 1000) && (tmax > 1) );
	}

}
