package com.paleon.engine.scenegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.paleon.engine.Display;
import com.paleon.engine.graph.RenderEngine;
import com.paleon.engine.graph.systems.GUIRendererSystem;
import com.paleon.engine.input.Key;
import com.paleon.engine.input.Keyboard;
import com.paleon.engine.input.Mouse;
import com.paleon.engine.instances.Camera;
import com.paleon.engine.instances.Light;
import com.paleon.engine.instances.WaterTile;
import com.paleon.engine.terrain.Terrain;
import com.paleon.engine.terrain.TerrainBlock;
import com.paleon.engine.toolbox.Color;
import com.paleon.engine.toolbox.ColorPicker;
import com.paleon.engine.toolbox.GameTime;
import com.paleon.engine.toolbox.MathUtils;
import com.paleon.engine.toolbox.MousePicker;
import com.paleon.engine.toolbox.MyRandom;
import com.paleon.engine.toolbox.OpenglUtils;
import com.paleon.engine.water.WaterFrameBuffers;
import com.paleon.engine.weather.Skybox;
import com.paleon.engine.weather.Weather;
import com.paleon.maths.vecmath.Vector3f;
import com.paleon.maths.vecmath.Vector4f;

public class World {
	
	private RenderEngine renderEngine;
	public static GUIRendererSystem guiRendererSystem;
	
	public Map<Terrain, List<TerrainBlock>> terrains = new HashMap<Terrain, List<TerrainBlock>>();
	
	private static final int WORLD_TERRAIN_WIDTH = 3;
	private static final int WORLD_TILE_WIDTH = WORLD_TERRAIN_WIDTH * Terrain.TERRAIN_WIDTH_BLOCKS;
	
	private final TerrainBlock[][] terrainGrid;
	
	private final List<Entity> entities = new ArrayList<Entity>();
	private final List<Entity> entitiesToAdd = new ArrayList<Entity>();
	
	private WaterFrameBuffers fbos;
	private final List<WaterTile> waterTiles = new ArrayList<WaterTile>();
	
	private Skybox skybox;
	
	public Weather weather;
	
	public Light sun;
	
	private boolean wireframeMode = false;
	private boolean colorMode = false;
	
	public int COLOR_PICKING_ID = -1;
	
	private Entity colorPickedObject;
	
	public World(Camera camera) {
		fbos = new WaterFrameBuffers();
		renderEngine = RenderEngine.getInstance(camera);
		guiRendererSystem = new GUIRendererSystem();
		
		terrainGrid = new TerrainBlock[WORLD_TILE_WIDTH][WORLD_TILE_WIDTH];
		
		weather = new Weather();
		
		sun = new Light(new Vector3f(-200000, 2000000, 800000), new Color(250, 220, 190));
		
		MousePicker.setUpMousePicker(this, camera);
	}
	
	public void update(float dt) {		
		renderEngine.update(dt);
		MousePicker.update();
		
		weather.updateWeather(GameTime.getATime());
		sun.setDiffuse(weather.getSunLightColor());	
		skybox.update(dt);
		
		Iterator<Entity> goIter = entities.iterator();
		while(goIter.hasNext()){
			Entity gameObject = goIter.next();
			gameObject.update(dt);
			
			if(Mouse.isButtonDown(1)) {
				if(COLOR_PICKING_ID != 0) {
					if(gameObject.getID() == COLOR_PICKING_ID) {
						colorPickedObject = gameObject;
					}
				} else {
					colorPickedObject = null;
				}
			}
			
			if(gameObject.isRemoved()) {
				goIter.remove();
			}
		}
		
		if(!entitiesToAdd.isEmpty()) {
			for(Entity e : entitiesToAdd) {
				entities.add(e);
			}
			
			entitiesToAdd.clear();
		}
		
		if(Keyboard.isKeyDown(Key.F5)) {
			wireframeMode = !wireframeMode;
			OpenglUtils.wireframeMode(wireframeMode);
		}
		
		if(Keyboard.isKeyDown(Key.F6)) {
			colorMode = !colorMode;
		}
	}
	
