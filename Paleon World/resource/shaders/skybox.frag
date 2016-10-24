#version 330 core

in vec3 UV;

uniform samplerCube cubeMap;

out vec4 out_Color;

const float lowerLimit = 0.0f;
const float upperLimit = 60.0f;

void main(void){

    out_Color = texture(cubeMap, UV);
    
    float factor = (UV.y - lowerLimit) / (upperLimit - lowerLimit);
	factor = clamp(factor, 0.0f, 1.0f);
	out_Color = mix(vec4(0.85f, 0.85f, 1.0f, 1.0f), out_Color, factor);
}