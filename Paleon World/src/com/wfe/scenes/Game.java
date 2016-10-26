package com.wfe.scenes;

import com.wfe.components.Image;
import com.wfe.components.Material;
import com.wfe.components.Model;
import com.wfe.components.Text;
import com.wfe.core.Display;
import com.wfe.core.IScene;
import com.wfe.core.ResourceManager;
import com.wfe.entities.DesertHouse;
import com.wfe.entities.Palm;
import com.wfe.entities.Settler;
import com.wfe.graph.Camera;
import com.wfe.graph.render.GUIRenderer;
import com.wfe.graph.transform.Transform2D;
import com.wfe.graph.transform.Transform3D;
import com.wfe.graph.water.WaterTile;
import com.wfe.math.Vector3f;
import com.wfe.physics.FEllipse;
import com.wfe.physics.FPlane;
import com.wfe.scenegraph.Entity;
import com.wfe.scenegraph.World;
import com.wfe.terrain.Terrain;
import com.wfe.terrain.TerrainGenerator;
import com.wfe.terrain.TexturePack;
import com.wfe.utils.CellInfo;
import com.wfe.utils.Color;
import com.wfe.utils.GameTime;
import com.wfe.utils.MathUtils;
import com.wfe.utils.Triangle;

public class Game implements IScene {
	
	private World world;
	
	@Override
	public void loadResources() {
		ResourceManager.loadTexture("rock", "rock");
		
		ResourceManager.loadTexture("gui/crosshair", "ui_crosshair");
		
		/*** Terrain Textures ***/;
		ResourceManager.loadTexture("terrain/sand", "sand");
		/*** *** ***/
		
		/*** Water Textures ***/
		ResourceManager.loadTexture("water/dudvMap", "dudvMap");
		ResourceManager.loadTexture("water/normalMap", "normalMap");
		
		/*** Settler ***/
		ResourceManager.loadTexture("models/settler/skin", "skin");

		ResourceManager.loadMesh("models/settler/body", "body");
		ResourceManager.loadMesh("models/settler/head", "head");
		ResourceManager.loadMesh("models/settler/leftArm", "rightArm");
		ResourceManager.loadMesh("models/settler/rightArm", "leftArm");
		ResourceManager.loadMesh("models/settler/leftForearm", "rightForearm");
		ResourceManager.loadMesh("models/settler/rightForearm", "leftForearm");
		ResourceManager.loadMesh("models/settler/hip", "hip");
		ResourceManager.loadMesh("models/settler/shin", "shin");

		ResourceManager.loadTexture("models/settler/eyes", "eyes");
		ResourceManager.loadMesh("models/settler/eyes", "eyes");
		/*** *** ***/
		
		/*** Conifer ***/
    	ResourceManager.loadTexture("models/conifer/trunk", "conifer_trunk");
        ResourceManager.loadMesh("models/conifer/trunk", "conifer_trunk");
        
        ResourceManager.loadTexture("models/conifer/leaves", "conifer_leaves");
        ResourceManager.loadMesh("models/conifer/leaves", "conifer_leaves");
        /*** *** ***/
        
        /*** Birch ***/
    	ResourceManager.loadTexture("models/birch/trunk", "birch_trunk");
        ResourceManager.loadMesh("models/birch/trunk", "birch_trunk");
        
        ResourceManager.loadTexture("models/birch/leaves2", "birch_leaves");
        ResourceManager.loadMesh("models/birch/leaves", "birch_leaves");
        /*** *** ***/
        
        /*** Rock ***/
        ResourceManager.loadMesh("models/rock/rock_1", "rock"); 
        /*** *** ***/
        
        /*** Grass ***/
        ResourceManager.loadTexture("models/grass/grass", "grass");
        ResourceManager.loadMesh("models/grass/grass", "grass");
        /*** *** ***/
        
        /*** Desert House ***/
        ResourceManager.loadTexture("models/desertHouse/desertHouse", "desertHouse");
        ResourceManager.loadMesh("models/desertHouse/desertHouse", "desertHouse");
        /*** *** ***/
        
        /*** Palm ***/
        ResourceManager.loadTexture("models/palm/palm", "palm");
        ResourceManager.loadMesh("models/palm/palm", "palm");
        /*** *** ***/
	}

