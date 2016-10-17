package com.paleon.engine.scenes;

import com.paleon.engine.core.IScene;
import com.paleon.engine.core.SceneManager;

/**
 * Created by Rick on 06.10.2016.
 */
public class Menu implements IScene {

    @Override
    public void loadResources() {

    }

    @Override
    public void init() {

    }

    @Override
    public void update(float deltaTime) throws Exception {
        SceneManager.change("Game");
    }

    @Override
    public void cleanup() {

    }
}
