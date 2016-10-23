package com.paleon.engine.graph.gui;

import com.paleon.engine.scenegraph.Entity;

public class ProgressBar {

	private float maxValue = 100;
	private float currentValue = 0;
	private Entity barSprite;
	
	private float spriteSizeOnePercent;
	
	public ProgressBar(Entity barSprite) {
		this.currentValue = maxValue;
		this.barSprite = barSprite;
		this.spriteSizeOnePercent = barSprite.scale.x / maxValue;
	}
	
	public void decrease(float value) {	
		float temp = currentValue - value;
		if(temp < 0) {
			value = 0;
			this.currentValue = 0;
		} else {
			this.currentValue -= value;
		}
		
		this.barSprite.scale.x = currentValue * spriteSizeOnePercent;
	}
	
	public void increase(float value) {	
		float temp = currentValue + value;
		if(temp > maxValue) {
			value = 0;
			this.currentValue = maxValue;
		} else {
			this.currentValue += value;
		}
		
		this.barSprite.scale.x = currentValue * spriteSizeOnePercent;
	}
	
	public float getCurrentValue() {
		return currentValue;
	}
	
	public float getMaxValue() {
		return maxValue;
	}
	
}
