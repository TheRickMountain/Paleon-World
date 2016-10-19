package com.paleon.engine.scenegraph;

import com.paleon.engine.behaviours.Behaviour;
import com.paleon.engine.components.*;
import com.paleon.engine.graph.Camera;
import com.paleon.engine.graph.Light;
import com.paleon.engine.graph.Mesh;
import com.paleon.engine.graph.render.GUIRenderer;
import com.paleon.engine.graph.render.MeshRenderer;
import com.paleon.engine.graph.render.SkyboxRenderer;
import com.paleon.engine.graph.render.TerrainRenderer;
import com.paleon.engine.graph.transform.Transform;
import com.paleon.engine.input.Key;
import com.paleon.engine.input.Keyboard;
import com.paleon.engine.terrain.Terrain;
import com.paleon.engine.terrain.TerrainBlock;
import com.paleon.engine.utils.CellInfo;
import com.paleon.engine.utils.Color;
import com.paleon.engine.utils.MousePicker;
import com.paleon.engine.utils.OpenglUtils;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Created by Rick on 06.10.2016.
 */
public class World {

    private final MeshRenderer meshRenderer;
    private final TerrainRenderer terrainRenderer;
    private final SkyboxRenderer skyboxRenderer;
    private final GUIRenderer guiRenderer;

    public Map<Terrain, List<TerrainBlock>> terrains = new HashMap<Terrain, List<TerrainBlock>>();

    private static final int WORLD_TERRAIN_WIDTH = 3;
    private static final int WORLD_TILE_WIDTH = WORLD_TERRAIN_WIDTH * Terrain.TERRAIN_WIDTH_BLOCKS;

    private final TerrainBlock[][] terrainGrid;

    private final List<Entity> entities = new ArrayList<>();
    private final List<Component> guis = new ArrayList<>();
    private final Map<Mesh, List<Entity>> meshes = new HashMap<>();

    private final List<Behaviour> behaviours = new ArrayList<>();
    private final List<Behaviour> behavioursToAdd = new ArrayList<>();
    private final List<Behaviour> behavioursToRemove = new ArrayList<>();

    private final List<Transform> transforms = new ArrayList<>();
    private final List<Transform> transformsToAdd = new ArrayList<>();
    private final List<Transform> transformsToRemove = new ArrayList<>();

    public final Map<String, CellInfo> cells = new HashMap<>();

    private Camera camera;
    private final Light light;

    private boolean wireframeMode = false;
    public boolean onGuiLayer = false;

    public World(Camera camera) {
        this.camera = camera;

        meshRenderer = new MeshRenderer(camera);
        terrainRenderer = new TerrainRenderer(camera);
        skyboxRenderer = new SkyboxRenderer(camera);
        guiRenderer = new GUIRenderer();

        terrainGrid = new TerrainBlock[WORLD_TILE_WIDTH][WORLD_TILE_WIDTH];

        MousePicker.setUpMousePicker(this, camera);

        light = new Light(new Vector3f(0, 1000, 1500), new Color(255, 255, 180));
    }

    public void update(float dt) {
        camera.update();
        camera.rotate(dt);
        camera.move(dt);

        MousePicker.update();

        for(Behaviour bh : behaviours) {
        	if(bh.active)
        		bh.update(dt);
        }

        if(!behavioursToAdd.isEmpty()) {
            for(Behaviour bh : behavioursToAdd) {
                behaviours.add(bh);
            }
            behavioursToAdd.clear();
        }

        if(!behavioursToRemove.isEmpty()) {
            for(Behaviour bh : behavioursToRemove) {
                behaviours.remove(bh);
            }
            behavioursToRemove.clear();
        }

        for(Transform tr : transforms) {
        	if(tr.active)
        		tr.update();
        }

        if(!transformsToAdd.isEmpty()) {
            for(Transform tr : transformsToAdd) {
                transforms.add(tr);
            }
            transformsToAdd.clear();
        }

        if(!transformsToRemove.isEmpty()) {
            for(Transform tr : transformsToRemove) {
                transforms.remove(tr);
            }
            transformsToRemove.clear();
        }

        if(Keyboard.isKeyDown(Key.F5)) {
            wireframeMode = !wireframeMode;
            OpenglUtils.wireframeMode(wireframeMode);
        }
    }

