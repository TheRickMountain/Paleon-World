package com.paleon.engine.components;

import com.paleon.engine.graph.Texture2D;
import com.paleon.engine.toolbox.Color;

public class Image extends Component {

	public Texture2D texture;
	public Color color;
	
	public Image(Texture2D texture, Color color) {
		this.texture = texture;
		this.color = color;
	}
	
}
