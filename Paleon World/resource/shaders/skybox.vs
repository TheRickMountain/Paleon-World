#version 330
layout (location = 0) in vec3 position;

out vec3 pass_TextureCoord;

uniform mat4 VP;

void main() {
	gl_Position = VP * vec4(position, 1.0f);
	pass_TextureCoord = position;
}