    public void render() {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        meshRenderer.render(meshes, light, camera);
        terrainRenderer.render(terrains, light, camera);
        skyboxRenderer.render(camera);
        guiRenderer.render(guis);
    }

    public void addTerrain(Terrain terrain){
        if(terrain.getGridX() > WORLD_TERRAIN_WIDTH || terrain.getGridZ() > WORLD_TERRAIN_WIDTH) {
            System.err.println("World not large enough to add terrain at " + terrain.getGridX());
            return;
        }

        List<TerrainBlock> terrainBlocks = new ArrayList<>();
        for(TerrainBlock terrainBlock : terrain.getTerrainBlocks()){
            int index = terrainBlock.getIndex();
            int gridX = (index % Terrain.TERRAIN_WIDTH_BLOCKS)
                    + (terrain.getGridX() * Terrain.TERRAIN_WIDTH_BLOCKS);
            int gridZ = (int) (Math.floor(index / Terrain.TERRAIN_WIDTH_BLOCKS) + (terrain
                    .getGridZ() * Terrain.TERRAIN_WIDTH_BLOCKS));
            terrainGrid[gridX][gridZ] = terrainBlock;
            terrainBlocks.add(terrainBlock);
        }
        terrains.put(terrain, terrainBlocks);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public Entity getEntityByName(String name) {
        for (Entity e:
                entities) {
            if(e.name.equals(name)) return e;
        }
        return null;
    }

    protected void addComponent(Component component) {
        if(component.type.equals(Component.Type.MODEL)) {
            Model model = (Model) component;
            Mesh mesh = model.mesh;
            List<Entity> batch = meshes.get(mesh);
            if(batch != null) {
                batch.add(model.parent);
            } else {
                List<Entity> newBatch = new ArrayList<Entity>();
                newBatch.add(model.parent);
                meshes.put(mesh, newBatch);
            }
        } else if(component.type.equals(Component.Type.IMAGE)) {
            Image image = (Image) component;
            guis.add(image);
        } else if(component.type.equals(Component.Type.TEXT)) {
            Text text = (Text) component;
            guis.add(text);
        }
    }

    protected void removeComponent(Component component) {
        if(component.type.equals(Component.Type.MODEL)) {
            Model model = (Model) component;
            Mesh mesh = model.mesh;
            List<Entity> batch = meshes.get(mesh);
            batch.remove(model.parent);
            if(batch.isEmpty()) {
                meshes.remove(mesh);
            }
        } else if(component.type.equals(Component.Type.IMAGE)) {
            Image image = (Image) component;
            guis.remove(image);
        } else if(component.type.equals(Component.Type.TEXT)) {
            Text text = (Text) component;
            guis.remove(text);
        }
    }

    protected void addBehaviour(Behaviour behaviour) {
        behaviour.start();
        behavioursToAdd.add(behaviour);
    }

    protected void removeBehaviour(Behaviour behaviour) {
        behavioursToRemove.add(behaviour);
    }

    protected void addTransform(Transform transform) {
        transformsToAdd.add(transform);
    }

    protected void removeTransform(Transform transform) {
        transformsToRemove.add(transform);
    }

    public void cleanup() {
        meshRenderer.cleanup();
        terrainRenderer.cleanup();
        skyboxRenderer.cleanup();
        guiRenderer.cleanup();
    }

    public float getTerrainHeight(float x, float z){
        TerrainBlock block = getTerrainForPosition(x, z);
        if(block == null){
            return 0;
        }
        float terrainX = x - (block.getX() - Terrain.BLOCK_SIZE / 2);
        float terrainZ = z - (block.getZ() - Terrain.BLOCK_SIZE / 2);
        return block.calcHeight(terrainX, terrainZ);
    }

    public TerrainBlock getTerrainForPosition(float x, float z) {
        int terrain_i = (int) Math.floor(x / Terrain.BLOCK_SIZE);
        int terrain_j = (int) Math.floor(z / Terrain.BLOCK_SIZE);
        if (terrain_i < 0 || terrain_j < 0) {
            return null;
        }
        if (terrain_i >= terrainGrid.length) {
            return null;
        } else if (terrain_j >= terrainGrid[terrain_i].length) {
            return null;
        }
        return terrainGrid[terrain_i][terrain_j];
    }

}
