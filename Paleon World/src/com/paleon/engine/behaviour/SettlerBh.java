package com.paleon.engine.behaviour;

import com.paleon.engine.graph.gui.ItemDatabase;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.toolbox.MathUtils;
import com.paleon.engine.toolbox.TimeUtil;
import com.paleon.engine.world.Firewood;
import com.paleon.engine.world.FishBox;
import com.paleon.engine.world.Logs;
import com.paleon.game.building.BarnGuiBh;
import com.paleon.maths.vecmath.Vector3f;

public class SettlerBh extends Behaviour {

	private World world;
	private boolean moving = false;
	
	private float speed = 12;
	
	private CharacterAnimBh playerAnim;
	
	private Entity targetEntity;
	
	private Vector3f targetPosition;
	
	private Entity targetWork;
	
	private Entity cargo;
	
	private TimeUtil time;
	
	public String flName = "noname";
	
	public SettlerBh(String flName, World world) {
		this.flName = flName;
		this.world = world;
	}
	
	@Override
	public void start() {
		playerAnim = parent.getBehaviour(CharacterAnimBh.class);
		time = new TimeUtil();
	}

	@Override
	public void update(float dt) {
		moving = false;
		
		parent.position.y = world.getTerrainHeight(parent.position.x, parent.position.z) + 2.25f;
		
		if(targetWork != null) {
			switch (targetWork.name) {
			case "Forester":
			{
				if(targetEntity == null) {
					if(parent.getWorld().getEntityCountInRangeByName("Tree", targetWork.position, 200) != 0) {
						Entity tree = parent.getWorld().getRandomEntityByName("Tree");
						if(tree != null) {
							if(MathUtils.getDistanceBetweenPoints(tree.position, targetWork.position) <= 200) {
								if(tree != null) {
									targetEntity = tree;
								}
							}
						}
					} else {
						System.out.println("It's time to plant trees");
					}
				}
			}
				break;
			case "Fishing Dock":
				if(targetEntity == null) {
					targetEntity = targetWork;
				}
				break;
			case "Woodcutter":
				if(targetEntity == null) {
					Entity barn = parent.getWorld().getEntityByName("Barn");
					BarnGuiBh barnGuiBh = barn.getBehaviour(BarnGuiBh.class);
					int woodCount = barnGuiBh.getItemCount(ItemDatabase.LOGS);
					if(woodCount >= 1) {
						targetEntity = barn;
					}
				}
				break;
			}
		}
		
		if(targetEntity != null) {
			Vector3f tgoPos = targetEntity.position;
			if(MathUtils.getDistanceBetweenPoints(tgoPos.x, tgoPos.z, parent.position.x, parent.position.z) >= 2) {
				float direction = MathUtils.getRotationBetweenPoints(tgoPos.x, tgoPos.z, parent.position.x, parent.position.z);
				parent.position.x += (float)Math.sin(Math.toRadians(direction + 90)) * -1.0f * speed * dt;
				parent.position.z += (float)Math.cos(Math.toRadians(direction + 90)) * speed * dt;
				parent.rotation.y = -direction + 90;
				moving = true;
			} else {
				if(targetEntity.name.equals("Tree")) {
					if(time.getTime() >= 10) {
						time.reset();
						targetEntity.remove();
						targetEntity = null;
						cargo = new Logs(parent.getWorld());
						cargo.localPosition.z = 0.5f;
						cargo.localPosition.y = 0.5f;
						cargo.localRotation.x = 90;
						parent.addChild(cargo);
						targetEntity = parent.getWorld().getEntityByName("Barn");
					}
				} else if(targetEntity.name.equals("Fishing Dock")) {
					if(time.getTime() >= 10) {
						time.reset();
						targetEntity = null;
						cargo = new FishBox(parent.getWorld());
						cargo.localPosition.z = -1f;
						parent.addChild(cargo);
						targetEntity = parent.getWorld().getEntityByName("Barn");
					}
				} else if(targetEntity.name.equals("Barn")) {

					if(targetWork.name.equals("Forester")) {
						cargo.remove();
						cargo = null;
						
						for(int i = 0; i < 6; i++)
							targetEntity.getBehaviour(BarnGuiBh.class).addItem(ItemDatabase.LOGS);
						
						targetEntity = null;
					} else if(targetWork.name.equals("Fishing Dock")) {
						cargo.remove();
						cargo = null;
						
						for(int i = 0; i < 10; i++)
							targetEntity.getBehaviour(BarnGuiBh.class).addItem(ItemDatabase.FISH);
						
						targetEntity = null;
					} else if(targetWork.name.equals("Woodcutter")) {
						if(cargo != null) {
							cargo.remove();
							cargo = null;
							
							for(int i = 0; i < 12; i++)
								targetEntity.getBehaviour(BarnGuiBh.class).addItem(ItemDatabase.FIREWOOD);
						}
						
						for(int i = 0; i < 6; i++) {
							targetEntity.getBehaviour(BarnGuiBh.class).removeItem(ItemDatabase.LOGS);
						}
						
						targetEntity = targetWork;
					}
				} else if(targetEntity.name.equals("Woodcutter")) {
					if(time.getTime() >= 10) {
						time.reset();
						targetEntity = null;
						
						cargo = new Firewood(parent.getWorld());
						cargo.localPosition.z = 1.2f;
						cargo.localRotation.y = 90;
						cargo.localScale.set(0.3f);
						parent.addChild(cargo);
						targetEntity = parent.getWorld().getEntityByName("Barn");
					}
				}
			}
		}
		
		if(targetPosition != null) {
			if(MathUtils.getDistanceBetweenPoints(targetPosition.x, targetPosition.z, 
					parent.position.x, parent.position.z) >= 2) {
				float direction = MathUtils.getRotationBetweenPoints(targetPosition.x, targetPosition.z, 
						parent.position.x, parent.position.z);
				parent.position.x += (float)Math.sin(Math.toRadians(direction + 90)) * -1.0f * speed * dt;
				parent.position.z += (float)Math.cos(Math.toRadians(direction + 90)) * speed * dt;
				parent.rotation.y = -direction + 90;
				moving = true;
			} else {
				targetPosition = null;
			}
		}
		
		if(moving) {
			playerAnim.walkAnim(dt);
		} else {
			playerAnim.idleAnim(dt);
		}
	}

	@Override
	public void onGUI() {
		
	}

	public void setWorkPlace(Entity workPlace) {
		this.targetWork = workPlace;
	}
	
	public Entity getWorkPlace() {
		return targetWork;
	}
	
}
