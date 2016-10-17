package com.paleon.engine.core;

/**
 * Created by Rick on 06.10.2016.
 */
public interface IScene {

    void loadResources();

    void init() throws Exception;

    void update(float deltaTime) throws Exception;

    void cleanup();

}
