package com.paleon.engine;

public interface IScene {
	
	void loadResources();

	void init();
	
	void update(float deltaTime);
	
	void cleanup();
	
}
