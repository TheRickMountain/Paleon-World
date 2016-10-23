package com.paleon.scenes;

import com.paleon.engine.IScene;
import com.paleon.engine.ResourceManager;
import com.paleon.engine.components.Model;
import com.paleon.engine.graph.Material;
import com.paleon.engine.graph.gui.ItemDatabase;
import com.paleon.engine.instances.Camera;
import com.paleon.engine.instances.WaterTile;
import com.paleon.engine.loaders.TextureLoader;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.terrain.Terrain;
import com.paleon.engine.terrain.TexturePack;
import com.paleon.engine.toolbox.GameTime;
import com.paleon.engine.toolbox.MathUtils;
import com.paleon.engine.toolbox.MyRandom;
import com.paleon.engine.weather.Skybox;
import com.paleon.engine.world.Player;
import com.paleon.game.building.BuildingGui;
import com.paleon.maths.vecmath.Vector3f;

public class Game implements IScene {

	public static enum State {
		GAME, INVENTORY
	}
	
	private Camera camera;
	
	private World world;
	
	public static State state;
	
	@Override
	public void loadResources() {
		/*** Textures ***/
		ResourceManager.loadTexture("/biomes/blendmap.png", "blendMap");
		ResourceManager.loadTexture("/textures/sand.png", "sand");
		ResourceManager.loadTexture("/textures/preview.png", "dirt");
		ResourceManager.loadTexture("/textures/dry_grass.png", "grass");
		
		ResourceManager.loadTexture("/prefabs/human/skin.png", "skin");
		ResourceManager.loadTexture("/prefabs/human/eyes.png", "eyes");
		
		ResourceManager.loadTexture("/prefabs/weapons/weapon.png", "weapon");
		
		// Water
		ResourceManager.loadTexture("/water/dudvMap.png", "dudvMap");
		ResourceManager.loadTexture("/water/normalMap.png", "normalMap");
		
		// Skybox
		ResourceManager.loadSkybox("sunset");
		ResourceManager.loadSkybox("sunny2");
		ResourceManager.loadSkybox("night");
		
		// GUI
		ResourceManager.loadTexture("/gui/icons/logs.png", "ui_logs");
		ResourceManager.loadTexture("/gui/icons/firewood.png", "ui_firewood");
		ResourceManager.loadTexture("/gui/icons/settler.png", "ui_settler");
		ResourceManager.loadTexture("/gui/icons/close.png", "ui_close");
		ResourceManager.loadTexture("/gui/icons/question.png", "ui_question");
		ResourceManager.loadTexture("/gui/elements/button.png", "ui_button");
		ResourceManager.loadTexture("/gui/slot.png", "ui_slot");
		ResourceManager.loadTexture("/gui/icons/flint.png", "ui_flint");
		ResourceManager.loadTexture("/gui/icons/bow.png", "ui_bow");
		ResourceManager.loadTexture("/gui/icons/stick.png", "ui_stick");
		ResourceManager.loadTexture("/gui/icons/shroom.png", "ui_shroom");
		ResourceManager.loadTexture("/gui/icons/wheat.png", "ui_wheat");
		ResourceManager.loadTexture("/gui/icons/seeds_of_wheat.png", "ui_seeds of wheat");
		ResourceManager.loadTexture("/gui/icons/rope.png", "ui_rope");
		ResourceManager.loadTexture("/gui/icons/fish.png", "ui_fish");
		ResourceManager.loadTexture("/gui/icons/stone_axe.png", "ui_stone axe");
		ResourceManager.loadTexture("/gui/icons/sharp_stone.png", "ui_sharp stone");
		ResourceManager.loadTexture("/gui/icons/apple.png", "ui_apple");
		ResourceManager.loadTexture("/gui/icons/flour.png", "ui_flour");
		ResourceManager.loadTexture("/gui/icons/bread.png", "ui_bread");
		ResourceManager.loadTexture("/gui/icons/dough.png", "ui_dough");
		ResourceManager.loadTexture("/gui/icons/leather.png", "ui_leather");
		ResourceManager.loadTexture("/gui/icons/clay_cup_of_water.png", "ui_clay cup of water");
		ResourceManager.loadTexture("/gui/icons/clay_cup.png", "ui_clay cup");
		ResourceManager.loadTexture("/gui/icons/wigwam.png", "ui_wigwam");
		ResourceManager.loadTexture("/gui/icons/beehive.png", "ui_beehive");
		ResourceManager.loadTexture("/gui/icons/character.png", "ui_character");
		ResourceManager.loadTexture("/gui/icons/craft.png", "ui_craft");
		ResourceManager.loadTexture("/gui/icons/building.png", "ui_building");
		
		ResourceManager.loadTexture("/gui/bar/bar_back.png", "bar_back");
		ResourceManager.loadTexture("/gui/bar/bar_front.png", "bar_front");
		ResourceManager.loadTexture("/gui/bar/health.png", "health");
		ResourceManager.loadTexture("/gui/bar/hunger.png", "hunger");
		ResourceManager.loadTexture("/gui/bar/thirst.png", "thirst");
		
		/*** Font ***/
		ResourceManager.loadTexture("/fonts/primitive_font.png", "primitive_font");
		/*** *** ***/
		
		/*** Bush ***/
		Material bushMat = new Material(TextureLoader.load("/prefabs/fern/fern.png"));
		bushMat.setNumberOfRows(2);
		bushMat.setHasTransparency(true);
		ResourceManager.loadMesh("/prefabs/fern/fern.obj", "fern", bushMat);
		/*** *** ***/
		
		/*** Birch ***/
		Material barkMat = new Material(TextureLoader.load("/prefabs/tree/pine/bark.png"));
		ResourceManager.loadMesh("/prefabs/tree/pine/bark.obj", "bark", barkMat);
		
		Material leavesMat = new Material(TextureLoader.load("/prefabs/tree/pine/leaves.png"));
		leavesMat.setHasTransparency(true);
		ResourceManager.loadMesh("/prefabs/tree/pine/leaves.obj", "leaves", leavesMat);
		/*** *** ***/
		
		/*** Shroom ***/
		Material shroomMat = new Material(TextureLoader.load("/prefabs/shroom/shroom.png"));
		ResourceManager.loadMesh("/prefabs/shroom/shroom_1.obj", "shroom_1", shroomMat);
		ResourceManager.loadMesh("/prefabs/shroom/shroom_2.obj", "shroom_2", shroomMat);
		ResourceManager.loadMesh("/prefabs/shroom/shroom_3.obj", "shroom_3", shroomMat);
		/*** *** ***/
		
		/*** Flint ***/
		Material flintMat = new Material(TextureLoader.load("/prefabs/flint/flint.png"));
		ResourceManager.loadMesh("/prefabs/flint/flint.obj", "flint", flintMat);
		/*** *** ***/
		
		/*** Stick ***/
		Material stickMat = new Material(TextureLoader.load("/prefabs/stick/stick.png"));
		ResourceManager.loadMesh("/prefabs/stick/stick.obj", "stick", stickMat);
		ResourceManager.loadMesh("/prefabs/logs/logs.obj", "logs", stickMat);
		/*** *** ***/
		
		/*** Fish Box ***/
		Material fishBoxMat = new Material(TextureLoader.load("/prefabs/fishBox/fishBox.png"));
		ResourceManager.loadMesh("/prefabs/fishBox/fishBox.obj", "fishBox", fishBoxMat);
		/*** *** ***/
		
		/*** Wheat ***/
		Material wheatMat = new Material(TextureLoader.load("/prefabs/wheat/wheat.png"));
		wheatMat.setHasTransparency(true);
		wheatMat.setUseFakeLighting(true);
		ResourceManager.loadMesh("/prefabs/wheat/wheat.obj", "wheat", wheatMat);
		/*** *** ***/
		
		/*** Bow ***/
		Material weaponMaterial = new Material(ResourceManager.getTexture("weapon"));
		ResourceManager.loadMesh("/prefabs/weapons/bow.obj", "bow", weaponMaterial);					
		/*** *** ***/
		
		/*** Firewood ***/
		Material firewoodMat = new Material(ResourceManager.loadTexture("/prefabs/firewood/firewood.png", "firewood"));
		ResourceManager.loadMesh("/prefabs/firewood/firewood.obj", "firewood", firewoodMat);
		/*** *** ***/
		
		/*** Rock ***/
		Material rockMaterial = new Material(ResourceManager.loadTexture("/prefabs/rock/rock_2.png", "rock"));
		ResourceManager.loadMesh("/prefabs/rock/rock_1.obj", "rock", rockMaterial);
		/*** *** ***/
		
		/*** Grass ***/
		Material grassMaterial = new Material(ResourceManager.loadTexture("/prefabs/grass/diffuse.png", "diffuse"));
		grassMaterial.setHasTransparency(true);
		grassMaterial.setUseFakeLighting(true);
		grassMaterial.setNumberOfRows(2);
		ResourceManager.loadMesh("/prefabs/grass/grass.obj", "grass", grassMaterial);
		/*** *** ***/
		
		/*** Buildings ***/
		// Barn
		Material barnMat = new Material(ResourceManager.loadTexture("/buildings/barn/barn.png", "barn"));
		
		ResourceManager.loadTexture("/buildings/barn/barn_icon.png", "barn_icon");
		ResourceManager.loadMesh("/buildings/barn/barn.obj", "barn", barnMat);
		
		Material houseMaterial = new Material(ResourceManager.loadTexture("/buildings/house/house.png", "house"));
		
		// House
		ResourceManager.loadTexture("/buildings/house/house_icon.png", "house_icon");
		ResourceManager.loadMesh("/buildings/house/house.obj", "house", houseMaterial);
		
		// Forester
		ResourceManager.loadTexture("/buildings/forester/forester_icon.png", "forester_icon");
		ResourceManager.loadMesh("/buildings/forester/forester.obj", "forester", houseMaterial);
		
		// Fishing Dock
		ResourceManager.loadTexture("/buildings/fishingDock/fishingDock_icon.png", "fishingDock_icon");
		ResourceManager.loadMesh("/buildings/fishingDock/fishingDock.obj", "fishingDock", houseMaterial);
		
		// Woodcutter
		ResourceManager.loadTexture("/buildings/woodcutter/woodcutter_icon.png", "woodcutter_icon");
		ResourceManager.loadMesh("/buildings/woodcutter/woodcutter.obj", "woodcutter", houseMaterial);
		/*** *** ***/
		
		/*** Player ***/
		Material playerMat = new Material(ResourceManager.getTexture("skin"));
		
		Material eyesMat = new Material(ResourceManager.getTexture("eyes"));
		eyesMat.setReflectivity(1);
		eyesMat.setShineDamper(10);
		
		ResourceManager.loadMesh("/prefabs/human/eyes.obj", "eyes", eyesMat);
		
		ResourceManager.loadMesh("/prefabs/human/rightArm.obj", "leftArm", playerMat);
		ResourceManager.loadMesh("/prefabs/human/leftArm.obj", "rightArm", playerMat);
		ResourceManager.loadMesh("/prefabs/human/leftForearm.obj", "rightForearm", playerMat);
		ResourceManager.loadMesh("/prefabs/human/rightForearm.obj", "leftForearm", playerMat);
		ResourceManager.loadMesh("/prefabs/human/body.obj", "body", playerMat);
		ResourceManager.loadMesh("/prefabs/human/head.obj", "head", playerMat);
		ResourceManager.loadMesh("/prefabs/human/hip.obj", "hip", playerMat);
		ResourceManager.loadMesh("/prefabs/human/shin.obj", "shin", playerMat);
		/*** *** ***/
	}
	
