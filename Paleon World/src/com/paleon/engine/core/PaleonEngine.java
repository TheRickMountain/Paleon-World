package com.paleon.engine.core;

import com.paleon.engine.scenes.Game;
import com.paleon.engine.scenes.Menu;
import com.paleon.engine.input.Keyboard;
import com.paleon.engine.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * Created by Rick on 06.10.2016.
 */
public class PaleonEngine implements Runnable {

    private final Thread gameLoopThread;

    private Display display;

    private SceneManager scene;

    public PaleonEngine() {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        display = new Display();
        scene = new SceneManager();
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            dispose();
        }
    }

    protected void init() throws Exception {
        display.create("Paleon 0.0.1a", 1152, 648);
        initScenes();
    }

    public void initScenes() throws Exception {
        SceneManager.add("Menu", new Menu());
        SceneManager.add("Game", new Game());
        SceneManager.change("Menu");
    }

    public void gameLoop() throws Exception {
        while (!display.isCloseRequested()) {
            Keyboard.startEventFrame();
            Mouse.startEventFrame();

            float deltaTime = Time.getDeltaTime();

            if (deltaTime >= 1) {
                deltaTime = 0;
            }

            update(deltaTime);

            Keyboard.clearEventFrame();
            Mouse.clearEventFrame();

            if (Display.wasResized()) {
                GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
                Display.setResized(false);
            }
        }
    }

    protected void update(float deltaTime) throws Exception {
        display.pollEvents();
        scene.update(deltaTime);
        display.swapBuffers();
        Time.update();
    }

    public void dispose() {
        try {
            SceneManager.change(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        display.destroy();
    }
}
