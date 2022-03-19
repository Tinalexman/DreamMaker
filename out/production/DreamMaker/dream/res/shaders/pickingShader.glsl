#type VERTEX
#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;
layout (location = 2) in vec3 normals;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;


void main()
{
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(vertices, 1.0);
}

#type FRAGMENT
#version 330 core

uniform float fEntityId;

out vec4 outColor;

void main()
{
    outColor = vec4(fEntityId, fEntityId, fEntityId, 1.0);
}