	@Override
	public void init() {
		ItemDatabase.init();
		
		camera = new Camera(new Vector3f(400, 3.92f, 400));
		
		world = new World(camera);
		
		TexturePack texturePack = new TexturePack("blendMap", "sand", "dirt", "dirt", "grass");		
		Terrain terrain1 = new Terrain(0, 0, "/biomes/heightmap.png", texturePack);
		world.addTerrain(terrain1);
		
		for(int i = 60; i < 840; i+= 120) {
			for(int j = 60; j < 840; j+= 120) {
				world.addWaterTile(new WaterTile(j, i));
			}
		}
		
		GameTime.init();
		
		state = State.GAME;
		
		GameTime.setTime(12, 00);
		
		world.setSkybox(new Skybox("sunset", "sunny2", "night"));
		
		/*** Environment generation ***/
		
		// Fern
		for(int i = 0; i < 20; i++) {
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity gameObject = new Entity(world);
				gameObject.addComponent(new Model(ResourceManager.getMesh("fern")));
				gameObject.position.set(x, world.getTerrainHeight(x, z), z);
				gameObject.scale.set(1);
				gameObject.setTextureIndex(MyRandom.nextInt(4));
				gameObject.setFurthestPoint(ResourceManager.getMesh("fern").getFurthestPoint());
				world.addEntity(gameObject);
			}
		}
		
