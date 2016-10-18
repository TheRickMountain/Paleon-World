package com.paleon.engine.scenes;

import com.paleon.engine.components.Model;
import com.paleon.engine.components.Text;
import com.paleon.engine.core.Display;
import com.paleon.engine.graph.render.GUIRenderer;
import com.paleon.engine.graph.transform.Transform2D;
import com.paleon.engine.graph.transform.Transform3D;
import com.paleon.engine.core.IScene;
import com.paleon.engine.core.ResourceManager;
import com.paleon.engine.graph.Camera;
import com.paleon.engine.components.Material;
import com.paleon.engine.scenegraph.Entity;
import com.paleon.engine.scenegraph.World;
import com.paleon.engine.terrain.Terrain;
import com.paleon.engine.terrain.TexturePack;
import com.paleon.engine.utils.CellInfo;
import com.paleon.engine.utils.Color;
import com.paleon.engine.utils.OpenglUtils;
import com.paleon.engine.world.BuildingGuiBh;
import com.paleon.engine.world.Button;
import com.paleon.engine.world.SettlerPf;
import org.joml.Vector3f;

/**
 * Created by Rick on 06.10.2016.
 */
public class Game implements IScene {

    World world;

    @Override
    public void loadResources() {
        ResourceManager.loadTexture("/textures/rock.png", "rock");
        ResourceManager.loadTexture("/textures/house.png", "house");

        ResourceManager.loadTexture("/terrainTextures/blendmap.png", "blendmap");
        ResourceManager.loadTexture("/terrainTextures/ground.png", "ground");
        ResourceManager.loadTexture("/terrainTextures/grass.png", "grass");
        ResourceManager.loadTexture("/terrainTextures/sand.png", "sand");

        ResourceManager.loadSkybox("sunny");

        ResourceManager.loadMaterial(new Material(ResourceManager.getTexture("rock"), Color.WHITE), "rock");
        ResourceManager.loadMaterial(new Material(ResourceManager.getTexture("sand"), Color.YELLOW), "sand");
        ResourceManager.loadMesh("/models/box.obj", "box");
        ResourceManager.loadMesh("/models/sphere.obj", "sphere");

        ResourceManager.loadTexture("/settler/skin.png", "skin");
        ResourceManager.loadMaterial(new Material(ResourceManager.getTexture("skin")), "skin");

        ResourceManager.loadMesh("/settler/body.obj", "body");
        ResourceManager.loadMesh("/settler/head.obj", "head");
        ResourceManager.loadMesh("/settler/leftArm.obj", "rightArm");
        ResourceManager.loadMesh("/settler/rightArm.obj", "leftArm");
        ResourceManager.loadMesh("/settler/leftForearm.obj", "rightForearm");
        ResourceManager.loadMesh("/settler/rightForearm.obj", "leftForearm");
        ResourceManager.loadMesh("/settler/hip.obj", "hip");
        ResourceManager.loadMesh("/settler/shin.obj", "shin");

        ResourceManager.loadTexture("/settler/eyes.png", "eyes");
        Material eyesMaterial = new Material(ResourceManager.getTexture("eyes"));
        eyesMaterial.useSpecular = true;
        ResourceManager.loadMaterial(eyesMaterial, "eyes");
        ResourceManager.loadMesh("/settler/eyes.obj", "eyes");

        ResourceManager.loadTexture("/grass/diffuse.png", "diffuse");
        Material grassMaterial = new Material(ResourceManager.getTexture("diffuse"));
        grassMaterial.setNumberOfRows(2);
        grassMaterial.useFakeLighting = true;
        grassMaterial.transparency = true;
        ResourceManager.loadMaterial(grassMaterial, "grass");
        ResourceManager.loadMesh("/grass/grass.obj", "grass");

        ResourceManager.loadTexture("/textures/wheat.png", "wheat");
        Material wheatMaterial = new Material(ResourceManager.getTexture("wheat"));
        wheatMaterial.useFakeLighting = true;
        wheatMaterial.transparency = true;
        ResourceManager.loadMaterial(wheatMaterial, "wheat");
        ResourceManager.loadMesh("/models/wheat.obj", "wheat");

        ResourceManager.loadTexture("/gui/axe.png", "axe");

        ResourceManager.loadTexture("/models/plane/plane.png", "plane");
        ResourceManager.loadMesh("/models/plane/plane.obj", "plane");

        ResourceManager.loadTexture("/models/house/house.png", "house");
        ResourceManager.loadMaterial(new Material(ResourceManager.getTexture("house"), new Color(1.0f, 1.0f, 1.0f)), "house");
        ResourceManager.loadMesh("/models/house/house.obj", "house");
    }

    @Override
    public void init() {
        Camera camera = new Camera(new Vector3f(400, 3.92f, 400));

        world = new World(camera);

        OpenglUtils.depthTest(true);
        OpenglUtils.cullFace(true);

        /*** Terrain ***/
        TexturePack texturePack = new TexturePack(
                ResourceManager.getTexture("blendmap"),
                ResourceManager.getTexture("grass"),
                ResourceManager.getTexture("grass"),
                ResourceManager.getTexture("ground"),
                ResourceManager.getTexture("sand"));

        Terrain terrain = new Terrain(0, 0, "/heightmap.png", texturePack);
        world.addTerrain(terrain);

        for(int x = 0; x < 256; x++) {
            for(int z = 0; z < 256; z++) {
                float height = world.getTerrainHeight((x * 3) + 1.5f, (z * 3) + 1.5f);

                int cellState = 0;

                if(height < 3.9f)
                    cellState = 1;

                world.cells.put(String.valueOf(x * 3 + 1.5f) + "," + String.valueOf(z * 3 + 1.5f), new CellInfo(x, z, cellState));
            }
        }
        /*** *** ***/

        /*** Settler ***/
        SettlerPf settlerPf = new SettlerPf(world, camera);
        settlerPf.rotation.y = 180;
        /*** *** ***/

        /*** Grass ***/
        for(int i = 0; i < 2; i++) {
            Entity grass = new Entity(world, "Grass");
            grass.addComponent(new Model(ResourceManager.getMesh("grass")));
            grass.addComponent(ResourceManager.getMaterial("grass"));
            grass.setTransform(new Transform3D());
            grass.position.set(400 + i * 3, world.getTerrainHeight(400 + i * 3, 400), 400);
            grass.scale.set(2.5f, 2.5f, 2.5f);
            grass.textureIndex = i * 2;
        }
        /*** *** ***/

        /*** Text ***/
        Entity text = new Entity(world, "Text");
        text.addComponent(new Text("PALEON 0.1.0", GUIRenderer.primitiveFont, 1f, Color.WHITE));
        text.setTransform(new Transform2D());
        /*** *** ***/

        /*** GUI ***/
        Button button = new Button(world, "Building Button", ResourceManager.getTexture("axe"));
        button.scale.x = 64;
        button.scale.y = 64;
        button.position.x = Display.getWidth() - button.scale.x;
        button.position.y = Display.getHeight() - button.scale.y;

        Entity building = new Entity(world, "Building");
        building.addBehaviour(new BuildingGuiBh(button));
        /*** *** ***/
    }

    @Override
    public void update(float deltaTime) {
        world.update(deltaTime);
        world.render();
    }

    @Override
    public void cleanup() {
        world.cleanup();
        ResourceManager.cleanup();
    }

}
