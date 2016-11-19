package com.wfe.behaviours;

import java.util.LinkedList;

import com.wfe.astar.Cell;
import com.wfe.astar.Table;
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
		if(Mouse.isButtonDown(0)) {
			Vector2f finishPoint = MousePicker.getGridPoint();
			
			Table<Cell> cellList = new Table<Cell>(256, 256);
			LinkedList<Cell> openList = new LinkedList<Cell>();
			LinkedList<Cell> closedList = new LinkedList<Cell>();
			LinkedList<Cell> tmpList = new LinkedList<Cell>();
			
			for(int i = 0; i < 256; i++)
				for(int j = 0; j < 256; j++)
					cellList.add(new Cell(j, i, world.blockList.get(j, i).blocked));
			
			Vector2f startPoint = MousePicker.toGridPoint(parent.position);
			
			cellList.get((int)startPoint.x, (int)startPoint.y).setAsStart();
			cellList.get((int)finishPoint.x, (int)finishPoint.y).setAsFinish();
			Cell start = cellList.get((int)startPoint.x, (int)startPoint.y);
			Cell finish = cellList.get((int)finishPoint.x, (int)finishPoint.y);
			
			boolean found = false;
			boolean noroute = false;
			
			openList.push(start);
			
			while(!found && !noroute) {
				Cell min = openList.getFirst();
				for(Cell cell : openList) {
					if(cell.F < min.F) min = cell;
				}
				
				closedList.push(min);
				openList.remove(min);
				
				tmpList.clear();
				tmpList.add(cellList.get(min.x - 1, min.y - 1));
				tmpList.add(cellList.get(min.x, 	min.y - 1));
				tmpList.add(cellList.get(min.x + 1, min.y - 1));
				tmpList.add(cellList.get(min.x + 1, min.y));
				tmpList.add(cellList.get(min.x + 1, min.y + 1));
				tmpList.add(cellList.get(min.x, 	min.y + 1));
				tmpList.add(cellList.get(min.x - 1, min.y + 1));
				tmpList.add(cellList.get(min.x - 1, min.y));
				
				for(Cell neightbour : tmpList) {
					if(neightbour.blocked || closedList.contains(neightbour)) continue;
				
					if(!openList.contains(neightbour)) {
						openList.add(neightbour);
						neightbour.parent = min;
						neightbour.H = neightbour.mandist(finish);
						neightbour.G = start.price(min);
						neightbour.F = neightbour.H + neightbour.G;
						continue;
					}
					
					if(neightbour.G + neightbour.price(min) < min.G) {
						neightbour.parent = min;
						neightbour.H = neightbour.mandist(finish);
						neightbour.G = start.price(min);
						neightbour.F = neightbour.H + neightbour.G;
					}
				}
				
				if(openList.contains(finish))
					found = true;
				
				if(openList.isEmpty())
					noroute = true;
			}
			
			if(!noroute) {
				Cell rd = finish.parent;
				while(!rd.equals(start)) {
					rd.road = true;
					rd = rd.parent;
					if(rd == null) break;
				}
			} else {
				System.out.println("No route");
			}
			
			/*if(ctp != null) {
				targetPosition = new Vector3f();
				Vector3f tp = world.cells.get((int)ctp.x + " " + (int)ctp.y).position;
				targetPosition.x = tp.x;
				targetPosition.z = tp.z;
			}*/
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
