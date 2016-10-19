package com.paleon.engine.scenegraph;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.components.Component;
import com.paleon.engine.components.Material;
import com.paleon.engine.graph.transform.Transform;
import com.paleon.engine.utils.ReflectionUtils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rick on 06.10.2016.
 */
public class Entity3D implements Entity {

    public final Vector3f position;
    public final Vector3f rotation;
    public final Vector3f scale;

    private final World world;

    private Transform transform;

    private Entity3D parent;

    public String name;

    private boolean selected;

    private final List<Component> components;
    private final List<Behaviour> behaviours;
    private final List<Entity3D> children;

    public int textureIndex = 0;

    public Entity3D(World world, String name) {
        this.world = world;
        this.world.addEntity(this);

        this.name = name;

        components = new ArrayList<>();
        behaviours = new ArrayList<>();
        children = new ArrayList<>();

        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1, 1, 1);
    }

    public World getWorld() {
        return world;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
        this.transform.setParent(this);
        this.world.addTransform(transform);
    }

    public Transform getTransform() {
        return transform;
    }

    public Entity3D getParent() {
        return parent;
    }

    private void setParent(Entity3D parent) {
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null ? true : false;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type)
    {
        for (Component component : components)
            if (ReflectionUtils.isInstanceOf(type, component))
                return (T) component;

        return null;
    }

    public void addComponent(Component component) {
        component.setParent(this);
        this.components.add(component);
        this.world.addComponent(component);
    }

    public void removeComponent(Component component) {
        this.components.remove(component);
        this.world.removeComponent(component);
    }

    public List<Component> getComponents() {
        return components;
    }

    @SuppressWarnings("unchecked")
    public <T extends Behaviour> T getBehaviour(Class<T> type)
    {
        for (Behaviour behaviour : behaviours)
            if (ReflectionUtils.isInstanceOf(type, behaviour))
                return (T) behaviour;

        return null;
    }

    public void addBehaviour(Behaviour behaviour) {
        behaviour.setParent(this);
        this.behaviours.add(behaviour);
        this.world.addBehaviour(behaviour);
    }

    public void removeBehaviour(Behaviour behaviour) {
        this.behaviours.remove(behaviour);
        this.world.removeBehaviour(behaviour);
    }

    public List<Behaviour> getBehaviours() {
        return behaviours;
    }

    public void remove() {
        for(Component component : components) {
            this.world.removeComponent(component);
        }

        if(transform != null)
            this.world.removeTransform(transform);

        for(Behaviour behaviour : behaviours) {
            this.world.removeBehaviour(behaviour);
        }

        for(Entity3D child : children) {
            child.remove();
        }

        this.world.removeEntity(this);
    }

    public void addChild(Entity3D child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void removeChild(Entity3D child) {
        child.remove();
        this.children.remove(child);
        this.world.removeEntity(child);
    }

    public Entity3D getChildByName(String name) {
        for(Entity3D child : children) {
            if(child.name.equals(name)) {
                return child;
            }
        }
        return null;
    }

    public float getTextureXOffset(){
        Material material = getComponent(Material.class);

        int column = textureIndex % material.getNumberOfRows();
        return (float) column / (float) material.getNumberOfRows();
    }

    public float getTextureYOffset(){
        Material material = getComponent(Material.class);

        int row = textureIndex / material.getNumberOfRows();
        return (float) row / (float)material.getNumberOfRows();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
