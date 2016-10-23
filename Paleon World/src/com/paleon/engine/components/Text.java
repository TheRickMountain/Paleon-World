package com.paleon.engine.components;

import com.paleon.engine.graph.Mesh;
import com.paleon.engine.graph.font.FontType;
import com.paleon.engine.graph.font.TextMeshData;
import com.paleon.engine.toolbox.Color;

public class Text extends Component {

	public String text;
	public FontType font;
    public float size;
    public Color color;
    
    public float lineMaxSize;
    public int numberOfLines;
    public boolean centered;
	
    public Mesh mesh;
    
    public String lastText = "test";
    
    public Text(String text, FontType font, float fontSize, Color color, float maxLineLength,
            boolean centered) {
        this.text = text;
        this.size = fontSize;
        this.color = color;
        this.font = font;
        this.lineMaxSize = maxLineLength;
        this.centered = centered;
        
        TextMeshData data = font.loadText(this);
        mesh = new Mesh(data.getVertexPositions(), 4);
    }
    
    public void setText(String text) {
      	if(!lastText.equals(text)) {
    		this.lastText = text;
    		this.text = text;
    		mesh.cleanup();
    	
    		TextMeshData data = font.loadText(this);
    		mesh.set(data.getVertexPositions(), 4);
    	}
    }
	
}
