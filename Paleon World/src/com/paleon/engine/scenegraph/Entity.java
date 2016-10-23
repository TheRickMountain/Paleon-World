package com.paleon.engine.scenegraph;

import java.util.ArrayList;
import java.util.List;

import com.paleon.engine.behaviour.Behaviour;
import com.paleon.engine.components.Component;
import com.paleon.engine.components.MeshFilter;
import com.paleon.engine.graph.Material;
import com.paleon.engine.graph.Transform;
import com.paleon.engine.toolbox.MathUtils;
import com.paleon.engine.toolbox.ReflectionUtils;
import com.paleon.maths.vecmath.Matrix4f;
import com.paleon.maths.vecmath.Vector3f;
import com.paleon.maths.vecmath.Vector4f;

public class Entity {
	
	private World world;
	
	public String name;
	
	public final Vector3f position;
	public final Vector3f rotation;
	public final Vector3f scale;
	
	public final Vector3f localPosition;
	public final Vector3f localRotation;
	public final Vector3f localScale;
	
	private List<Component> components;
	private List<Behaviour> behaviours;
	private List<Entity> children;
	
	// TEMP
	private Matrix4f modelMatrix = new Matrix4f();
	// TEMP
	
	private Entity parent;
	
	private boolean active = true;
	
	public final Transform transform;
	
	private boolean useWaving;
	
	private int textureIndex = 0;
	
	private int ID = 0;
	
	private boolean removed = false;
	
	private int guiId = -1;
	
	private float furthestPoint = 1;
	
	public Entity(World world) {
		this("", world);
	}
	
	public Entity(String name, World world) {
		this.world = world;
		this.name = name;
		position = new Vector3f();
		rotation = new Vector3f();
		scale = new Vector3f(1, 1, 1);
		
		localPosition = new Vector3f();
		localRotation = new Vector3f();
		localScale = new Vector3f(0, 0, 0);
		
		components = new ArrayList<Component>();
		behaviours = new ArrayList<Behaviour>();
		children = new ArrayList<Entity>();
		
		this.transform = new Transform(this);
	}
	
	public void init() {
		for(Behaviour b : behaviours) {
			b.start();
		}
		
		updatePRS();
	}
	
	private void updatePRS() {
		if(getParent() != null) {
			MathUtils.getEulerModelMatrix(modelMatrix, getParent().position, getParent().rotation, getParent().scale);
			Matrix4f.transform(modelMatrix, new Vector4f(localPosition.x, localPosition.y, localPosition.z, 1.0f), position);

			Vector3f.add(localRotation,  getParent().rotation, rotation);
			Vector3f.add(localScale,  getParent().scale, scale);
		}
	}
	
	public void update(float deltaTime) {		
		updatePRS();
		
		transform.update();
		
		for(Behaviour b : behaviours) {
			b.update(deltaTime);
		}
	}
	
	public void onGUI() {
		for(Behaviour b : behaviours) {
			b.onGUI();
		}
	}
	
	public World getWorld() {
		return world;
	}
	
	public float getTextureXOffset(){
		Material material = getComponent(MeshFilter.class).mesh.getMaterial();
		
		int column = textureIndex % material.getNumberOfRows();
		return (float) column / (float) material.getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		Material material = getComponent(MeshFilter.class).mesh.getMaterial();
		
		int row = textureIndex / material.getNumberOfRows();
		return (float) row / (float)material.getNumberOfRows();
	}

	public boolean isUseWaving() {
		return useWaving;
	}

	public void setUseWaving(boolean useWaving) {
		this.useWaving = useWaving;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void remove() {
		this.removed = true;
		
		this.components.clear();
		this.behaviours.clear();
		
		for(Entity child : children) {
			child.remove();
		}
		
		this.children.clear();
	}

	public int getGuiID() {
		return guiId;
	}

	public void setGuiID(int guiId) {
		this.guiId = guiId;
	}
	
	public void setFurthestPoint(float furthestPoint) {
		this.furthestPoint = furthestPoint;
	}
	
	public float getFurthestPoint() {
		return furthestPoint * Math.max(Math.max(scale.x, scale.y), scale.z);
	}
	
	public void addChild(Entity child) {
		child.setParent(this);
		children.add(child);
		world.addEntity(child);
	}
	
	public void removeChild(Entity child) {
		child.setParent(null);
		child.remove();
		children.remove(child);
	}
	
	public List<Entity> getChildren() {
		return children;
	}
	
	public Entity getChildByName(String childName) {
		for(Entity gameObject : children) {
			if(gameObject.name == childName) {
				return gameObject;
			}
		}
		return null;
	}
	
	public void addComponent(Component component) {
		component.setParent(this);
		this.components.add(component);
	}
	
	public void addBehaviour(Behaviour behaviour) {
		behaviour.setParent(this);
		this.behaviours.add(behaviour);
	}
	
	public void removeComponent(Component component) {
		this.components.remove(component);
	}
	
	public void removeBehaviour(Behaviour behaviour) {
		this.behaviours.remove(behaviour);
	}
	
	@SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type)
    {
        for (Component component : components)
            if (ReflectionUtils.isInstanceOf(type, component))
                return (T) component;

        return null;
    }
	
	@SuppressWarnings("unchecked")
    public <T extends Behaviour> T getBehaviour(Class<T> type)
    {
        for (Behaviour behaviour : behaviours)
            if (ReflectionUtils.isInstanceOf(type, behaviour))
                return (T) behaviour;

        return null;
    }
	
	public Entity getParent() {
		return parent;
	}
	
	private void setParent(Entity parent) {
		this.parent = parent;
	}

	public boolean isActive() {
		return active;
	}
	
	public void activate() {
		this.active = true;
		
		for(Entity child : children) {
			child.activate();
		}
	}
	
	public void deactivate() {
		this.active = false;
		
		for(Entity child : children) {
			child.deactivate();
		}
	}

}
