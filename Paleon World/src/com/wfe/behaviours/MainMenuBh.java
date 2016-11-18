package com.wfe.behaviours;

import com.wfe.core.SceneManager;
import com.wfe.entities.Button;

public class MainMenuBh extends Behaviour {

	private Button newGameBox;
	
	@Override
	public void start() {
		newGameBox = (Button) parent.getChildByName("New Game Button");
	}

	@Override
	public void update(float deltaTime) {
		if(newGameBox.getBehaviour(ButtonBh.class).isPressedDown()) {
			try {
				SceneManager.change("Game");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onGUI() {
		
	}

}