	@Override
	public void init() throws Exception {
		Camera camera = new Camera(new Vector3f(0, 0, 0));
		world = new World(camera);
		
		/*** Terrain ***/
		TerrainGenerator generator = new TerrainGenerator();
		TexturePack texturePack = new TexturePack(
        		generator.getHeightMap(),
                generator.getBlendMapTexture(),
                ResourceManager.getTexture("sand"),
                ResourceManager.getTexture("sand"),
                ResourceManager.getTexture("sand"),
                ResourceManager.getTexture("sand"));

        Terrain terrain = new Terrain(0, 0, texturePack);
        world.addTerrain(terrain);
        
        for(int x = 0; x < 256; x++) {
            for(int z = 0; z < 256; z++) {
                float height = world.getTerrainHeight((x * 3) + 1.5f, (z * 3) + 1.5f);

                int cellState = 0;

                if(height < 3.9f)
                    cellState = 1;
                
                world.cells.put(x + " " + z, 
                		new CellInfo(new Vector3f(x * 3 + 1.5f, height, z * 3 + 1.5f), cellState));
            }
        }
        /*** *** ***/
		
		for(int i = 60; i < 840; i+= 120) {
			for(int j = 60; j < 840; j+= 120) {
				world.addWaterTile(new WaterTile(j, i));
			}
		}
		
        Entity text = new Entity(world, "Text");
        text.addComponent(new Text("Winter Fox Engine 0.1", GUIRenderer.primitiveFont, 1f, Color.WHITE));
        text.setTransform(new Transform2D());
        
        Entity rock = new Entity(world, "Rock");
        rock.addComponent(new Model(ResourceManager.getMesh("rock")));
        rock.addComponent(new Material(ResourceManager.getTexture("rock")));
        rock.setTransform(new Transform3D());
        rock.position.set(395, world.getTerrainHeight(395, 395), 395);
        rock.scale.set(0.65f);
        
        Settler settler = new Settler(world, camera);
        settler.position.set(380, world.getTerrainHeight(380, 420) + 2.2f, 420);
        settler.rotation.y = 180;
        
        Material grassMat = new Material(ResourceManager.getTexture("grass"));
        grassMat.useFakeLighting = true;
        grassMat.setNumberOfRows(2);
        grassMat.transparency = true;
        
        Entity grass = new Entity(world, "Grass");
        grass.setTransform(new Transform3D());
        grass.addComponent(new Model(ResourceManager.getMesh("grass")));
        grass.addComponent(grassMat);
        grass.position.set(400, world.getTerrainHeight(400, 400), 400);
        grass.textureIndex = 1;
        grass.scale.set(2.5f);
        
        Entity grass1 = new Entity(world, "Grass");
        grass1.setTransform(new Transform3D());
        grass1.addComponent(new Model(ResourceManager.getMesh("grass")));
        grass1.addComponent(grassMat);
        grass1.position.set(400, world.getTerrainHeight(400, 400), 400);
        grass1.rotation.y = 85;
        grass1.textureIndex = 3;
        grass1.scale.set(2.5f);
        
        Entity crosshair = new Entity(world, "Crosshair");
        crosshair.setTransform(new Transform2D());
        crosshair.addComponent(new Image(ResourceManager.getTexture("ui_crosshair")));
        crosshair.scale.set(32, 32);
        crosshair.position.set(Display.getWidth() / 2 - 16, Display.getHeight() / 2 - 16);
        
        DesertHouse desertHouse = new DesertHouse(world);
        desertHouse.position.set(410, world.getTerrainHeight(410, 410), 410);
        
        Palm palm = new Palm(world);
        palm.position.set(395, world.getTerrainHeight(395, 410), 410);
        
        GameTime.setTime(12, 00);
        
        FEllipse ellipse = new FEllipse(new Vector3f(0.5f, 0, 0.5f),
        		new Vector3f(3, 7, 3));
        ellipse.velocity.set(0, 0, 1);
        
        Triangle triangle = new Triangle(new Vector3f(1.0f, 0.0f, 1.0f),
        		new Vector3f(-1.0f, 0.0f, -1.0f),
        		new Vector3f(-1.0f, 0.0f, 1.0f));
        
        FPlane fplane = new FPlane(triangle.getPointN(0), triangle.getPointN(1), triangle.getPointN(2));
       
        float signedDistanceBasePoint = Vector3f.dot(fplane.normal, ellipse.position) + fplane.equation[3];
        
        float t0 = (1 - signedDistanceBasePoint) / Vector3f.dot(fplane.normal, ellipse.velocity);
        float t1 = (-1 - signedDistanceBasePoint) / Vector3f.dot(fplane.normal, ellipse.velocity);
        
        System.out.println(MathUtils.checkPointInTriangle(ellipse.position, 
        		triangle.getPointN(0), triangle.getPointN(1), triangle.getPointN(2)));
	}

	@Override
	public void update(float dt) throws Exception {
		world.update(dt);
		world.render();
	}

	@Override
	public void cleanup() {
		world.cleanup();
	}

}
