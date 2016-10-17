package com.paleon.engine.components;


import com.paleon.engine.graph.Texture;
import com.paleon.engine.utils.Color;

public class Image extends Component {

	public Texture texture;
	public Color color;
	
	public Image(Texture texture, Color color) {
		this.texture = texture;
		this.color = color;
		type = Type.IMAGE;
	}
	
}