	public void render(Camera camera) {		
		if(Display.wasResized()) {
			camera.updateProjectionMatrix();
		}
		
		// Render in color mode
		RenderEngine.clear(0, 0, 0);
		renderEngine.renderColor(entities, camera);
		COLOR_PICKING_ID = ColorPicker.getId(Mouse.getX(), Mouse.getY());
		
		if(!colorMode) {
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			// Render reflection texture
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - WaterTile.HEIGHT);
			camera.getPosition().y -= distance;
			camera.invertPitch();
	
			RenderEngine.clear(0.98f, 0.9f, 0.78f);
			renderEngine.render(entities, sun, weather.getFogColor(), new Vector4f(0, 1, 0, -WaterTile.HEIGHT), camera);
			renderEngine.render(terrains, sun, weather.getFogColor(), new Vector4f(0, 1, 0, -WaterTile.HEIGHT), camera);
			if(skybox != null) {
				renderEngine.render(skybox, weather.getFogColor(), camera);
			}
	
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			// Render refraction texture
			fbos.bindRefractionFrameBuffer();
	
			RenderEngine.clear(0.98f, 0.9f, 0.78f);
			renderEngine.render(entities, sun, weather.getFogColor(), new Vector4f(0, -1, 0, WaterTile.HEIGHT), camera);
			renderEngine.render(terrains, sun, weather.getFogColor(), new Vector4f(0, -1, 0, WaterTile.HEIGHT), camera);
			if(skybox != null) {
				renderEngine.render(skybox, weather.getFogColor(), camera);
			}
			
			// Render to the world
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();
			RenderEngine.clear(0.98f, 0.9f, 0.78f);
			
			renderEngine.render(entities, sun, weather.getFogColor(), new Vector4f(0, 1, 0, 100000), camera);
			renderEngine.render(terrains, sun, weather.getFogColor(), new Vector4f(0, 1, 0, 100000), camera);
			if(skybox != null) {
				renderEngine.render(skybox, weather.getFogColor(), camera);
			}
			renderEngine.render(waterTiles, sun, weather.getFogColor(), fbos, camera);
			
			// Render to the screen
			guiRendererSystem.startRender();
			guiRendererSystem.render(entities);
			for(Entity go : entities) {
				go.onGUI();
			}
			guiRendererSystem.endRender();
		}
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

	public void addEntity(Entity entity) {		
		entity.init();
		entitiesToAdd.add(entity);
	}
	
	public void addEntity(Entity entity, boolean inverse) {		
		entity.init();
		entitiesToAdd.add(entity);
	}
	
	public void addWaterTile(WaterTile waterTile) {
		waterTiles.add(waterTile);
	}
	
	public void setSkybox(Skybox skybox) {
		this.skybox = skybox;
	}
	
	public int getColorPickingId(){
		return COLOR_PICKING_ID;
	}

	public void cleanup() {
		fbos.cleanup();
		
		renderEngine.cleanup();
		
		if(skybox != null) {
			skybox.cleanup();
		}
		
		guiRendererSystem.cleanup();
	}
	
	public Entity getColorPickedObject() {
		return colorPickedObject;
	}
	
	public Entity getEntityByName(String name) {
		for(Entity entity : entities) {
			if(entity.name.equals(name)) {
				return entity;
			}
		}
		return null;
	}
	
	public Entity getRandomEntityByName(String name) {
		List<Entity> temp = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(entity.name.equals(name)) {
				temp.add(entity);
			}
		}
		
		return temp.get(MyRandom.nextInt(temp.size()));
	}
	
	public List<Entity> getEntityListByName(String name) {
		List<Entity> temp = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(entity.name.equals(name)) {
				temp.add(entity);
			}
		}
		
		return temp;
	}
	
	public int getEntityCountInRangeByName(String name, Vector3f origin, int range) {
		List<Entity> temp = new ArrayList<Entity>();
		for(Entity entity : entities) {
			if(entity.name.equals(name)) {
				if(MathUtils.getDistanceBetweenPoints(entity.position, origin) <= 200) {
					temp.add(entity);
				}
			}
		}
		
		return temp.size();
	}

}
