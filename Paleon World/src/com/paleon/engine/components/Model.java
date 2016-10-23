package com.paleon.engine.components;

import com.paleon.engine.graph.Mesh;

public class Model extends Component {

	public Mesh mesh;

	public Model(Mesh mesh) {
		this.mesh = mesh;
	}
	
}