		// Rock
		for(int i = 0; i < 20; i++) {
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity gameObject = new Entity(world);
				gameObject.addComponent(new Model(ResourceManager.getMesh("rock")));
				gameObject.position.set(x, world.getTerrainHeight(x, z), z);
				gameObject.scale.set(0.8f);
				gameObject.setFurthestPoint(ResourceManager.getMesh("rock").getFurthestPoint());
				world.addEntity(gameObject);
			}
		}
		
		// Flint
		for(int i = 0; i < 25; i++) {
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity gameItem = new Entity(world);
				gameItem.addComponent(new Model(ResourceManager.getMesh("flint")));
				gameItem.position.set(x, world.getTerrainHeight(x, z), z);
				gameItem.scale.set(0.5f);
				gameItem.setID(MathUtils.genID());
				gameItem.setGuiID(4);
				gameItem.setFurthestPoint(ResourceManager.getMesh("flint").getFurthestPoint());
				world.addEntity(gameItem);
			}
		}
		
		// Shroom
		for(int i = 0; i < 50; i++) {
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity gameItem = null;
				switch(MyRandom.nextInt(3) + 1){
				case 1:
					gameItem = new Entity(world);
					gameItem.addComponent(new Model(ResourceManager.getMesh("shroom_1")));
					gameItem.setFurthestPoint(ResourceManager.getMesh("shroom_1").getFurthestPoint());
					break;
				case 2:
					gameItem = new Entity(world);
					gameItem.addComponent(new Model(ResourceManager.getMesh("shroom_2")));
					gameItem.setFurthestPoint(ResourceManager.getMesh("shroom_2").getFurthestPoint());
					break;
				case 3:
					gameItem = new Entity(world);
					gameItem.addComponent(new Model(ResourceManager.getMesh("shroom_3")));
					gameItem.setFurthestPoint(ResourceManager.getMesh("shroom_3").getFurthestPoint());
					break;
				}
				gameItem.position.set(x, world.getTerrainHeight(x, z), z);
				gameItem.scale.set(0.75f);
				gameItem.setID(MathUtils.genID());
				gameItem.setGuiID(1);
				world.addEntity(gameItem);
			}
		}
		
		// Stick
		for(int i = 0; i < 25; i++) {
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity gameItem = new Entity(world);
				gameItem.addComponent(new Model(ResourceManager.getMesh("stick")));
				gameItem.position.set(x, world.getTerrainHeight(x, z), z);
				gameItem.scale.set(1.5f);
				gameItem.setID(MathUtils.genID());
				gameItem.setGuiID(2);
				gameItem.setFurthestPoint(ResourceManager.getMesh("stick").getFurthestPoint());
				world.addEntity(gameItem);
			}
		}
		
		// Wheat
		for(int i = 0; i < 25; i++) {
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity gameItem = new Entity(world);
				gameItem.addComponent(new Model(ResourceManager.getMesh("wheat")));
				gameItem.setUseWaving(true);
				gameItem.position.set(x, world.getTerrainHeight(x, z), z);
				gameItem.scale.set(1.5f);
				gameItem.setID(MathUtils.genID());
				gameItem.setGuiID(3);
				gameItem.setFurthestPoint(ResourceManager.getMesh("wheat").getFurthestPoint());
				world.addEntity(gameItem);
			}
		}
		
		// Tree
		for(int i = 0; i < 100; i++) {
			int id = MathUtils.genID();
			
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity bark = new Entity("Tree", world);
				bark.addComponent(new Model(ResourceManager.getMesh("bark")));
				bark.position.set(x, world.getTerrainHeight(x, z), z);
				bark.scale.set(3f);
				bark.setFurthestPoint(ResourceManager.getMesh("bark").getFurthestPoint());
				bark.setID(id);
				world.addEntity(bark);
				
				Entity leaves = new Entity(world);
				leaves.addComponent(new Model(ResourceManager.getMesh("leaves")));
				leaves.setFurthestPoint(ResourceManager.getMesh("leaves").getFurthestPoint());
				leaves.setID(id);
				bark.addChild(leaves);
			}
		}
		
		// Grass
		for(int i = 0; i < 100; i++) {
			float x = MyRandom.nextFloat(800);
			float z = MyRandom.nextFloat(800);
			if(world.getTerrainHeight(x,  z) > 3) {
				Entity grass = new Entity(world);
				grass.addComponent(new Model(ResourceManager.getMesh("grass")));
				grass.position.set(x, world.getTerrainHeight(x, z), z);
				grass.scale.set(3);
				grass.setTextureIndex(MyRandom.nextInt(3));
				grass.setFurthestPoint(ResourceManager.getMesh("grass").getFurthestPoint());
				grass.setUseWaving(true);
				world.addEntity(grass);
			}
		}
		
		/*** *** ***/
		
		for(int i = 0; i < 6; i++) {
			Player player = new Player(MathUtils.getRandomName(), "Settler", world);	
			player.position.set(400 + i * 10, 20, 400);
			player.rotation.y = 180;
			world.addEntity(player);
		}
		
		BuildingGui building = new BuildingGui(world);
		world.addEntity(building);
	}

	@Override
	public void update(float dt) {
		// Updating
		GameTime.update();
		
		if(state == State.GAME) {
			camera.update(dt);
			camera.move(dt);
		}
	
		world.update(dt);
		
		// Rendering
		world.render(camera);
	}

	@Override
	public void cleanup() {
		world.cleanup();
	}

}
